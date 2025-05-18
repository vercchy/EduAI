import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { useNavigate } from "react-router-dom";

function UpcomingTestCard(props) {
    const { isProfessor, id, title, description, startDate, endDate, duration, maxPoints } = props;
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

                    {!isProfessor && (
                        <div className="text-start">
                            <br/>
                            <button
                                className="btn btn-dark btn-sm"
                                onClick={() => navigate('/take-test', { state: { testId: id, duration: duration} })}>
                                Take Test
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default UpcomingTestCard;
