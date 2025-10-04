// import React, { useState } from "react";
// import { Form, Button, Container, Card, Alert } from "react-bootstrap";
// import { useNavigate } from "react-router-dom";

// const ForgotPassword = () => {
//   const [email, setEmail] = useState("");
//   const [message, setMessage] = useState("");
//   const navigate = useNavigate();

//   const handleSubmit = (e) => {
//     e.preventDefault();
//     fetch(`http://localhost:8084/api/users/forgot-password?email=${email}`, {
//       method: "POST",
//     })
//       .then((res) => {
//         if (!res.ok)
//           return res.text().then((text) => {
//             throw new Error(text);
//           });
//         setMessage("OTP sent to your email. Use it to reset password.");
//       })
//       .catch((err) => setMessage(err.message));
//   };

//   return (
//     <Container className="mt-5">
//       <Card className="p-4">
//         <h2>Forgot Password</h2>
//         {message && <Alert variant="info">{message}</Alert>}
//         <Form onSubmit={handleSubmit}>
//           <Form.Group className="mb-3">
//             <Form.Label>Email</Form.Label>
//             <Form.Control
//               type="email"
//               value={email}
//               onChange={(e) => setEmail(e.target.value)}
//               required
//             />
//           </Form.Group>
//           <Button type="submit">Send OTP</Button>
//         </Form>
//         <p className="mt-3">
//           Back to{" "}
//           <span
//             style={{ cursor: "pointer", color: "blue" }}
//             onClick={() => navigate("/login")}
//           >
//             Login
//           </span>
//         </p>
//       </Card>
//     </Container>
//   );
// };

// export default ForgotPassword;

import React, { useState } from "react";
import { Form, Button, Container, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(
        `http://localhost:8084/api/users/forgot-password?email=${email}`,
        { method: "POST" }
      );
      if (!res.ok) throw new Error(await res.text());
      const data = await res.text();
      setMessage(data);
      setError("");
      // navigate to reset password page, passing email via state
      navigate("/reset-password", { state: { email } });
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Container className="mt-5" style={{ maxWidth: "400px" }}>
      <h2>Forgot Password</h2>
      {message && <Alert variant="success">{message}</Alert>}
      {error && <Alert variant="danger">{error}</Alert>}
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </Form.Group>
        <Button type="submit">Send OTP</Button>
      </Form>
    </Container>
  );
};

export default ForgotPassword;
