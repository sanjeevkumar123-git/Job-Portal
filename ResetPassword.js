// import React, { useState } from "react";
// import { Form, Button, Container, Card, Alert } from "react-bootstrap";
// import { useNavigate, useParams } from "react-router-dom";

// const ResetPassword = () => {
//   const [form, setForm] = useState({ otp: "", newPassword: "" });
//   const [message, setMessage] = useState("");
//   const { token } = useParams(); // if you want token via URL
//   const navigate = useNavigate();

//   const handleChange = (e) => {
//     setForm({ ...form, [e.target.name]: e.target.value });
//   };

//   const handleSubmit = (e) => {
//     e.preventDefault();
//     fetch("http://localhost:8084/api/users/reset-password", {
//       method: "POST",
//       headers: { "Content-Type": "application/json" },
//       body: JSON.stringify(form),
//     })
//       .then((res) => {
//         if (!res.ok)
//           return res.text().then((text) => {
//             throw new Error(text);
//           });
//         setMessage("Password reset successful! Login again.");
//         setTimeout(() => navigate("/login"), 2000);
//       })
//       .catch((err) => setMessage(err.message));
//   };

//   return (
//     <Container className="mt-5">
//       <Card className="p-4">
//         <h2>Reset Password</h2>
//         {message && <Alert variant="info">{message}</Alert>}
//         <Form onSubmit={handleSubmit}>
//           <Form.Group className="mb-3">
//             <Form.Label>OTP</Form.Label>
//             <Form.Control name="otp" onChange={handleChange} required />
//           </Form.Group>
//           <Form.Group className="mb-3">
//             <Form.Label>New Password</Form.Label>
//             <Form.Control
//               name="newPassword"
//               type="password"
//               onChange={handleChange}
//               required
//             />
//           </Form.Group>
//           <Button type="submit">Reset Password</Button>
//         </Form>
//       </Card>
//     </Container>
//   );
// };

// export default ResetPassword;

import React, { useState } from "react";
import { Form, Button, Container, Alert } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";

const RecruiterResetPassword = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || ""; // email passed from previous page

  const [otp, setOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(
        "http://localhost:8084/api/users/reset-password",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ otp, newPassword }),
        }
      );
      if (!res.ok) throw new Error(await res.text());
      const data = await res.text();
      setMessage(data);
      setError("");
      navigate("/login");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Container className="mt-5" style={{ maxWidth: "400px" }}>
      <h2>Reset Password</h2>
      <p>
        Reset password for: <strong>{email}</strong>
      </p>
      {message && <Alert variant="success">{message}</Alert>}
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>OTP</Form.Label>
          <Form.Control
            type="text"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            required
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>New Password</Form.Label>
          <Form.Control
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
        </Form.Group>
        <Button type="submit">Reset Password</Button>
      </Form>
    </Container>
  );
};

export default RecruiterResetPassword;
