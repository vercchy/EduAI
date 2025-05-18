import React from 'react';
import UpcomingTestCard from './UpcomingTestCard';
import Navbar from "../Navbar";
import Footer from "../Footer";
import { useNavigate } from "react-router-dom";

function UpcomingTests({ isProfessor, subjectName, subjectId, message, testData }) {
    const navigate = useNavigate();
    const pageTitle = isProfessor
        ? "All Tests"
        : "Currently Available Tests";
    return (
        <>
            <Navbar/>
            <div className="container mt-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h5 className="text-muted">{pageTitle}</h5>
                        <h2 className="fw-bold">Subject: {subjectName}</h2>
                    </div>
                    <div>
                        <button
                            className="btn btn-outline-secondary"
                            onClick={() => navigate('/home')}
                        >
                            ← Back to Subjects
                        </button>
                    </div>
                </div>

                {message && <div className="alert alert-danger">{message}</div>}

                {testData.map((test) => (
                    <div key={test.id} className="mb-5">
                        <h5 className="mb-4">{test.title}</h5>
                        <hr className="mt-0 mb-4"/>
                        <div className="row bg-light p-4 rounded-3">
                            <UpcomingTestCard
                                key={test.id}
                                isProfessor={isProfessor}
                                subjectName={subjectName}
                                subjectId={subjectId}
                                {...test}
                            />
                        </div>
                    </div>
                ))}
            </div>
            <Footer/>
        </>
    );
}

export default UpcomingTests;
