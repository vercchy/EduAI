import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';

function Subject() {
    const { id } = useParams(); // subjectId from URL
    const [studentEmail, setStudentEmail] = useState("");
    const [message, setMessage] = useState("");
    const [subjectDetails, setSubjectDetails] = useState(null);
    const [currentUser, setCurrentUser] = useState(null);


    useEffect(() => {
        axios.get(`http://localhost:9090/api/subjects/${id}`, { withCredentials: true })
            .then(response => {
                setSubjectDetails(response.data);
            })
            .catch(error => {
                console.error("Could not load subject", error);
            });
        // Fetch current user
        axios.get('http://localhost:9090/api/auth/current-user', { withCredentials: true })
            .then(response => {
                setCurrentUser(response.data);
            })
            .catch(error => {
                console.error("Could not load current user", error);
            });

    }, [id]);

    const handleAssign = async () => {
        try {
            const response = await axios.post('http://localhost:9090/api/subjects/assign-student', {
                subjectId: id,
                studentEmail: studentEmail
            }, { withCredentials: true });

            setMessage(response.data);
            // Refresh student list
            const updatedSubject = await axios.get(`http://localhost:9090/api/subjects/${id}`, { withCredentials: true });
            setSubjectDetails(updatedSubject.data);
        } catch (error) {
            setMessage("Error assigning student.");
            console.error(error);
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