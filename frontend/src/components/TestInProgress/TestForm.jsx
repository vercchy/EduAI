import React, { useState, useImperativeHandle, forwardRef } from 'react';
import api from "../../api/axios";
import { useNavigate } from "react-router-dom";

const TestForm = forwardRef(({ questions, testAttemptId, subjectName, subjectId }, ref) => {
    const navigate = useNavigate();
    const [responses, setResponses] = useState({});
    const [message, setMessage] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleChange = (questionId, value, isOpenEnded) => {
        setResponses(prev => {
            const current = prev[questionId] || { answerIds: [], openEndedAnswerText: null };
            return {
                ...prev,
                [questionId]: {
                    ...current,
                    ...(isOpenEnded
                        ? { openEndedAnswerText: value }
                        : { answerIds: value })
                }
            };
        });
    };

    const handleSubmit = async () => {
        if (submitting) return;
        setSubmitting(true);
        try {
            const formattedResponses = Object.entries(responses).map(([questionId, value]) => ({
                questionId: parseInt(questionId),
                openEndedAnswerText: value.openEndedAnswerText || null,
                answerIds: value.answerIds?.length ? value.answerIds : []
            }));

            const payload = {
                testAttemptId,
                responses: formattedResponses
            };

            const res = await api.post('/api/test-attempts/submit', payload);
            setMessage(res.data);
        } catch (error) {
            setMessage(error.response?.data?.message || 'Submission failed.');
        } finally {
            setSubmitting(false);
            navigate('/tests-for-subject', { state: { subjectId: subjectId, subjectName: subjectName } })
        }
    };

    useImperativeHandle(ref, () => ({
        submitTest: handleSubmit
    }));


    return (
        <form onSubmit={e => { e.preventDefault(); handleSubmit(); }}>
            {questions.map((q, idx) => (
                <div key={q.id} className="mb-4 p-4 bg-white rounded shadow-sm">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                        <h5 className="mb-0">Question {idx + 1}:</h5>
                        <span className="badge bg-secondary">Points: {q.maxPoints}</span>
                    </div>
                    <p>{q.questionText}</p>

                    {q.questionType.name === 'OPEN_ENDED' ? (
                        <textarea
                            className="form-control"
                            rows="4"
                            placeholder="Write your answer..."
                            value={responses[q.id]?.openEndedAnswerText || ''}
                            onChange={e => handleChange(q.id, e.target.value, true)}
                        />
                    ) : (
                        <div>
                            {q.answers.map((a, index) => (
                                <label key={a.id} className="d-block">
                                    <input
                                        type={q.questionType.name === 'SINGLE_CHOICE' ? 'radio' : 'checkbox'}
                                        name={`q_${q.id}`}
                                        value={a.id}
                                        onChange={e => {
                                            const isChecked = e.target.checked;
                                            setResponses(prev => {
                                                const current = prev[q.id]?.answerIds || [];
                                                const updated = q.questionType.name === 'MULTIPLE_CHOICE'
                                                    ? isChecked
                                                        ? [...current, a.id]
                                                        : current.filter(id => id !== a.id)
                                                    : [a.id];
                                                return {
                                                    ...prev,
                                                    [q.id]: {
                                                        ...prev[q.id],
                                                        answerIds: updated
                                                    }
                                                };
                                            });
                                        }}
                                        checked={
                                            q.questionType.name === 'MULTIPLE_CHOICE'
                                                ? (responses[q.id]?.answerIds || []).includes(a.id)
                                                : (responses[q.id]?.answerIds || [])[0] === a.id
                                        }
                                    />{' '}
                                    {`${index + 1}. ${a.answerText}`}
                                </label>
                            ))}
                        </div>
                    )}
                </div>
            ))}
            <div className="text-end mt-4">
                {message && <div className="alert alert-info">{message}</div>}
                <button className="btn btn-primary" type="submit" disabled={submitting}>
                    {submitting ? 'Submitting...' : 'Submit Test'}
                </button>
            </div>
        </form>
    );
});


export default TestForm;
