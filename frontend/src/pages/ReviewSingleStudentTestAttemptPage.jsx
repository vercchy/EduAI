import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/axios";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import TestSubmissionCard from "../components/TakenTests/TestSubmisionCard";

function ReviewSingleStudentTestAttemptPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const {
        testId,
        subjectName,
        subjectId,
        testTitle
    } = location.state || {};
    const helperData = {testId, subjectId, subjectName, testTitle};
    const [submission, setSubmission] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!testId) return;
        api.get(`/api/test-attempts/${testId}`)
            .then(res => {
                setSubmission(res.data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.response?.data?.message || "Failed to load submission.");
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <>
                <Navbar />
                <div className="container my-5 text-center">
                    <p>Loading your submission...</p>
                </div>
                <Footer />
            </>
        );
    }

    if (error) {
        return (
            <>
                <Navbar />
                <div className="container my-5 text-center">
                    <div className="alert alert-danger">{error}</div>
                </div>
                <Footer />
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="container my-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <h4 className="fw-bold">Review of Your Test</h4>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => navigate('/tests-for-subject', {
                            state: { subjectId, subjectName }
                        })}>
                        ← Back to Tests
                    </button>
                </div>
                <p className="text-muted mb-4">Here you can find the summary of your submission for <strong>{submission.testTitle}</strong>.</p>
                <div className="row">
                    <div className="col-md-6 offset-md-3">
                        <TestSubmissionCard submission={submission} helperData={helperData} navigate={navigate} />
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}

export default ReviewSingleStudentTestAttemptPage;