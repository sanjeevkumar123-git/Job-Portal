import React, { useState } from "react";
import { Form, Button, Container, Alert } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";

const VerifyOtp = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || "";
  const [otp, setOtp] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(
        `http://localhost:8084/api/users/verify-otp?email=${email}&otp=${otp}`,
        {
          method: "POST",
        }
      );
      if (!res.ok) throw new Error("OTP verification failed");
      navigate("/login");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Container className="mt-5" style={{ maxWidth: "400px" }}>
      <h2>Verify OTP</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Enter OTP sent to {email}</Form.Label>
          <Form.Control
            type="text"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
          />
        </Form.Group>
        <Button type="submit">Verify</Button>
      </Form>
    </Container>
  );
};

export default VerifyOtp;
