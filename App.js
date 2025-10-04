import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Landing from "./Landing";

// Jobseeker
import Register from "./Register";
import Login from "./Login";
import Dashboard from "./Dashboard";

// Recruiter
import RecruiterRegister from "./RecruiterRegister";
import RecruiterLogin from "./RecruiterLogin";
import RecruiterDashboard from "./RecruiterDashboard";
import RecruiterForgotPassword from "./RecruiterForgotPassword";
import RecruiterResetPassword from "./RecruiterResetPassword";
import ForgotPassword from "./ForgotPassword";
import ResetPassword from "./ResetPassword";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Landing />} />

        {/* Jobseeker Routes */}
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />

        {/* Recruiter Routes */}
        <Route path="/recruiter-register" element={<RecruiterRegister />} />
        <Route path="/recruiter-login" element={<RecruiterLogin />} />
        <Route path="/recruiter-dashboard" element={<RecruiterDashboard />} />
        <Route
          path="/recruiter-forgot-password"
          element={<RecruiterForgotPassword />}
        />
        <Route
          path="/recruiter-reset-password"
          element={<RecruiterResetPassword />}
        />
      </Routes>
    </Router>
  );
}

export default App;
