import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../api/axios';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import { useNavigate } from 'react-router-dom';

function Subject() {

    const navigate = useNavigate();
    const location = useLocation();
    const subjectId = location.state?.subjectId;
    const isProfessor = localStorage.getItem('role') === 'ROLE_PROFESSOR';
    const token = localStorage.getItem('token');
    const [studentEmail, setStudentEmail] = useState("");
    const [message, setMessage] = useState("");
    const [subjectDetails, setSubjectDetails] = useState(null);

    const fetchSubjectDetails = () => {
        api.get(`/api/subjects/${subjectId}`)
            .then(response => {
                setSubjectDetails(response.data);
            })
            .catch(error => {
                console.error("Could not load subject: ", error.response.data.message);
            });
    };


    useEffect(() => {
       fetchSubjectDetails();
    }, [subjectId]);

    const handleAssign = async () => {
        try {
            const response = await api.post('/api/subjects/assign-student', {
                subjectId: subjectId,
                studentEmail: studentEmail
            });

            setMessage(response.data);
            fetchSubjectDetails();
        } catch (error) {
            setMessage(error.response.data.message);
        }
    };

    if (!subjectDetails) {
        return <p>Loading subject details...</p>;
    }

    return (
        <>
            <Navbar />
            <div className="container my-4">
                <h2>Subject: {subjectDetails.name}</h2>
                <p><strong>Description:</strong> {subjectDetails.description}</p>
                <p><strong>Difficulty:</strong> {subjectDetails.difficultyLevel}</p>
                <p><strong>Created At:</strong> {new Date(subjectDetails.createdAt).toLocaleString()}</p>
                <p><strong>Professor:</strong> {subjectDetails.teacherDto.user.firstName} {subjectDetails.teacherDto.user.lastName} ({subjectDetails.teacherDto.user.email})</p>
                {isProfessor &&
                    <>
                        <button
                            className="btn btn-dark"
                            onClick={() => navigate('/create-test', { state: { subjectId } })}
                        >
                            Create a new test
                        </button>


                        <hr />
                <h4>Assign Student</h4>
                <input
                    type="email"
                    className="form-control mb-2"
                    value={studentEmail}
                    onChange={e => setStudentEmail(e.target.value)}
                    placeholder="Enter student email"
                />
                <button className="btn btn-primary" onClick={handleAssign}>
                    Add student to subject
                </button>
                {message && <p className="mt-2 text-info">{message}</p>}
                <hr />
                    </>
                }

                <h4>Assigned Students</h4>
                {subjectDetails.students.length === 0 ? (
                    <p>No students assigned yet.</p>
                ) : (
                    <ul className="list-group">
                        {subjectDetails.students.map(student => (
                            <li key={student.id} className="list-group-item">
                                {student.firstName} {student.lastName} ({student.email})
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <Footer />
        </>
    );
}

export default Subject;