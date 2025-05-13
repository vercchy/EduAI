// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import axios from 'axios';
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { root } from "../index"




// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function Subject() {
    const {id} = useParams(); // subjectId from URL
    const [studentEmail, setStudentEmail] = useState("");
    const [message, setMessage] = useState("");
    const [subjectName, setSubjectName] = useState("");

    useEffect(() => {
        axios.get(`http://localhost:9090/api/subjects/${id}`, { withCredentials: true })
            .then(response => setSubjectName(response.data.name))
            .catch(error => console.error("Could not load subject", error));
    }, [id]);

    const handleAssign = async () => {
        try {
            const response = await axios.post('http://localhost:9090/api/subjects/assign-student', {
                subjectId: id,
                studentEmail: studentEmail
            }, {withCredentials: true});

            setMessage(response.data); // "Access successfully assigned to student."
        } catch (error) {
            setMessage("Error assigning student.");
            console.error(error);
        }
        return (
            <>
                <Navbar/>
                <div className="container my-4">
                    <h3>Subject: {subjectName || `Subject ID ${id}`}</h3>

                    <div className="my-4">
                        <label>Student Email:</label>
                        <input
                            type="email"
                            className="form-control mb-2"
                            value={studentEmail}
                            onChange={e => setStudentEmail(e.target.value)}
                            placeholder="Enter student email"
                        />
                        <button className="btn btn-danger" onClick={handleAssign}>
                            Add a student to subject
                        </button>

                        {message && <p className="mt-2">{message}</p>}
                    </div>
                </div>
                <Footer/>
            </>
        );
    }
}

export default Subject;


