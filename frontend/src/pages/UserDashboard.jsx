import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import { useNavigate } from 'react-router-dom';

import {
    Box,
    Card,
    CardContent,
    Typography,
    List,
    ListItem,
    ListItemText,
    Grid,
} from '@mui/material';

import {
    LineChart,
    Line,
    CartesianGrid,
    XAxis,
    YAxis,
    Tooltip,
    ResponsiveContainer,
} from 'recharts';

function UserDashboard() {
    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [role, setRole] = useState(null);
    const [professorReport, setProfessorReport] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const userRole = localStorage.getItem('role');
        setRole(userRole);

        api
            .get('/api/subjects')
            .then(async (response) => {
                setSubjects(response.data);
                setLoading(false);

                if (userRole === 'ROLE_PROFESSOR' && response.data.length > 0) {
                    const subjectId = response.data[0].id;

                    if (subjectId) {
                        try {
                            const reportRes = await api.get(`/professor-report/${subjectId}`);
                            setProfessorReport(reportRes.data);
                        } catch (e) {
                            console.error('Failed to fetch professor report:', e);
                        }
                    }
                }
            })
            .catch((error) => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data ||
                    error.message ||
                    'Unknown error occurred';
                setError(`Failed to load subjects: ${message}`);
                setLoading(false);
            });
    }, []);

    const headingText = role === 'ROLE_PROFESSOR' ? 'Created Subjects' : 'Enrolled Subjects';

    return (
        <>
            <div className="container-fluid">
                <div className="d-flex justify-content-between align-items-center px-4 pt-4 mb-4">
                    <h2 className="h2 mb-0">{headingText}</h2>
                    {role === 'ROLE_PROFESSOR' && (
                        <button
                            className="btn btn-outline-custom btn-sm"
                            onClick={() => navigate('/add-subject')}
                        >
                            <i className="bi bi-plus-circle me-1"></i> Create Subject
                        </button>
                    )}
                </div>

                <div className="container py-4">
                    {role === 'ROLE_PROFESSOR' && professorReport && (
                        <Box mb={5}>
                            <Typography variant="h5" color="primary" gutterBottom>
                                Professor Summary Report for the Latest Test
                            </Typography>
                            {professorReport.scoreTrend?.length > 0 && (
                                <Box mb={4}>
                                    <Card variant="outlined" sx={{ borderRadius: 2, height: 300 }}>
                                        <CardContent>
                                            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
                                                Score Trend
                                            </Typography>
                                            <ResponsiveContainer width="100%" height="85%">
                                                <LineChart data={professorReport.scoreTrend}>
                                                    <CartesianGrid stroke="#ccc" strokeDasharray="5 5" />
                                                    <XAxis dataKey="date" />
                                                    <YAxis domain={[0, 100]} />
                                                    <Tooltip />
                                                    <Line
                                                        type="monotone"
                                                        dataKey="averageScore"
                                                        stroke="#3f51b5"
                                                        strokeWidth={3}
                                                        dot={{ r: 5 }}
                                                        activeDot={{ r: 7 }}
                                                    />
                                                </LineChart>
                                            </ResponsiveContainer>
                                        </CardContent>
                                    </Card>
                                </Box>
                            )}

                            <Grid container spacing={3}>
                                <Grid item xs={12} sm={4}>
                                    <Card variant="outlined" sx={{ borderRadius: 2, height: '100%' }}>
                                        <CardContent>
                                            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
                                                Average Correctness
                                            </Typography>
                                            <Typography variant="h6" color="primary">
                                                {professorReport.averageCorrectPercent}%
                                            </Typography>
                                        </CardContent>
                                    </Card>
                                </Grid>

                                <Grid item xs={12} sm={4}>
                                    <Card variant="outlined" sx={{ borderRadius: 2, height: '100%' }}>
                                        <CardContent>
                                            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
                                                Hardest Question
                                            </Typography>
                                            <Typography variant="body1">
                                                {professorReport.hardestQuestion?.text || 'N/A'}
                                            </Typography>
                                        </CardContent>
                                    </Card>
                                </Grid>

                                <Grid item xs={12} sm={4}>
                                    <Card
                                        variant="outlined"
                                        sx={{ borderRadius: 2, height: '100%', display: 'flex', flexDirection: 'column' }}
                                    >
                                        <CardContent sx={{ flexGrow: 1, overflowY: 'auto' }}>
                                            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
                                                Weak Areas
                                            </Typography>
                                            {professorReport.weakAreas.length > 0 ? (
                                                <List dense>
                                                    {professorReport.weakAreas.map((area, index) => (
                                                        <ListItem key={index} disablePadding>
                                                            <ListItemText
                                                                primary={`${area.questionText} - ${area.averageScore}%`}
                                                            />
                                                        </ListItem>
                                                    ))}
                                                </List>
                                            ) : (
                                                <Typography variant="body2" color="textSecondary">
                                                    None
                                                </Typography>
                                            )}
                                        </CardContent>
                                    </Card>
                                </Grid>
                            </Grid>
                        </Box>
                    )}

                    {loading && <div>Loading...</div>}
                    {error && <div className="text-danger">{error}</div>}

                    <div className="row gy-4">
                        {subjects.map((subject) => (
                            <div key={subject.id} className="col-md-6 col-lg-4">
                                <div className="card h-100 shadow-sm border-0 rounded">
                                    <div className="card-body d-flex flex-column justify-content-between">
                                        <div>
                                            <h5 className="card-title" style={{ color: '#FF9500' }}>{subject.name}</h5>
                                            <h6 className="card-subtitle mb-2 text-muted">
                                                By {subject.professorFullName}
                                            </h6>
                                            <p className="card-text">{subject.description}</p>
                                        </div>
                                        <div className="mt-3 d-flex flex-wrap gap-2">
                                            <button
                                                className="btn btn-outline-custom"
                                                onClick={() =>
                                                    navigate('/course-details', {
                                                        state: { subjectId: subject.id },
                                                    })
                                                }
                                            >
                                                View Course
                                            </button>
                                            <button
                                                className="btn btn-outline-secondary"
                                                onClick={() =>
                                                    navigate('/tests-for-subject', {
                                                        state: { subjectId: subject.id, subjectName: subject.name },
                                                    })
                                                }
                                            >
                                                View Tests
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div> {/* 👈 closes .container py-4 */}
            </div> {/* 👈 closes .container-fluid */}
        </>
    );
}

export default UserDashboard;
