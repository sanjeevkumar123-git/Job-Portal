// import React, { useState, useEffect } from "react";
// import {
//   Container,
//   Row,
//   Col,
//   Navbar,
//   Nav,
//   Button,
//   Table,
//   Modal,
//   Form,
//   Card,
//   Alert,
// } from "react-bootstrap";
// import axios from "axios";

// const RecruiterDashboard = () => {
//   const token = localStorage.getItem("recruiterToken");
//   const headers = { Authorization: `Bearer ${token}` };

//   const [recruiter, setRecruiter] = useState(null);
//   const [jobs, setJobs] = useState([]);
//   const [expiredJobs, setExpiredJobs] = useState([]);
//   const [showModal, setShowModal] = useState(false);
//   const [editJob, setEditJob] = useState(null);
//   const [formData, setFormData] = useState({
//     title: "",
//     description: "",
//     location: "",
//     type: "",
//     postedDate: "",
//     expiryDate: "",
//     active: true,
//   });
//   const [alert, setAlert] = useState("");
//   const [applicants, setApplicants] = useState({}); // store applicants per job

//   // Fetch recruiter details
//   const fetchRecruiterDetails = async () => {
//     try {
//       const res = await axios.get(
//         "http://localhost:8085/api/recruiter/auth/details",
//         { headers }
//       );
//       setRecruiter(res.data);
//     } catch (err) {
//       console.error("Failed to fetch recruiter details:", err);
//     }
//   };

//   // Fetch jobs
//   const fetchJobs = async () => {
//     if (!recruiter) return;
//     try {
//       const res = await axios.get(
//         `http://localhost:8086/api/jobs/recruiter/${recruiter.id}?page=0&size=50&sortBy=postedDate`,
//         { headers }
//       );
//       const jobList = Array.isArray(res.data)
//         ? res.data
//         : res.data.content || [];
//       setJobs(jobList);
//       fetchAllApplicants(jobList);
//     } catch (err) {
//       console.error("Failed to fetch jobs:", err);
//       setJobs([]);
//     }
//   };

//   const fetchExpiredJobs = async () => {
//     if (!recruiter) return;
//     try {
//       const res = await axios.get(
//         `http://localhost:8086/api/jobs/expired/${recruiter.id}`,
//         { headers }
//       );
//       setExpiredJobs(Array.isArray(res.data) ? res.data : []);
//     } catch (err) {
//       console.error("Failed to fetch expired jobs:", err);
//       setExpiredJobs([]);
//     }
//   };

//   const fetchAllApplicants = async (jobList) => {
//     const tempApplicants = {};
//     for (let i = 0; i < jobList.length; i++) {
//       const job = jobList[i];
//       try {
//         const res = await axios.get(
//           `http://localhost:8086/api/job-application/job/${job.id}/applicants`,
//           { headers }
//         );
//         tempApplicants[job.id] = Array.isArray(res.data) ? res.data : [];
//       } catch (err) {
//         console.error(`Failed to fetch applicants for job ${job.id}:`, err);
//         tempApplicants[job.id] = [];
//       }
//     }
//     setApplicants(tempApplicants);
//   };

//   useEffect(() => {
//     fetchRecruiterDetails();
//   }, []);

//   useEffect(() => {
//     if (recruiter) {
//       fetchJobs();
//       fetchExpiredJobs();
//     }
//   }, [recruiter]);

//   const handleChange = (e) => {
//     const value =
//       e.target.type === "checkbox" ? e.target.checked : e.target.value;
//     setFormData({ ...formData, [e.target.name]: value });
//   };

