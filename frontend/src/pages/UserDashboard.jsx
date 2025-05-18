import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import { useNavigate } from 'react-router-dom';

function UserDashboard() {
    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [role, setRole] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        api.get('/api/subjects')
            .then(response => {
                setSubjects(response.data);
                setLoading(false);
                setRole(localStorage.getItem('role'))
            })
            .catch(error => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data ||
                    error.message || 'Unknown error occurred';
                setError(`Failed to load subjects: ${message}`);
                setLoading(false);
            });
    }, []);

    const headingText = role === 'ROLE_PROFESSOR' ? 'Created Subjects' : 'Enrolled Subjects';

    return (
        <>
            <div className="container-fluid">
                <div className="d-flex justify-content-between align-items-center px-4 pt-4 mb-4">
                    <h2 className="h2 mb-0">{headingText}</h2>
                    {role === 'ROLE_PROFESSOR' && (
                        <button
                            className="btn btn-outline-primary btn-sm"
                            onClick={() => navigate('/add-subject')}>
                            <i className="bi bi-plus-circle me-1"></i> Create Subject
                        </button>
                    )}
                </div>

                {loading && <div className="px-4">Loading...</div>}
                {error && <div className="text-danger px-4">{error}</div>}

                <div className="row px-4">
                    {subjects.map((subject) => (
                        <div key={subject.id} className="col-12 mb-4">
                            <div className="card w-100 shadow-sm">
                                <div className="card-body">
                                    <h5 className="card-title">{subject.name}</h5>
                                    <p className="card-text text-muted mb-2">By {subject.professorFullName}</p>
                                    <p className="card-text">
                                        {subject.description}
                                    </p>
                                    <div className="d-flex gap-3">
                                        <button
                                            className="btn btn-dark"
                                            onClick={() => navigate('/course-details', { state: { subjectId: subject.id } })}>
                                            View Course
                                        </button>
                                        <button
                                            className="btn btn-dark"
                                            onClick={() => navigate('/tests-for-subject', { state: { subjectId: subject.id, subjectName: subject.name } })}>
                                            View Tests
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default UserDashboard;
