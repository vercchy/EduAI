import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/axios";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import TestSubmissionCard from "../components/TakenTests/TestSubmisionCard";

function ReviewSubmissionsPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const {
        testId,
        subjectId,
        subjectName,
        testTitle
    } = location.state || {};
    const helperData = {testId, subjectId, subjectName, testTitle};
    const [submissions, setSubmissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!testId) return;
        api.get(`/api/test-attempts/summary/${testId}`)
            .then(res => {
                setSubmissions(res.data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.response?.data?.message || 'Failed to load submissions.');
                setLoading(false);
            });
    }, []);

    return (
        <>
            <Navbar/>
            <div className="container my-5">
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h4 className="fw-bold">{testTitle}</h4>
                        <p className="text-muted mb-0">
                            Submissions for subject: <strong>{subjectName}</strong>
                        </p>
                    </div>
                    <button
                        className="btn btn-outline-secondary"
                        onClick={() => navigate('/tests-for-subject', {
                            state: { subjectId, subjectName }
                        })}
                    >
                        <i className="bi bi-arrow-left me-1"></i> Back to Tests
                    </button>
                </div>

                {loading && <p>Loading submissions...</p>}
                {error && <div className="alert alert-danger">{error}</div>}

                <div className="row">
                    {submissions.map((submission) => (
                        <div key={submission.id} className="col-md-6 col-lg-4 mb-4">
                            <TestSubmissionCard submission={submission} navigate={navigate} helperData={helperData} />
                        </div>
                    ))}
                </div>
            </div>
            <Footer/>
        </>
    );
}

export default ReviewSubmissionsPage;