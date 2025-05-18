import React from 'react';
import UpcomingTestCard from './UpcomingTestCard';
import Navbar from "../Navbar";
import Footer from "../Footer";

function UpcomingTests({ isProfessor, subjectName, message, testData }) {
    const pageTitle = isProfessor
        ? "All Tests"
        : "Currently Available Tests";
    return (
        <>
            <Navbar/>
            <div className="container mt-5">
                <div className="text-center mb-4">
                    <h5 className="text-muted">{pageTitle}</h5>
                    <h2 className="fw-bold">Subject: {subjectName}</h2>
                </div>
                {message && <div className="alert alert-danger">{message}</div>}
                {testData.map((test) => (
                    <div key={test.id} className="mb-5">
                        <h5 className="mb-4">{test.title}</h5>
                        <hr className="mt-0 mb-4"/>
                        <div className="row bg-light p-4 rounded-3">
                            <UpcomingTestCard key={test.id} isProfessor={isProfessor} {...test} />
                        </div>
                    </div>
                ))}
            </div>
            <Footer/>
        </>
    );
}

export default UpcomingTests;
