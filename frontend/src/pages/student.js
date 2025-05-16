// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import logo from '../assets/eduAI_logo.svg';
import grid1 from '../assets/grid1.png';
import grid2 from '../assets/grid2.png';
import grid3 from '../assets/grid3.png';
import zhurka_slika from '../assets/zhurkaaa.svg';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { root } from "../index"
import axios from 'axios';




// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function Student() {
    const [subjects, setSubjects] = useState([]);
    const studentId = 1; // Replace with actual logged-in student's ID (e.g., from auth context)
    const token = localStorage.getItem("token");

    useEffect(() => {
        axios.get('http://localhost:9090/api/subjects/student', {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`
            }
        }).then(res => {
            setSubjects(res.data);
        }).catch(err => {
            console.error("Error fetching student subjects:", err);
        });
    }, []);

    return (
        <>
            <Navbar />
            <div>
                <div className="h2 px-4 mb-4">Enrolled Courses</div>

                {subjects.map(subject => (
                    <div key={subject.id} className="subject-card mb-5 px-4">
                        <div className="h6 mb-2">{subject.name}</div>
                        <div className="d-flex justify-content-between align-items-center">
                            <div className="me-3">
                                <p className="mb-0">{subject.description}</p>
                            </div>
                            <div>
                                <button className="btn btn-dark">View Course</button>
                            </div>
                        </div>

                        <div className="container mt-3">
                            <div className="row">
                                <div className="col"><img src={grid1} alt="EduAI Grid" width="300" height="300" /></div>
                                <div className="col"><img src={grid2} alt="EduAI Grid" width="300" height="300" /></div>
                                <div className="col"><img src={grid3} alt="EduAI Grid" width="300" height="300" /></div>
                            </div>
                        </div>

                        <div className="d-flex justify-content-between align-items-center mt-3">
                            <div>
                                <p className="mb-0">
                                    By {subject.professorFullName}
                                </p>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <Footer />
        </>
    );
}

export default Student;



