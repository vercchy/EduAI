import React, { useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import UpcomingTests from "../components/UpcomingTests/UpcomingTests";
import Auth from "../utils/auth";
import api from "../api/axios";

function UpcomingTestsContainer() {
    const location = useLocation();
    const subjectId = location.state?.subjectId;
    const subjectName = location.state?.subjectName;
    const isProfessor = Auth.isProfessor();
    const basePath = '/api/tests';
    const [testData, setTestData] = useState([]);
    const [message, setMessage] = useState("");

    const fetchTestsForSubject = () => {
        const endpointPath = basePath.concat(isProfessor
            ? `/subject/${subjectId}`
            : `/available-tests/subject/${subjectId}`);
        api.get(endpointPath)
            .then(response => {
                setTestData(response.data);
            })
            .catch(error => {
                setMessage(error.response.data.message);
            });
    };

    useEffect(() => {
        fetchTestsForSubject();
        }, []);

    return <UpcomingTests isProfessor={isProfessor} subjectName={subjectName} message={message} testData={testData} />;
}

export default UpcomingTestsContainer;