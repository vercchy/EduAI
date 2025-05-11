import React from 'react';
import 'bootstrap-icons/font/bootstrap-icons.css';

function UpcomingTestCard({ index, title, description, date, time }) {
    return (
        <>
        <div className="col-md-4 mb-4">
            <div className="card h-100 border-0 rounded-3">
                <div className="card-body p-5">
                    <h5 className="text-end primary-color fw-bold fs-1">{String(index + 1).padStart(2, '0')}</h5>
                    <h6 className="card-title">{title}</h6>
                    <p className="card-text small">{description}</p>
                </div>
                <div className="card-footer bg-white border-0 d-flex align-items-center text-muted p-5 fw-bold">
                    <span className="primary-color bg-light rounded p-1">
                        <i className="bi bi-clock me-2"></i>
                        {date} &nbsp; {time}
                    </span>
                </div>
            </div>
        </div>
        </>
    );
}

export default UpcomingTestCard;