//   const handleEdit = (job) => {
//     setEditJob(job);
//     setFormData({
//       title: job.title,
//       description: job.description,
//       location: job.location,
//       type: job.type,
//       postedDate: job.postedDate,
//       expiryDate: job.expiryDate,
//       active: job.active,
//     });
//     setShowModal(true);
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       if (!recruiter) return;
//       if (editJob) {
//         await axios.put(
//           `http://localhost:8086/api/jobs/${editJob.id}`,
//           formData,
//           { headers }
//         );
//         setAlert("Job updated successfully!");
//       } else {
//         await axios.post(
//           "http://localhost:8086/api/jobs/post",
//           {
//             title: formData.title,
//             description: formData.description,
//             location: formData.location,
//             type: formData.type,
//             postedDate: formData.postedDate,
//             expiryDate: formData.expiryDate,
//             active: formData.active,
//             recruiterId: recruiter.id,
//           },
//           { headers }
//         );
//         setAlert("Job posted successfully!");
//       }
//       setShowModal(false);
//       setEditJob(null);
//       setFormData({
//         title: "",
//         description: "",
//         location: "",
//         type: "",
//         postedDate: "",
//         expiryDate: "",
//         active: true,
//       });
//       fetchJobs();
//       fetchExpiredJobs();
//     } catch (err) {
//       console.error("Failed to save job:", err);
//       setAlert("Failed to save job");
//     }
//   };

//   const handleDelete = async (id) => {
//     if (!window.confirm("Are you sure you want to delete this job?")) return;
//     try {
//       await axios.delete(`http://localhost:8086/api/jobs/${id}`, { headers });
//       setAlert("Job deleted successfully!");
//       fetchJobs();
//       fetchExpiredJobs();
//     } catch (err) {
//       console.error("Failed to delete job:", err);
//       setAlert("Failed to delete job");
//     }
//   };

//   // Download resume
//   const handleDownload = async (app) => {
//     if (!app.applicationId) return window.alert("Invalid application ID");
//     try {
//       const res = await axios.get(
//         `http://localhost:8086/api/job-application/download-resume/${app.applicationId}`,
//         { headers, responseType: "blob" }
//       );
//       const url = window.URL.createObjectURL(new Blob([res.data]));
//       const link = document.createElement("a");
//       link.href = url;
//       link.setAttribute("download", app.resumeFileName || "resume.pdf");
//       document.body.appendChild(link);
//       link.click();
//       link.remove();
//     } catch (err) {
//       console.error("Failed to download resume:", err);
//       window.alert("Failed to download resume");
//     }
//   };

//   // Shortlist applicant
//   const handleShortlist = async (app) => {
//     try {
//       await axios.post(
//         `http://localhost:8086/api/job-application/shortlist`,
//         null,
//         { headers, params: { appliedJobId: app.applicationId } }
//       );
//       setAlert(`Shortlisted ${app.firstName} ${app.lastName}`);
//       fetchAllApplicants(jobs);
//     } catch (err) {
//       console.error("Shortlist error:", err);
//       setAlert("Failed to shortlist applicant");
//     }
//   };

//   // Select applicant
//   const handleSelect = async (app) => {
//     try {
//       await axios.post(
//         `http://localhost:8086/api/job-application/select`,
//         null,
//         { headers, params: { appliedJobId: app.applicationId } }
//       );
//       setAlert(`Selected ${app.firstName} ${app.lastName}`);
//       fetchAllApplicants(jobs);
//     } catch (err) {
//       console.error("Select error:", err);
//       setAlert("Failed to select applicant");
//     }
//   };

//   // Schedule interview
//   const handleSchedule = async (app) => {
//     try {
//       // Ask recruiter for interview date & time
//       const interviewDateTime = prompt(
//         "Enter interview date and time (YYYY-MM-DDTHH:MM)"
//       );
//       if (!interviewDateTime) return;

//       // Ask recruiter for mode (ONLINE/OFFLINE)
//       const mode = prompt("Enter mode (ONLINE/OFFLINE)", "ONLINE");

//       let location = null;
//       let meetingLink = null;

