import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faVideoSlash } from "@fortawesome/free-solid-svg-icons";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { NavLink } from "react-router-dom";
import Register from "../registrationForm/RegistrationForm";
import Login from "../loginForm/LoginForm";
import { useEffect, useState } from "react";
import AuthService from "../../services/auth.service";


const Header = () => {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (user) {
      setCurrentUser(user.username);
    }
  }, []);

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container fluid>
        <Navbar.Brand href="/" style={{"color":'gold'}}>
          <FontAwesomeIcon icon={faVideoSlash}/>GoldSet
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{maxHeight: '100px'}}
            navbarScroll
          >
            <NavLink className="nav-link" to="/">Home</NavLink>
            <NavLink className="nav-link" to="/watchList">Watch List</NavLink>
          </Nav>
          <div style={{ flexGrow: 1, textAlign: "center", "color":'gold' }}>
            <span>
              <h4>{currentUser ? `Welcome, ${currentUser}` : ""}</h4>
            </span>
          </div>
          <Button variant="outline-secondary" className="me-2">Tickets</Button>
          <Login setCurrentUser={setCurrentUser} />
          <Register />
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header