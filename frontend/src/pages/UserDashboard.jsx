import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
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
        const headers = { Authorization: `Bearer ${token}` }
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

    const headingText = role === 'ROLE_PROFESSOR' ? 'Created Courses' : 'Enrolled Courses';

    return (
        <>
            <Navbar/>

            <div className="container-fluid">
                <div className="h2 px-4 mb-4">{headingText}</div>

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
                                    <button
                                        className="btn btn-dark"
                                        onClick={() => navigate('/course-details', { state: { subjectId: subject.id } })}>
                                        View Course
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <Footer/>
        </>
    );
}

export default UserDashboard;
