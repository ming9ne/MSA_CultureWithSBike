// reactstrap components
import {
  Button,
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  InputGroupAddon,
  InputGroupText,
  InputGroup,
  Row,
  Col,
} from "reactstrap";
import {Link} from "react-router-dom";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const[id, setId] = useState("");
  const[password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleInputId = e => {
    setId(e.target.value);
  }

  const handleInputPassword = e => {
    setPassword(e.target.value);
  }

  const loginConfirm = async e => {
    e.preventDefault();
    if(id.length === 0 || password.length === 0) {
      window.alert("!!!");
    } else {
      fetch(`http://localhost:8000/api/v1/user-service/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id, password
        }),
      }).then(response => {
        if(response.status == 200) {
          console.log(response.headers);
          // console.log(response.headers.get("Authorization"), response.headers.get("username"), response.headers.get("email"))
          localStorage.setItem("login-token", response.headers.get("Authorization"));
          return response.json();
          // localStorage.setItem("username", response.headers.get("username"));
          // localStorage.setItem("email", response.headers.get("email"));
          
        }
      }).then(response => {
        console.log(response);
        localStorage.setItem("id", response.id);
        localStorage.setItem("username", response.username);
        localStorage.setItem("email", response.email);
        localStorage.setItem("uid", response.uid);
        alert("로그인이 완료되었습니다!");
        navigate("/");
      }).catch(e => {
        console.log(e);
        alert("로그인이 실패했습니다.");
      })

      
    }
  }

  return (
    <>
      <Col lg="5" md="7">
        <Card className="bg-secondary shadow border-0">
          <CardBody className="px-lg-5 py-lg-5">
            <div className="text-center text-muted mb-4">
              <small>Sign in with credentials</small>
            </div>
            <Form role="form">
              <FormGroup className="mb-3">
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-single-02" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="Id"
                    type="text"
                    onChange={handleInputId}
                    value={id}
                  />
                </InputGroup>
              </FormGroup>
              <FormGroup>
                <InputGroup className="input-group-alternative">
                  <InputGroupAddon addonType="prepend">
                    <InputGroupText>
                      <i className="ni ni-lock-circle-open" />
                    </InputGroupText>
                  </InputGroupAddon>
                  <Input
                    placeholder="Password"
                    type="password"
                    autoComplete="new-password"
                    onChange={handleInputPassword}
                    value={password}
                  />
                </InputGroup>
              </FormGroup>
              <div className="custom-control custom-control-alternative custom-checkbox">
                <input
                  className="custom-control-input"
                  id=" customCheckLogin"
                  type="checkbox"
                />
                <label
                  className="custom-control-label"
                  htmlFor=" customCheckLogin"
                >
                  <span className="text-muted">Remember me</span>
                </label>
              </div>
              <div className="text-center">
                <Button className="my-4" color="primary" type="button" onClick={loginConfirm}>
                  Sign in
                </Button>
              </div>
            </Form>
          </CardBody>
        </Card>
        <Row className="mt-3">
          <Col xs="6">
            <a
              className="text-light"
              href="#pablo"
              // onClick={(e) => e.preventDefault()}
            >
              <small>Forgot password?</small>
            </a>
          </Col>
          <Col className="text-right" xs="6">
            <Link
              className="text-light"
              to="/auth/register"
              // onClick={(e) => e.preventDefault()}
            >
              <small>Create new account</small>
            </Link>
          </Col>
        </Row>
      </Col>
    </>
  );
};

export default Login;
