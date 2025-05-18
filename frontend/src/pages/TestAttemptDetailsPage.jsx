import React, { useEffect, useState } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import api from '../api/axios';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Auth from "../utils/auth";

function TestAttemptDetailsPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const isProfessor = Auth.isProfessor();
    const {
        testAttemptId,
        helperData
    } = location.state || {};
    const backClickPath = isProfessor ? '/review-submissions' : '/review-test-attempt';
    const [reviewData, setReviewData] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!testAttemptId) return;
        api.get(`/api/test-attempts/review/${testAttemptId}`)
            .then(res => setReviewData(res.data))
            .catch(err => setError(err.response?.data?.message || 'Failed to load test review.'));
    }, []);

    if (error) {
        return (
            <>
                <Navbar/>
                <div className="container my-5 text-center">
                    <div className="alert alert-danger">{error}</div>
                </div>
                <Footer/>
            </>
        );
    }

    if (!reviewData) return <p className="text-center mt-5">Loading...</p>;

    const { testAttemptBasicInfoDto, questionsToReview } = reviewData;

    return (
        <>
            <Navbar />
            <div className="container my-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4 className="fw-bold">Reviewed Answers</h4>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => navigate(backClickPath, {
                            state: {
                                testId: helperData?.testId,
                                subjectId: helperData?.subjectId,
                                subjectName: helperData?.subjectName,
                                testTitle: helperData?.testTitle
                            }
                        })}
                    >
                        ← Back to Submissions
                    </button>
                </div>

                <div className="row">
                    <div className="col-md-4">
                        <div className="bg-light p-4 rounded shadow-sm">
                            <h5 className="mb-3">Test Info</h5>
                            <p><strong>Title:</strong> {testAttemptBasicInfoDto.testTitle}</p>
                            <p><strong>Student:</strong> <i className="bi bi-person-circle me-1"></i>{testAttemptBasicInfoDto.studentEmail}</p>
                            <p><strong>Submitted:</strong> {new Date(testAttemptBasicInfoDto.submissionDate).toLocaleString()}</p>
                            <p><strong>Score:</strong> {testAttemptBasicInfoDto.score} / {testAttemptBasicInfoDto.testMaxPoints}</p>
                        </div>
                    </div>

                    <div className="col-md-8">
                        {questionsToReview.map((q, idx) => (
                            <div key={idx} className="mb-4 p-4 bg-white rounded shadow-sm">
                                <h5>Question {idx + 1}:</h5>
                                <p>{q.questionText}</p>
                                <p className="text-muted mb-2">Score: {q.earnedScore} / {q.maximumPoints}</p>

                                {q.questionTypeDto.name === 'OPEN_ENDED' ? (
                                    <>
                                        <div className="border p-2 rounded bg-light mb-2">
                                            <strong>Student Answer:</strong>
                                            <p className="mb-0">{q.providedOpenEndedAnswer}</p>
                                        </div>
                                        <div className="border p-2 rounded bg-white">
                                            <strong>AI Feedback:</strong>
                                            <p className="mb-0 text-muted">{q.aiEvaluationComment}</p>
                                        </div>
                                    </>
                                ) : (
                                    <div>
                                        {q.allAnswersForQuestion.map((a, index) => (
                                            <div key={a.id} className="form-check">
                                                <input
                                                    className="form-check-input"
                                                    type={q.questionTypeDto.name === 'SINGLE_CHOICE' ? 'radio' : 'checkbox'}
                                                    name={`q_${idx}`}
                                                    value={a.id}
                                                    disabled
                                                    checked={q.providedAnswerIds.includes(a.id)}
                                                />
                                                <label className="form-check-label">
                                                    {`${index + 1}. ${a.answerText}`}
                                                </label>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}

export default TestAttemptDetailsPage;