//       if (mode?.toUpperCase() === "OFFLINE") {
//         location = prompt("Enter interview location");
//       } else if (mode?.toUpperCase() === "ONLINE") {
//         meetingLink = prompt("Enter meeting link (Zoom/Google Meet)");
//       }

//       await axios.post(
//         `http://localhost:8086/api/job-application/schedule-interview`,
//         null,
//         {
//           headers,
//           params: {
//             appliedJobId: app.applicationId,
//             interviewDateTime,
//             mode,
//             location,
//             meetingLink,
//           },
//         }
//       );

//       setAlert(`Interview scheduled for ${app.firstName} ${app.lastName}`);
//       fetchAllApplicants(jobs); // refresh applicants list
//     } catch (err) {
//       console.error("Schedule interview error:", err);
//       setAlert("Failed to schedule interview");
//     }
//   };

//   // Reject applicant
//   const handleReject = async (app) => {
//     const reason = prompt("Enter reason for rejection");
//     if (!reason) return;
//     try {
//       await axios.post(
//         `http://localhost:8086/api/job-application/reject`,
//         null,
//         { headers, params: { appliedJobId: app.applicationId, reason } }
//       );
//       setAlert(`Rejected ${app.firstName} ${app.lastName}`);
//       fetchAllApplicants(jobs);
//     } catch (err) {
//       console.error("Reject error:", err);
//       setAlert("Failed to reject applicant");
//     }
//   };

//   return (
//     <>
//       <Navbar bg="dark" variant="dark" expand="lg">
//         <Container>
//           <Navbar.Brand>Recruiter Dashboard</Navbar.Brand>
//           <Nav className="ml-auto">
//             {recruiter && (
//               <Nav.Link disabled>Welcome, {recruiter.username}</Nav.Link>
//             )}
//             <Button
//               variant="outline-light"
//               onClick={() => {
//                 localStorage.removeItem("recruiterToken");
//                 window.location.href = "/recruiter-login";
//               }}
//             >
//               Logout
//             </Button>
//           </Nav>
//         </Container>
//       </Navbar>

//       <Container className="mt-4">
//         {alert && (
//           <Alert variant="success" onClose={() => setAlert("")} dismissible>
//             {alert}
//           </Alert>
//         )}

//         {/* Stats */}
//         <Row className="mb-4">
//           <Col md={4}>
//             <Card bg="primary" text="white">
//               <Card.Body>
//                 <Card.Title>Total Jobs</Card.Title>
//                 <Card.Text>{jobs.length + expiredJobs.length}</Card.Text>
//               </Card.Body>
//             </Card>
//           </Col>
//           <Col md={4}>
//             <Card bg="success" text="white">
//               <Card.Body>
//                 <Card.Title>Active Jobs</Card.Title>
//                 <Card.Text>{jobs.length}</Card.Text>
//               </Card.Body>
//             </Card>
//           </Col>
//           <Col md={4}>
//             <Card bg="danger" text="white">
//               <Card.Body>
//                 <Card.Title>Expired Jobs</Card.Title>
//                 <Card.Text>{expiredJobs.length}</Card.Text>
//               </Card.Body>
//             </Card>
//           </Col>
//         </Row>

//         {/* Job Table */}
//         <h4>Active Jobs</h4>
//         {jobs.length === 0 ? (
//           <p>No jobs posted.</p>
//         ) : (
//           <Table striped bordered hover responsive>
//             <thead>
//               <tr>
//                 <th>Title</th>
//                 <th>Description</th>
//                 <th>Type</th>
//                 <th>Location</th>
//                 <th>Posted Date</th>
//                 <th>Expiry Date</th>
//                 <th>Active</th>
//                 <th>Actions</th>
//               </tr>
//             </thead>
//             <tbody>
//               {jobs.map((job) => (
//                 <tr key={job.id}>
//                   <td>{job.title}</td>
//                   <td>{job.description}</td>
//                   <td>{job.type}</td>
//                   <td>{job.location}</td>
//                   <td>{job.postedDate}</td>
//                   <td>{job.expiryDate}</td>
//                   <td>{job.active ? "Yes" : "No"}</td>
//                   <td>
//                     <Button
//                       size="sm"
//                       variant="info"
//                       onClick={() => handleEdit(job)}
//                     >
//                       Edit
//                     </Button>{" "}
//                     <Button
//                       size="sm"
//                       variant="danger"
//                       onClick={() => handleDelete(job.id)}
//                     >
//                       Delete
//                     </Button>
//                   </td>
//                 </tr>
//               ))}
//             </tbody>
//           </Table>
//         )}

