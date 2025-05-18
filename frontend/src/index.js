// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)
import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom/client';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';
import Login from './pages/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import UpcomingTestsPage from "./pages/UpcomingTestsPage";
import TakenTestsPage from "./pages/TakenTestsPage";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import TestInProgress from "./pages/TestInProgress";
import AddSubject from "./pages/add-subject";
import Subject from "./pages/Subject";
import UserDashboard from './pages/UserDashboard';
import RegisterForm from "./pages/Register";

reportWebVitals();

function App() {
    const token = localStorage.getItem('token');
    return (
        <>
            <Navbar/>
            {token ? <UserDashboard /> : <RegisterForm />}
            <Footer/>
        </>
    );
}

const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/home" element={<App />} />
                <Route path="/login" element={<Login/>} />
                <Route path="/dashboard" element={<UserDashboard />} />
                <Route path="/course-details" element={<Subject/>} />
                <Route path="/tests-for-subject" element={<UpcomingTestsPage/>} />
                <Route path="/take-test" element={<TestInProgress/>} />
                <Route path="/taken-tests" element={<TakenTestsPage/>} />
                <Route path="/add-subject" element={<AddSubject/>} />
                {/* Add other routes here, e.g. Home */}
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);
export default App;


