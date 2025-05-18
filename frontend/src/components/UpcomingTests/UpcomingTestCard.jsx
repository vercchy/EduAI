import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from "react-router-dom";

function UpcomingTestCard(props) {
    const { isProfessor, subjectName, subjectId, id, title, description, startDate, endDate, duration, maxPoints, testTakenByStudent } = props;
    const navigate = useNavigate();
    return (
        <div className="col-md-6 col-lg-4 mb-4">
            <div className="card shadow-sm h-100 border-0 rounded-4">
                <div className="card-body p-4">
                    <h5 className="card-title fw-semibold">{title}</h5>
                    <p className="card-text text-muted">{description}</p>
                </div>
                <div className="card-footer bg-light p-3 rounded-bottom">
                    <ul className="list-unstyled mb-0 text-sm text-dark fw-medium">
                        <li className="mb-1">
                            <i className="bi bi-calendar-event text-primary me-2"></i>
                            Start: <span className="text-dark">{new Date(startDate).toLocaleString()}</span>
                        </li>
                        <li className="mb-1">
                            <i className="bi bi-calendar-check text-success me-2"></i>
                            End: <span className="text-dark">{new Date(endDate).toLocaleString()}</span>
                        </li>
                        <li className="mb-1">
                            <i className="bi bi-stopwatch text-warning me-2"></i>
                            Duration: <span className="text-dark">{duration} min</span>
                        </li>
                        <li>
                            <i className="bi bi-star text-danger me-2"></i>
                            Max Points: <span className="text-dark">{maxPoints}</span>
                        </li>
                    </ul>

                    <div className="text-start">
                        {!isProfessor && (
                            <>
                                {!testTakenByStudent ? (
                                    <button
                                        className="btn btn-dark btn-sm"
                                        onClick={() =>
                                            navigate('/take-test', {
                                                state: { testId: id, duration, subjectName, subjectId }
                                            })
                                        }
                                    >
                                        <i className="bi bi-pencil-square me-1"></i> Take Test
                                    </button>
                                ) : (
                                    <div className="mt-3 p-3 border rounded bg-light text-center">
                                        <span className="text-success fw-semibold d-block mb-2">
                                            <i className="bi bi-check-circle me-2"></i>Test Already Taken
                                        </span>
                                        <button
                                            className="btn btn-outline-primary btn-sm"
                                            onClick={() =>
                                                navigate('/review-test', {
                                                    state: { testId: id, subjectId, subjectName }
                                                })
                                            }>
                                            <i className="bi bi-search me-1"></i> Review Test
                                        </button>
                                    </div>
                                )}
                            </>
                        )}

                        {isProfessor && (
                            <div className="mt-3 p-3 border rounded bg-light text-center">
                                <span className="text-info fw-semibold d-block mb-2">
                                    <i className="bi bi-people-fill me-2"></i>Test Overview
                                </span>
                                <button
                                    className="btn btn-outline-primary btn-sm"
                                    onClick={() =>
                                        navigate('/review-submissions', {
                                            state: { testId: id, subjectId, subjectName, testTitle: title }
                                        })
                                    }>
                                    <i className="bi bi-search me-1"></i> Review Submissions
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default UpcomingTestCard;
