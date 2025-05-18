import React, { useEffect, useRef, useState } from 'react';
import api from "../api/axios";
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { useLocation } from "react-router-dom";
import TestForm from "../components/TestInProgress/TestForm";

reportWebVitals();

function TestInProgress() {
    const testFormRef = useRef(null);
    const location = useLocation();
    const testId = location.state?.testId;
    const duration = location.state?.duration;
    const subjectName = location.state?.subjectName;
    const subjectId = location.state?.subjectId;
    const [testAttemptId, setTestAttemptId] = useState(null);
    const [questions, setQuestions] = useState([]);
    const [remainingTime, setRemainingTime] = useState(duration * 60);
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState('');

    function startTestAttempt () {
        api.post(`/api/test-attempts/start/${testId}`)
            .then(res => {
                setQuestions(res.data.questions);
                setTestAttemptId(res.data.id);
                setLoading(false);
            })
            .catch(err => {
                setMessage(err.response?.data?.message || "Something went wrong");
                setLoading(false);
            });
    }

    function formatTime(seconds) {
        const mins = Math.floor(seconds / 60);
        const secs = seconds % 60;
        return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
    }

    useEffect(() => {
        startTestAttempt();
    }, [testId]);


    useEffect(() => {
        const timer = setInterval(() => {
            setRemainingTime(prev => (prev > 0 ? prev - 1 : 0));
        }, 1000);
        return () => clearInterval(timer);
    }, []);

    useEffect(() => {
        if (remainingTime === 0 && testFormRef.current) {
            testFormRef.current.submitTest();
        }
    }, [remainingTime]);
    if (loading) return <p className="text-center mt-5">Loading...</p>;

    return (
        <>
            <Navbar />
            <div className="container my-5">
                {message ? (
                    <div className="alert alert-danger text-center" role="alert">
                        {message}
                    </div>
                ) : (
                    <div className="row">
                        <div className="col-md-3">
                            <div className="bg-light p-3 rounded">
                                <h5 className="text-danger">Time Remaining</h5>
                                <div className="fw-bold display-6">{formatTime(remainingTime)}</div>
                            </div>
                        </div>
                        <div className="col-md-9">
                            <TestForm
                                ref={testFormRef}
                                questions={questions}
                                testAttemptId={testAttemptId}
                                subjectName={subjectName}
                                subjectId={subjectId}
                            />
                        </div>
                    </div>
                )}
            </div>
            <Footer />
        </>
    );
}

export default TestInProgress;



