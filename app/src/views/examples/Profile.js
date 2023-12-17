// reactstrap components
import {
  Button,
  Card,
  CardHeader,
  CardBody,
  FormGroup,
  Form,
  Input,
  Container,
  Row,
  Col,
} from "reactstrap";
import React, { useEffect, useState } from "react";
// core components
import UserHeader from "components/Headers/UserHeader.js";

const Profile = () => {
  const [coupons, setCoupons] = useState([]);
  
  useEffect(() => {
    fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/coupon-service/userCoupons/${localStorage.getItem("id")}`, {
      method: "GET",
      headers: {
      "Content-Type": "application/json",
      "Authorization" : localStorage.getItem("login-token")
      }})
        .then(response => {
          if(response.ok) {
            return response.json();
          }
          return [];
        })
        .then(data => {
          setCoupons(data);
        })
        .catch(e => {
            console.log(e);
    });
  }, [])

  return (
    <>
      <UserHeader />
      {/* Page content */}
      <Container className="mt--7" fluid>
        <Row>
          <Col className="order-xl-1 mb-5 mb-xl-0" xl="4">
            <Card className="card-profile shadow">
              <Row className="justify-content-center">
                <Col className="order-lg-2" lg="3">
                  <div className="card-profile-image">
                    <a href="#pablo" onClick={(e) => e.preventDefault()}>
                      <img
                        alt="..."
                        className="rounded-circle"
                        src={require("../../assets/img/theme/profile.jpg")}
                      />
                    </a>
                  </div>
                </Col>
              </Row>
              <CardHeader className="text-center border-0 pt-8 pt-md-4 pb-0 pb-md-4">

              </CardHeader>
              <CardBody className="pt-0 pt-md-4">
                <Row>
                  <div className="col">
                    <div className="card-profile-stats d-flex justify-content-center mt-md-5">
                      {/* <div>
                        <span className="heading">22</span>
                        <span className="description">즐겨찾기</span>
                      </div> */}
                      <div>
                        <span className="heading">{coupons.length}</span>
                        <span className="description">쿠폰</span>
                      </div>
                    </div>
                  </div>
                </Row>
                <div className="text-center">
                  <h3>
                    Username : {localStorage.getItem("username")}
                    <span className="font-weight-light"></span>
                  </h3>
                  <div className="h5 font-weight-300">
                    <i className="ni location_pin mr-2" />
                    Email : {localStorage.getItem("email")}
                  </div>
                  <hr className="my-4" />
                  <p>
                    {localStorage.getItem("uid")}
                  </p>
                </div>
              </CardBody>
            </Card>
          </Col>
          <Col className="order-xl-2" xl="8">
            <Card className="bg-secondary shadow">
              <CardHeader className="bg-white border-0">
                <Row className="align-items-center">
                  <Col xs="8">
                    <h3 className="mb-0">내 쿠폰</h3>
                  </Col>
                  <Col className="text-right" xs="4">
                    <Button
                      color="primary"
                      href="#pablo"
                      onClick={(e) => e.preventDefault()}
                      size="sm"
                    >
                      Settings
                    </Button>
                  </Col>
                </Row>
              </CardHeader>
              <CardBody>
                <Row>
                  {coupons.length === 0 ? <></> : coupons.map(coupon => {; 
                    return( 
                      <Card
                        style={{
                            margin: "30px",
                            width: "300px"
                        }}>
                        <CardHeader tag="h3">
                            <Row className="align-items-center">
                                <Col xs="6">
                                    {coupon.coupon.used ? 
                                    <h3 className="mb-0" style={{ textDecoration: 'line-through', color: 'gray' }}>{coupon.coupon.couponCode}</h3> : 
                                    <h3 className="mb-0">{coupon.coupon.couponName}</h3>}
                                    
                                </Col>
                            </Row>
                        </CardHeader>
                        <CardBody>
                            <h2>{coupon.coupon.couponCode}</h2><br/>
                            {coupon.coupon.issueDate} ~ {coupon.coupon.expirationDate}
                        </CardBody>
                      </Card>
                    )
                  })}
                </Row>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Profile;