//         {/* Applicants */}
//         <h4 className="mt-5">Applicants</h4>
//         {jobs.map((job) => (
//           <div key={job.id} className="mb-4">
//             <h5>{job.title}</h5>
//             {!applicants[job.id] || applicants[job.id].length === 0 ? (
//               <p>No applicants yet.</p>
//             ) : (
//               <Table striped bordered hover responsive>
//                 <thead>
//                   <tr>
//                     <th>Name</th>
//                     <th>Email</th>
//                     <th>Applied Date</th>
//                     <th>Resume</th>
//                     <th>Status</th>
//                     <th>Actions</th>
//                   </tr>
//                 </thead>
//                 <tbody>
//                   {applicants[job.id].map((app) => (
//                     <tr key={app.applicationId}>
//                       <td>
//                         {app.firstName} {app.lastName}
//                       </td>
//                       <td>{app.email}</td>
//                       <td>{app.appliedDate}</td>
//                       <td>
//                         {app.resumeFileName ? (
//                           <Button
//                             variant="primary"
//                             size="sm"
//                             onClick={() => handleDownload(app)}
//                           >
//                             Download
//                           </Button>
//                         ) : (
//                           "No Resume"
//                         )}
//                       </td>
//                       <td>
//                         {/* Shortlist button */}
//                         {app.status === "SHORTLISTED" ? (
//                           <Button size="sm" variant="secondary" disabled>
//                             Shortlisted
//                           </Button>
//                         ) : (
//                           <Button
//                             size="sm"
//                             variant="warning"
//                             onClick={() => handleShortlist(app)}
//                           >
//                             Shortlist
//                           </Button>
//                         )}{" "}
//                         {/* Select button */}
//                         <Button
//                           size="sm"
//                           variant="success"
//                           onClick={() => handleSelect(app)}
//                           disabled={app.status === "SELECTED"}
//                         >
//                           {app.status === "SELECTED" ? "Selected" : "Select"}
//                         </Button>{" "}
//                         {/* Schedule interview button */}
//                         <Button
//                           size="sm"
//                           variant="info"
//                           onClick={() => handleSchedule(app)}
//                         >
//                           Schedule
//                         </Button>{" "}
//                         {/* Reject button */}
//                         <Button
//                           size="sm"
//                           variant="danger"
//                           onClick={() => handleReject(app)}
//                         >
//                           Reject
//                         </Button>
//                       </td>
//                     </tr>
//                   ))}
//                 </tbody>
//               </Table>
//             )}
//           </div>
//         ))}
//       </Container>

