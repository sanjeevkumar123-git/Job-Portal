import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { BsBriefcaseFill, BsPersonFill } from "react-icons/bs"; // ✅ Using react-icons

const Landing = () => {
  const navigate = useNavigate();

  return (
    <div
      style={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #f8f9fa, #e9ecef)",
        display: "flex",
        alignItems: "center",
      }}
    >
      <Container>
        <h1 className="text-center fw-bold mb-5" style={{ color: "#343a40" }}>
          🚀 Welcome to <span style={{ color: "#0d6efd" }}>Job Portal</span>
        </h1>

        <Row className="justify-content-center g-4">
          {/* Jobseeker Section */}
          <Col md={5}>
            <Card
              className="p-4 shadow-lg text-center border-0 h-100"
              style={{ borderRadius: "20px" }}
            >
              <BsPersonFill size={50} color="#0d6efd" className="mb-3" />
              <h3 className="fw-semibold mb-3" style={{ color: "#0d6efd" }}>
                Jobseeker
              </h3>
              <p className="text-muted mb-4">
                Find your dream job, apply easily, and track applications in
                real-time.
              </p>
              <div>
                <Button
                  variant="primary"
                  size="lg"
                  className="me-2 px-4"
                  onClick={() => navigate("/register")}
                >
                  Sign Up
                </Button>
                <Button
                  variant="outline-primary"
                  size="lg"
                  className="px-4"
                  onClick={() => navigate("/login")}
                >
                  Login
                </Button>
              </div>
            </Card>
          </Col>

          {/* Recruiter Section */}
          <Col md={5}>
            <Card
              className="p-4 shadow-lg text-center border-0 h-100"
              style={{ borderRadius: "20px" }}
            >
              <BsBriefcaseFill size={50} color="#198754" className="mb-3" />
              <h3 className="fw-semibold mb-3" style={{ color: "#198754" }}>
                Recruiter
              </h3>
              <p className="text-muted mb-4">
                Post jobs, manage applications, and hire the right talent with
                ease.
              </p>
              <div>
                <Button
                  variant="success"
                  size="lg"
                  className="me-2 px-4"
                  onClick={() => navigate("/recruiter-register")}
                >
                  Sign Up
                </Button>
                <Button
                  variant="outline-success"
                  size="lg"
                  className="px-4"
                  onClick={() => navigate("/recruiter-login")}
                >
                  Login
                </Button>
              </div>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Landing;
