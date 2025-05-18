import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';

function TestSubmissionCard({ submission, navigate, helperData }) {
    const { id, status, score, submissionDate, testTitle, testMaxPoints, studentEmail } = submission;
    const isGraded = status.name === 'GRADED';

    return (
        <div className="card h-100 shadow-sm border-0 rounded-4">
            <div className="card-body p-4">
                <h5 className="fw-bold mb-3">{testTitle}</h5>
                <p className="text-muted mb-1">
                    <i className="bi bi-calendar-check me-2"></i>
                    Submitted: {new Date(submissionDate).toLocaleString()}
                </p>
                <p className="text-muted mb-1">
                    <i className="bi bi-person-circle me-2"></i>
                    Submitted By: {studentEmail}
                </p>
                <p className="text-muted mb-2">
                    <i className="bi bi-hourglass-split me-2"></i>
                    Status: <span className={isGraded ? 'text-success' : 'text-warning'}>{status.name}</span>
                </p>
                {isGraded ? (
                    <p className="mb-3">
                        <i className="bi bi-star-fill me-2 text-primary"></i>
                        Score: <strong>{score}/{testMaxPoints}</strong>
                    </p>
                ) : (
                    <p className="mb-3 text-danger">
                        <i className="bi bi-info-circle me-2"></i>
                        Test not yet graded
                    </p>
                )}

                <div className="text-end">
                    <button
                        className="btn btn-outline-primary btn-sm"
                        onClick={() => navigate('/review-test-attempt-details', { state: { testAttemptId: id, helperData: helperData } })}
                        disabled={!isGraded}
                    >
                        <i className="bi bi-eye me-1"></i> Review Test
                    </button>
                </div>
            </div>
        </div>
    );
}

export default TestSubmissionCard;