//       {/* Modal for Post/Edit Job */}
//       <Modal show={showModal} onHide={() => setShowModal(false)}>
//         <Modal.Header closeButton>
//           <Modal.Title>{editJob ? "Edit Job" : "Post New Job"}</Modal.Title>
//         </Modal.Header>
//         <Modal.Body>
//           <Form onSubmit={handleSubmit}>
//             <Form.Group className="mb-3">
//               <Form.Label>Title</Form.Label>
//               <Form.Control
//                 type="text"
//                 name="title"
//                 value={formData.title}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Label>Description</Form.Label>
//               <Form.Control
//                 as="textarea"
//                 rows={3}
//                 name="description"
//                 value={formData.description}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Label>Location</Form.Label>
//               <Form.Control
//                 type="text"
//                 name="location"
//                 value={formData.location}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Label>Type</Form.Label>
//               <Form.Control
//                 type="text"
//                 name="type"
//                 value={formData.type}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Label>Posted Date</Form.Label>
//               <Form.Control
//                 type="date"
//                 name="postedDate"
//                 value={formData.postedDate}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Label>Expiry Date</Form.Label>
//               <Form.Control
//                 type="date"
//                 name="expiryDate"
//                 value={formData.expiryDate}
//                 onChange={handleChange}
//                 required
//               />
//             </Form.Group>
//             <Form.Group className="mb-3">
//               <Form.Check
//                 type="checkbox"
//                 label="Active"
//                 name="active"
//                 checked={formData.active}
//                 onChange={handleChange}
//               />
//             </Form.Group>
//             <Button type="submit">{editJob ? "Update Job" : "Post Job"}</Button>
//           </Form>
//         </Modal.Body>
//       </Modal>
//     </>
//   );
// };

// export default RecruiterDashboard;

import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Navbar,
  Nav,
  Button,
  Table,
  Modal,
  Form,
  Card,
  Alert,
  Badge,
  Dropdown,
} from "react-bootstrap";
import axios from "axios";

