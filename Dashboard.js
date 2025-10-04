import React, { useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Tabs,
  Tab,
  Form,
  Badge,
  ListGroup,
  Alert,
  Dropdown,
} from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const token = localStorage.getItem("userToken");
  const username = localStorage.getItem("userUsername");
  const userId = localStorage.getItem("userId");

  const headers = { Authorization: `Bearer ${token}` };
  const navigate = useNavigate();

  const [jobs, setJobs] = useState([]);
  const [appliedJobs, setAppliedJobs] = useState([]);
  const [savedJobs, setSavedJobs] = useState([]);
  const [alert, setAlert] = useState("");
  const [selectedResumes, setSelectedResumes] = useState({});
  const [searchTerm, setSearchTerm] = useState("");
  const [notifications, setNotifications] = useState([]);
  const [activeTab, setActiveTab] = useState("all");

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }
    fetchJobs();
    fetchAppliedJobs();
    fetchSavedJobs();
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8086/api/job-application/notifications/user/${userId}`,
        { headers }
      );
      setNotifications(res.data || []);
    } catch (err) {
      console.error("Error fetching notifications:", err);
      setNotifications([]);
    }
  };

  const fetchJobs = async () => {
    try {
      const res = await axios.get("http://localhost:8086/api/jobs/all", {
        headers,
      });
      setJobs(res.data || []);
    } catch (err) {
      console.error("Error fetching jobs:", err);
    }
  };

  const fetchAppliedJobs = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8086/api/job-application/applied/${userId}`,
        { headers }
      );
      setAppliedJobs(res.data || []);
    } catch (err) {
      console.error("Error fetching applied jobs:", err);
    }
  };

  const fetchSavedJobs = async () => {
    try {
      const res = await axios.get(
        `http://localhost:8086/api/job-application/saved/${userId}`,
        { headers }
      );
      setSavedJobs(res.data || []);
    } catch (err) {
      console.error("Error fetching saved jobs:", err);
    }
  };

  const isApplied = (jobId) =>
    appliedJobs.some((job) => job.jobId === jobId || job.job?.id === jobId);

  const isSaved = (jobId) =>
    savedJobs.some((job) => job.jobId === jobId || job.job?.id === jobId);

  const handleResumeChange = (jobId, file) => {
    setSelectedResumes((prev) => ({ ...prev, [jobId]: file }));
  };

  const handleApply = async (job) => {
    const resumeFile = selectedResumes[job.id];
    if (!resumeFile) {
      setAlert("Please upload your resume before applying");
      return;
    }

    const formData = new FormData();
    formData.append("userId", userId);
    formData.append("jobId", job.id);
    formData.append("jobTitle", job.title);
    formData.append("jobDescription", job.description);
    formData.append("resume", resumeFile);

    try {
      await axios.post(
        "http://localhost:8086/api/job-application/apply",
        formData,
        { headers: { ...headers, "Content-Type": "multipart/form-data" } }
      );
      setAlert(`Applied to "${job.title}" successfully!`);
      setAppliedJobs((prev) => [
        ...prev,
        {
          jobId: job.id,
          appliedDate: new Date().toISOString().split("T")[0],
          status: "APPLIED",
        },
      ]);
    } catch (err) {
      console.error("Error applying to job:", err);
      setAlert(err.response?.data || "Failed to apply");
    }
  };

  const handleSave = async (job) => {
    try {
      await axios.post(
        "http://localhost:8086/api/job-application/save",
        {},
        { headers, params: { userId, jobId: job.id } }
      );
      setAlert(`Saved "${job.title}" successfully!`);
      setSavedJobs((prev) => [
        ...prev,
        { jobId: job.id, savedDate: new Date().toISOString().split("T")[0] },
      ]);
    } catch (err) {
      console.error("Error saving job:", err);
      setAlert(err.response?.data || "Failed to save");
    }
  };

  const filteredJobs = jobs.filter(
    (job) =>
      job.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      job.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      job.location.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container fluid className="mt-4">
      <Row>
        {/* Sidebar */}
        <Col md={2} className="bg-light vh-100 p-3 shadow-sm">
          <h4 className="text-primary fw-bold mb-4">Dashboard</h4>
          <ListGroup variant="flush">
            <ListGroup.Item
              action
              active={activeTab === "all"}
              onClick={() => setActiveTab("all")}
            >
              All Jobs
            </ListGroup.Item>
            <ListGroup.Item
              action
              active={activeTab === "applied"}
              onClick={() => setActiveTab("applied")}
            >
              Applied Jobs
              <Badge bg="success" className="ms-2">
                {appliedJobs.length}
              </Badge>
            </ListGroup.Item>
            <ListGroup.Item
              action
              active={activeTab === "saved"}
              onClick={() => setActiveTab("saved")}
            >
              Saved Jobs
              <Badge bg="warning" className="ms-2 text-dark">
                {savedJobs.length}
              </Badge>
            </ListGroup.Item>
          </ListGroup>
        </Col>

        {/* Main Content */}
        <Col md={10}>
          {/* Top Row: Welcome + Notifications */}
          <Row className="mb-4 align-items-center">
            <Col>
              <h2 className="text-primary fw-bold">Welcome, {username}</h2>
              <p className="text-muted">Your job dashboard at a glance</p>
            </Col>
            <Col className="text-end d-flex justify-content-end align-items-center">
              <Dropdown align="end" className="me-3">
                <Dropdown.Toggle variant="light" id="dropdown-notifications">
                  Notifications{" "}
                  {notifications.length > 0 && (
                    <Badge bg="danger">{notifications.length}</Badge>
                  )}
                </Dropdown.Toggle>
                <Dropdown.Menu style={{ minWidth: "300px" }}>
                  {notifications.length === 0 && (
                    <Dropdown.Item>No notifications</Dropdown.Item>
                  )}
                  {notifications.map((n) => (
                    <Dropdown.Item key={n.id}>
                      <strong>{n.title}</strong>
                      <br />
                      <small className="text-muted">{n.message}</small>
                    </Dropdown.Item>
                  ))}
                </Dropdown.Menu>
              </Dropdown>
              <Button
                variant="danger"
                onClick={() => {
                  localStorage.clear();
                  navigate("/login");
                }}
              >
                Logout
              </Button>
            </Col>
          </Row>

          {/* Alert */}
          {alert && (
            <Alert variant="success" onClose={() => setAlert("")} dismissible>
              {alert}
            </Alert>
          )}

          {/* Stats Cards */}
          <Row className="mb-4">
            <Col md={3}>
              <Card className="shadow-sm border-0 rounded-3 text-center p-3">
                <h5 className="fw-bold">Total Jobs</h5>
                <p className="display-6 text-primary">{jobs.length}</p>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="shadow-sm border-0 rounded-3 text-center p-3">
                <h5 className="fw-bold">Applied Jobs</h5>
                <p className="display-6 text-success">{appliedJobs.length}</p>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="shadow-sm border-0 rounded-3 text-center p-3">
                <h5 className="fw-bold">Saved Jobs</h5>
                <p className="display-6 text-warning">{savedJobs.length}</p>
              </Card>
            </Col>
            <Col md={3}>
              <Card className="shadow-sm border-0 rounded-3 text-center p-3">
                <h5 className="fw-bold">Notifications</h5>
                <p className="display-6 text-danger">{notifications.length}</p>
              </Card>
            </Col>
          </Row>

          {/* Tabs Content */}
          <Row className="mb-3">
            <Col md={6}>
              <Form.Control
                type="text"
                placeholder="Search jobs by title, description, or location..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </Col>
          </Row>

          <Row>
            {activeTab === "all" &&
              (filteredJobs.length === 0 ? (
                <p>No jobs found</p>
              ) : (
                filteredJobs.map((job) => (
                  <Col md={4} key={job.id} className="mb-4">
                    <Card className="shadow-sm h-100 border-0 rounded-3">
                      <Card.Body className="d-flex flex-column">
                        <Card.Title className="fw-bold">{job.title}</Card.Title>
                        <Card.Text className="flex-grow-1 text-secondary">
                          {job.description}
                        </Card.Text>
                        <Card.Text className="mb-1">
                          <strong>Location:</strong> {job.location}
                        </Card.Text>
                        <Card.Text className="mb-1">
                          <strong>Type:</strong> {job.type}
                        </Card.Text>
                        <Card.Text className="mb-2">
                          <strong>Expiry:</strong> {job.expiryDate}
                        </Card.Text>

                        {!isApplied(job.id) && (
                          <Form.Group className="mb-2">
                            <Form.Label>Upload Resume</Form.Label>
                            <Form.Control
                              type="file"
                              accept=".pdf,.doc,.docx"
                              onChange={(e) =>
                                handleResumeChange(job.id, e.target.files[0])
                              }
                            />
                          </Form.Group>
                        )}

                        <div className="mt-auto d-flex justify-content-between">
                          <Button
                            variant="primary"
                            className="me-2"
                            disabled={isApplied(job.id)}
                            onClick={() => handleApply(job)}
                          >
                            {isApplied(job.id) ? "Applied" : "Apply"}
                          </Button>
                          <Button
                            variant="outline-secondary"
                            disabled={isSaved(job.id)}
                            onClick={() => handleSave(job)}
                          >
                            {isSaved(job.id) ? "Saved" : "Save"}
                          </Button>
                        </div>
                      </Card.Body>
                    </Card>
                  </Col>
                ))
              ))}

            {activeTab === "applied" &&
              (appliedJobs.length === 0 ? (
                <p>No applied jobs</p>
              ) : (
                appliedJobs.map((a) => {
                  const job = a.job || {};
                  return (
                    <Col md={4} key={a.id || a.jobId}>
                      <Card className="mb-3 shadow-sm border-0 rounded-3 bg-success text-white">
                        <Card.Body>
                          <Card.Title className="fw-bold">
                            {job.title || `Job #${a.jobId}`}
                          </Card.Title>
                          <Card.Text>
                            {job.description || "No description available"}
                          </Card.Text>
                          <Card.Text>
                            <strong>Applied on:</strong> {a.appliedDate}
                          </Card.Text>
                          <Card.Text>
                            <strong>Status:</strong> {a.status}
                          </Card.Text>
                        </Card.Body>
                      </Card>
                    </Col>
                  );
                })
              ))}

            {activeTab === "saved" &&
              (savedJobs.length === 0 ? (
                <p>No saved jobs</p>
              ) : (
                savedJobs.map((s) => {
                  const job = s.job || {};
                  return (
                    <Col md={4} key={s.id || s.jobId}>
                      <Card className="mb-3 shadow-sm border-0 rounded-3 bg-warning text-dark">
                        <Card.Body>
                          <Card.Title className="fw-bold">
                            {job.title || `Job #${s.jobId}`}
                          </Card.Title>
                          <Card.Text>
                            {job.description || "No description available"}
                          </Card.Text>
                          <Card.Text>
                            <strong>Saved on:</strong> {s.savedDate}
                          </Card.Text>
                        </Card.Body>
                      </Card>
                    </Col>
                  );
                })
              ))}
          </Row>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;
