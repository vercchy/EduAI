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
    const helperData = { testId, subjectId, subjectName, testTitle };

    const [submissions, setSubmissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [pdfUrl, setPdfUrl] = useState(null);
    const [showModal, setShowModal] = useState(false);

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

    const openPdfReportDialog = () => {
        api.get(`/professor-report/${subjectId}/${testId}/pdf`, {
            responseType: 'blob'
        }).then(response => {
            console.log(response.data)
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = URL.createObjectURL(blob);
            setPdfUrl(url);
            setShowModal(true);
        }).catch((err) => {
            console.error("PDF fetch failed:", err);
            alert("Failed to load PDF report.");
        });
    };

    const viewJsonReport = () => {
        api.get(`/professor-report/${subjectId}/${testId}`)
            .then(res => {
                alert(JSON.stringify(res.data, null, 2));
            })
            .catch(() => alert('Failed to load report.'));
    };

    const closeModal = () => {
        setShowModal(false);
        if (pdfUrl) {
            URL.revokeObjectURL(pdfUrl);
            setPdfUrl(null);
        }
    };

    return (
        <>
            <Navbar />
            <div className="container my-5">
                <div className="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-2">
                    <div>
                        <h4 className="fw-bold">{testTitle}</h4>
                        <p className="text-muted mb-0">
                            Submissions for subject: <strong>{subjectName}</strong>
                        </p>
                    </div>
                    <div className="d-flex gap-2">
                        <button
                            className="btn btn-outline-secondary"
                            onClick={() => navigate('/tests-for-subject', {
                                state: { subjectId, subjectName }
                            })}
                        >
                            <i className="bi bi-arrow-left me-1"></i> Back to Tests
                        </button>
                        <button className="btn btn-outline-primary" onClick={viewJsonReport}>
                            <i className="bi bi-bar-chart-fill me-1"></i> View Report
                        </button>
                        <button className="btn btn-outline-danger" onClick={openPdfReportDialog}>
                            <i className="bi bi-file-earmark-pdf me-1"></i> View PDF
                        </button>
                    </div>
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

            {/* PDF Modal */}
            {showModal && (
                <div className="modal d-block" tabIndex="-1" onClick={closeModal} style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog modal-xl" onClick={e => e.stopPropagation()}>
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Professor Report (PDF)</h5>
                                <button type="button" className="btn-close" onClick={closeModal}></button>
                            </div>
                            <div className="modal-body" style={{ height: '80vh' }}>
                                {pdfUrl && (
                                    <iframe
                                        src={pdfUrl}
                                        title="Professor Report PDF"
                                        width="100%"
                                        height="100%"
                                        frameBorder="0"
                                    />
                                )}
                            </div>
                            <div className="modal-footer">
                                <button className="btn btn-secondary" onClick={closeModal}>Close</button>
                                {pdfUrl && (
                                    <a href={pdfUrl} download="professor_report.pdf" className="btn btn-danger">
                                        <i className="bi bi-download me-1"></i>Download
                                    </a>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}

            <Footer />
        </>
    );
}

export default ReviewSubmissionsPage;