import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';

function UpcomingTestCard({ index, id, title, description }) {
    return (
        <div className="col-md-4 mb-4">
            <div className="card h-100 border-0 rounded-3">
                <div className="card-body p-5">
                    <h5 className="text-end primary-color fw-bold fs-1">{String(index + 1).padStart(2, '0')}</h5>
                    <h6 className="card-title">{title}</h6>
                    <p className="card-text small">{description}</p>
                </div>
                <div className="card-footer bg-white border-0 d-flex align-items-center text-muted p-5">
                    <span className="primary-color bg-light rounded p-3 ms-auto fw-bold">
                        <a href={`test/${id}/results`} className="text-reset text-decoration-none">
                            <i className="bi bi-arrow-up-right me-2"></i>
                            <span> View results</span>
                        </a>
                    </span>
                </div>
            </div>
        </div>
    );
}

export default UpcomingTestCard;