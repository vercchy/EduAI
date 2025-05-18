import React, { useState } from 'react';
import api from "../api/axios";
import { useLocation } from "react-router-dom";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import 'bootstrap/dist/css/bootstrap.min.css';

function CreateTest() {
    const location = useLocation();
    const subjectId = location.state?.subjectId;

    const [testInfo, setTestInfo] = useState({
        title: '',
        description: '',
        startDate: '',
        endDate: '',
        duration: '',
        maxPoints: ''
    });

    const [questions, setQuestions] = useState([]);
    const [successMsg, setSuccessMsg] = useState('');

    const questionTypeMapping = {
        single: 0,
        multiple: 1,
        essay: 2
    };

    const handleTestInfoChange = (e) => {
        const { name, value } = e.target;
        setTestInfo(prev => ({ ...prev, [name]: value }));
    };

    const addQuestion = (type) => {
        setQuestions(prev => [
            ...prev,
            {
                id: Date.now(),
                type,
                text: '',
                maxPoints: '',
                options: type !== 'essay' ? [''] : [],
                correctAnswers: type === 'multiple' ? [] : (type === 'single' ? null : undefined),
            }
        ]);
    };

    const updateQuestion = (id, field, value) => {
        setQuestions(prev =>
            prev.map(q => q.id === id ? { ...q, [field]: value } : q)
        );
    };

    const updateOption = (qId, index, value) => {
        setQuestions(prev =>
            prev.map(q => {
                if (q.id !== qId) return q;
                const newOptions = [...q.options];
                newOptions[index] = value;
                return { ...q, options: newOptions };
            })
        );
    };

    const addOption = (qId) => {
        setQuestions(prev =>
            prev.map(q =>
                q.id === qId ? { ...q, options: [...q.options, ''] } : q
            )
        );
    };

    const removeQuestion = (id) => {
        setQuestions(prev => prev.filter(q => q.id !== id));
    };

    const updateCorrectAnswer = (qId, index) => {
        setQuestions(prev =>
            prev.map(q =>
                q.id === qId ? { ...q, correctAnswers: index } : q
            )
        );
    };

    const toggleCorrectAnswer = (qId, index) => {
        setQuestions(prev =>
            prev.map(q => {
                if (q.id !== qId) return q;
                const current = q.correctAnswers || [];
                const updated = current.includes(index)
                    ? current.filter(i => i !== index)
                    : [...current, index];
                return { ...q, correctAnswers: updated };
            })
        );
    };

    const updateQuestionPoints = (id, value) => {
        setQuestions(prev =>
            prev.map(q =>
                q.id === id ? { ...q, maxPoints: parseInt(value) || 0 } : q
            )
        );
    };

    const handleSubmit = async () => {
        const token = localStorage.getItem("token");

        const formattedQuestions = questions.map(q => ({
            questionText: q.text,
            questionType: questionTypeMapping[q.type],
            maxPoints: q.maxPoints,
            answers: q.type === 'essay' ? [] : q.options.map((opt, index) => ({
                answerText: opt,
                isCorrect: q.type === 'multiple'
                    ? (q.correctAnswers || []).includes(index)
                    : q.correctAnswers === index
            }))
        }));

        const payload = {
            basicTestCreationInfo: {
                title: testInfo.title,
                description: testInfo.description,
                startDate: testInfo.startDate,
                endDate: testInfo.endDate,
                duration: parseInt(testInfo.duration),
                maxPoints: parseInt(testInfo.maxPoints),
                subjectId: parseInt(subjectId)
            },
            questions: formattedQuestions
        };

        try {
            await api.post('/api/tests', payload);
            setSuccessMsg("Test was created successfully.");
            setTestInfo({
                title: '',
                description: '',
                startDate: '',
                endDate: '',
                duration: '',
                maxPoints: ''
            });
            setQuestions([]);
        } catch (err) {
            console.error('Error submitting test:', err.response?.data || err.message);
        }
    };

    return (
        <>
            <Navbar />
            <div className="container my-5">
                <h2 className="mb-4">Create New Test</h2>

                <div className="mb-4">
                    <input
                        className="form-control mb-2"
                        type="text"
                        name="title"
                        placeholder="Title"
                        value={testInfo.title}
                        onChange={handleTestInfoChange}
                    />
                    <textarea
                        className="form-control mb-2"
                        name="description"
                        placeholder="Description"
                        value={testInfo.description}
                        onChange={handleTestInfoChange}
                    />
                    <div className="row mb-2">
                        <div className="col">
                            <label className="form-label">Start Date</label>
                            <input
                                type="datetime-local"
                                className="form-control"
                                name="startDate"
                                value={testInfo.startDate}
                                onChange={handleTestInfoChange}
                            />
                        </div>
                        <div className="col">
                            <label className="form-label">End Date</label>
                            <input
                                type="datetime-local"
                                className="form-control"
                                name="endDate"
                                value={testInfo.endDate}
                                onChange={handleTestInfoChange}
                            />
                        </div>
                    </div>
                    <div className="row mb-2">
                        <div className="col">
                            <input
                                type="number"
                                className="form-control"
                                name="duration"
                                placeholder="Duration (minutes)"
                                value={testInfo.duration}
                                onChange={handleTestInfoChange}
                            />
                        </div>
                        <div className="col">
                            <input
                                type="number"
                                className="form-control"
                                name="maxPoints"
                                placeholder="Max Points"
                                value={testInfo.maxPoints}
                                onChange={handleTestInfoChange}
                            />
                        </div>
                    </div>
                </div>

                <div className="mb-3">
                    <button className="btn btn-outline-primary me-2" onClick={() => addQuestion('single')}>+ Single Choice</button>
                    <button className="btn btn-outline-success me-2" onClick={() => addQuestion('multiple')}>+ Multiple Choice</button>
                    <button className="btn btn-outline-warning me-2" onClick={() => addQuestion('essay')}>+ Essay</button>
                </div>

                {questions.map((q, idx) => (
                    <div key={q.id} className="card mb-4">
                        <div className="card-body">
                            <div className="d-flex justify-content-between">
                                <h5>Question {idx + 1} ({q.type})</h5>
                                <button className="btn btn-sm btn-danger" onClick={() => removeQuestion(q.id)}>Remove</button>
                            </div>
                            <textarea
                                className="form-control my-2"
                                rows="2"
                                placeholder="Write your question here..."
                                value={q.text}
                                onChange={(e) => updateQuestion(q.id, 'text', e.target.value)}
                            />
                            <div className="mb-2">
                                <label>Points for this question:</label>
                                <input
                                    type="number"
                                    className="form-control"
                                    value={q.maxPoints}
                                    onChange={(e) => updateQuestionPoints(q.id, e.target.value)}
                                    min="1"
                                />
                            </div>

                            {q.type !== 'essay' && (
                                <>
                                    <h6>Options:</h6>
                                    {q.options.map((opt, i) => (
                                        <div key={i} className="input-group mb-2">
                                            <span className="input-group-text">
                                                {q.type === 'single' ? (
                                                    <input
                                                        type="radio"
                                                        name={`correct-${q.id}`}
                                                        checked={q.correctAnswers === i}
                                                        onChange={() => updateCorrectAnswer(q.id, i)}
                                                    />
                                                ) : (
                                                    <input
                                                        type="checkbox"
                                                        checked={q.correctAnswers.includes(i)}
                                                        onChange={() => toggleCorrectAnswer(q.id, i)}
                                                    />
                                                )}
                                            </span>
                                            <input
                                                type="text"
                                                className="form-control"
                                                value={opt}
                                                onChange={(e) => updateOption(q.id, i, e.target.value)}
                                                placeholder={`Option ${i + 1}`}
                                            />
                                        </div>
                                    ))}
                                    <button className="btn btn-sm btn-outline-secondary" onClick={() => addOption(q.id)}>+ Add Option</button>
                                </>
                            )}

                            {q.type === 'essay' && (
                                <p className="text-muted">Student will type their answer in a textarea.</p>
                            )}
                        </div>
                    </div>
                ))}

                {questions.length > 0 && (
                    <button className="btn btn-primary" onClick={handleSubmit}>Submit Test</button>
                )}

                {successMsg && (
                    <div className="alert alert-success mt-4">
                        {successMsg}
                    </div>
                )}
            </div>
            <Footer />
        </>
    );
}

export default CreateTest;