const RecruiterDashboard = () => {
  const token = localStorage.getItem("recruiterToken");
  const headers = { Authorization: `Bearer ${token}` };

  const [recruiter, setRecruiter] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [expiredJobs, setExpiredJobs] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editJob, setEditJob] = useState(null);
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    location: "",
    type: "",
    postedDate: "",
    expiryDate: "",
    active: true,
  });
  const [alert, setAlert] = useState("");
  const [applicants, setApplicants] = useState({});
  const [notifications, setNotifications] = useState([]);

  // Fetch recruiter details
  const fetchRecruiterDetails = async () => {
    try {
      const res = await axios.get(
        "http://localhost:8085/api/recruiter/auth/details",
        { headers }
      );
      setRecruiter(res.data);
      fetchNotifications(res.data.id);
    } catch (err) {
      console.error("Failed to fetch recruiter details:", err);
    }
  };

  // Fetch notifications
  const fetchNotifications = async (recruiterId) => {
    try {
      const res = await axios.get(
        `http://localhost:8086/api/job-application/notifications/recruiter/${recruiterId}`,
        { headers }
      );
      setNotifications(res.data);
    } catch (err) {
      console.error("Failed to fetch notifications:", err);
      setNotifications([]);
    }
  };

  // Fetch jobs
  const fetchJobs = async () => {
    if (!recruiter) return;
    try {
      const res = await axios.get(
        `http://localhost:8086/api/jobs/recruiter/${recruiter.id}?page=0&size=50&sortBy=postedDate`,
        { headers }
      );
      const jobList = Array.isArray(res.data)
        ? res.data
        : res.data.content || [];
      setJobs(jobList);
      fetchAllApplicants(jobList);
    } catch (err) {
      console.error("Failed to fetch jobs:", err);
      setJobs([]);
    }
  };

  const fetchExpiredJobs = async () => {
    if (!recruiter) return;
    try {
      const res = await axios.get(
        `http://localhost:8086/api/jobs/expired/${recruiter.id}`,
        { headers }
      );
      setExpiredJobs(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Failed to fetch expired jobs:", err);
      setExpiredJobs([]);
    }
  };

  const fetchAllApplicants = async (jobList) => {
    const tempApplicants = {};
    for (let i = 0; i < jobList.length; i++) {
      const job = jobList[i];
      try {
        const res = await axios.get(
          `http://localhost:8086/api/job-application/job/${job.id}/applicants`,
          { headers }
        );
        tempApplicants[job.id] = Array.isArray(res.data) ? res.data : [];
      } catch (err) {
        console.error(`Failed to fetch applicants for job ${job.id}:`, err);
        tempApplicants[job.id] = [];
      }
    }
    setApplicants(tempApplicants);
  };

  useEffect(() => {
    fetchRecruiterDetails();
  }, []);

  useEffect(() => {
    if (recruiter) {
      fetchJobs();
      fetchExpiredJobs();
    }
  }, [recruiter]);

  const handleChange = (e) => {
    const value =
      e.target.type === "checkbox" ? e.target.checked : e.target.value;
    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleEdit = (job) => {
    setEditJob(job);
    setFormData({
      title: job.title,
      description: job.description,
      location: job.location,
      type: job.type,
      postedDate: job.postedDate,
      expiryDate: job.expiryDate,
      active: job.active,
    });
    setShowModal(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (!recruiter) return;
      if (editJob) {
        await axios.put(
          `http://localhost:8086/api/jobs/${editJob.id}`,
          formData,
          { headers }
        );
        setAlert("Job updated successfully!");
      } else {
        await axios.post(
          "http://localhost:8086/api/jobs/post",
          {
            ...formData,
            recruiterId: recruiter.id,
          },
          { headers }
        );
        setAlert("Job posted successfully!");
      }
      setShowModal(false);
      setEditJob(null);
      setFormData({
        title: "",
        description: "",
        location: "",
        type: "",
        postedDate: "",
        expiryDate: "",
        active: true,
      });
      fetchJobs();
      fetchExpiredJobs();
    } catch (err) {
      console.error("Failed to save job:", err);
      setAlert("Failed to save job");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this job?")) return;
    try {
      await axios.delete(`http://localhost:8086/api/jobs/${id}`, { headers });
      setAlert("Job deleted successfully!");
      fetchJobs();
      fetchExpiredJobs();
    } catch (err) {
      console.error("Failed to delete job:", err);
      setAlert("Failed to delete job");
    }
  };

  const handleDownload = async (app) => {
    if (!app.applicationId) return window.alert("Invalid application ID");
    try {
      const res = await axios.get(
        `http://localhost:8086/api/job-application/download-resume/${app.applicationId}`,
        { headers, responseType: "blob" }
      );
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", app.resumeFileName || "resume.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (err) {
      console.error("Failed to download resume:", err);
      window.alert("Failed to download resume");
    }
  };

  // Shortlist, Select, Schedule, Reject
  const handleShortlist = async (app) => {
    try {
      await axios.post(
        `http://localhost:8086/api/job-application/shortlist`,
        null,
        { headers, params: { appliedJobId: app.applicationId } }
      );
      setAlert(`Shortlisted ${app.firstName} ${app.lastName}`);
      fetchAllApplicants(jobs);
    } catch (err) {
      console.error("Shortlist error:", err);
      setAlert("Failed to shortlist applicant");
    }
  };

  const handleSelect = async (app) => {
    try {
      await axios.post(
        `http://localhost:8086/api/job-application/select`,
        null,
        { headers, params: { appliedJobId: app.applicationId } }
      );
      setAlert(`Selected ${app.firstName} ${app.lastName}`);
      fetchAllApplicants(jobs);
    } catch (err) {
      console.error("Select error:", err);
      setAlert("Failed to select applicant");
    }
  };

  const handleSchedule = async (app) => {
    try {
      const interviewDateTime = prompt(
        "Enter interview date and time (YYYY-MM-DDTHH:MM)"
      );
      if (!interviewDateTime) return;
      const mode = prompt("Enter mode (ONLINE/OFFLINE)", "ONLINE");
      let location = null;
      let meetingLink = null;
      if (mode?.toUpperCase() === "OFFLINE") {
        location = prompt("Enter interview location");
      } else if (mode?.toUpperCase() === "ONLINE") {
        meetingLink = prompt("Enter meeting link (Zoom/Google Meet)");
      }

      await axios.post(
        `http://localhost:8086/api/job-application/schedule-interview`,
        null,
        {
          headers,
          params: {
            appliedJobId: app.applicationId,
            interviewDateTime,
            mode,
            location,
            meetingLink,
          },
        }
      );
      setAlert(`Interview scheduled for ${app.firstName} ${app.lastName}`);
      fetchAllApplicants(jobs);
    } catch (err) {
      console.error("Schedule interview error:", err);
      setAlert("Failed to schedule interview");
    }
  };

  const handleReject = async (app) => {
    const reason = prompt("Enter reason for rejection");
    if (!reason) return;
    try {
      await axios.post(
        `http://localhost:8086/api/job-application/reject`,
        null,
        { headers, params: { appliedJobId: app.applicationId, reason } }
      );
      setAlert(`Rejected ${app.firstName} ${app.lastName}`);
      fetchAllApplicants(jobs);
    } catch (err) {
      console.error("Reject error:", err);
      setAlert("Failed to reject applicant");
    }
  };

  return (
    <>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          <Navbar.Brand>Recruiter Dashboard</Navbar.Brand>
          <Nav className="ml-auto d-flex align-items-center">
            {recruiter && (
              <>
                <Dropdown align="end" className="me-3">
                  <Dropdown.Toggle variant="light" id="dropdown-basic">
                    Notifications ({notifications.length})
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    {notifications.length === 0 && (
                      <Dropdown.Item>No notifications</Dropdown.Item>
                    )}
                    {notifications.map((n) => (
                      <Dropdown.Item key={n.id}>
                        <strong>{n.title}</strong> - {n.message}
                      </Dropdown.Item>
                    ))}
                  </Dropdown.Menu>
                </Dropdown>
                <Nav.Link disabled>Welcome, {recruiter.username}</Nav.Link>
              </>
            )}
            <Button
              variant="outline-light"
              onClick={() => {
                localStorage.removeItem("recruiterToken");
                window.location.href = "/recruiter-login";
              }}
            >
              Logout
            </Button>
          </Nav>
        </Container>
      </Navbar>

      <Container className="mt-4">
        {alert && (
          <Alert variant="success" onClose={() => setAlert("")} dismissible>
            {alert}
          </Alert>
        )}

        {/* Stats */}
        <Row className="mb-4">
          <Col md={4}>
            <Card bg="primary" text="white">
              <Card.Body>
                <Card.Title>Total Jobs</Card.Title>
                <Card.Text>{jobs.length + expiredJobs.length}</Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card bg="success" text="white">
              <Card.Body>
                <Card.Title>Active Jobs</Card.Title>
                <Card.Text>{jobs.length}</Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4}>
            <Card bg="danger" text="white">
              <Card.Body>
                <Card.Title>Expired Jobs</Card.Title>
                <Card.Text>{expiredJobs.length}</Card.Text>
              </Card.Body>
            </Card>
          </Col>
        </Row>

        {/* Post New Job Button + Active Jobs */}
        <Row className="mb-3 d-flex justify-content-between align-items-center">
          <Col>
            <h4>Active Jobs</h4>
          </Col>
          <Col className="text-end">
            <Button
              variant="primary"
              onClick={() => {
                setEditJob(null);
                setFormData({
                  title: "",
                  description: "",
                  location: "",
                  type: "",
                  postedDate: "",
                  expiryDate: "",
                  active: true,
                });
                setShowModal(true);
              }}
            >
              Post New Job
            </Button>
          </Col>
        </Row>

        {/* Active Jobs Table */}
        {jobs.length === 0 ? (
          <p>No jobs posted.</p>
        ) : (
          <Table striped bordered hover responsive className="shadow-sm">
            <thead className="table-dark">
              <tr>
                <th>Title</th>
                <th>Description</th>
                <th>Type</th>
                <th>Location</th>
                <th>Posted Date</th>
                <th>Expiry Date</th>
                <th>Active</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {jobs.map((job) => (
                <tr key={job.id}>
                  <td>{job.title}</td>
                  <td>{job.description}</td>
                  <td>{job.type}</td>
                  <td>{job.location}</td>
                  <td>{job.postedDate}</td>
                  <td>{job.expiryDate}</td>
                  <td>
                    {job.active ? (
                      <Badge bg="success">Yes</Badge>
                    ) : (
                      <Badge bg="secondary">No</Badge>
                    )}
                  </td>
                  <td>
                    <Button
                      size="sm"
                      variant="info"
                      onClick={() => handleEdit(job)}
                    >
                      Edit
                    </Button>{" "}
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={() => handleDelete(job.id)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}

        {/* Applicants */}
        <h4 className="mt-5">Applicants</h4>
        {jobs.map((job) => (
          <div key={job.id} className="mb-4">
            <h5>{job.title}</h5>
            {!applicants[job.id] || applicants[job.id].length === 0 ? (
              <p>No applicants yet.</p>
            ) : (
              <Table striped bordered hover responsive className="shadow-sm">
                <thead className="table-secondary">
                  <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Applied Date</th>
                    <th>Resume</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {applicants[job.id].map((app) => (
                    <tr key={app.applicationId}>
                      <td>
                        {app.firstName} {app.lastName}
                      </td>
                      <td>{app.email}</td>
                      <td>{app.appliedDate}</td>
                      <td>
                        {app.resumeFileName ? (
                          <Button
                            variant="primary"
                            size="sm"
                            onClick={() => handleDownload(app)}
                          >
                            Download
                          </Button>
                        ) : (
                          "No Resume"
                        )}
                      </td>
                      <td>
                        <Badge
                          bg={
                            app.status === "SHORTLISTED"
                              ? "warning"
                              : app.status === "SELECTED"
                              ? "success"
                              : app.status === "REJECTED"
                              ? "danger"
                              : "secondary"
                          }
                        >
                          {app.status || "APPLIED"}
                        </Badge>
                      </td>
                      <td>
                        {app.status === "SHORTLISTED" ? (
                          <Button size="sm" variant="secondary" disabled>
                            Shortlisted
                          </Button>
                        ) : (
                          <Button
                            size="sm"
                            variant="warning"
                            onClick={() => handleShortlist(app)}
                          >
                            Shortlist
                          </Button>
                        )}{" "}
                        <Button
                          size="sm"
                          variant="success"
                          onClick={() => handleSelect(app)}
                          disabled={app.status === "SELECTED"}
                        >
                          {app.status === "SELECTED" ? "Selected" : "Select"}
                        </Button>{" "}
                        <Button
                          size="sm"
                          variant="info"
                          onClick={() => handleSchedule(app)}
                        >
                          Schedule
                        </Button>{" "}
                        <Button
                          size="sm"
                          variant="danger"
                          onClick={() => handleReject(app)}
                        >
                          Reject
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            )}
          </div>
        ))}
      </Container>

      {/* Modal for Post/Edit Job */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{editJob ? "Edit Job" : "Post New Job"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Title</Form.Label>
              <Form.Control
                type="text"
                name="title"
                value={formData.title}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="description"
                value={formData.description}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Location</Form.Label>
              <Form.Control
                type="text"
                name="location"
                value={formData.location}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Type</Form.Label>
              <Form.Control
                type="text"
                name="type"
                value={formData.type}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Posted Date</Form.Label>
              <Form.Control
                type="date"
                name="postedDate"
                value={formData.postedDate}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Expiry Date</Form.Label>
              <Form.Control
                type="date"
                name="expiryDate"
                value={formData.expiryDate}
                onChange={handleChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Check
                type="checkbox"
                label="Active"
                name="active"
                checked={formData.active}
                onChange={handleChange}
              />
            </Form.Group>
            <Button variant="primary" type="submit">
              {editJob ? "Update Job" : "Post Job"}
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default RecruiterDashboard;
