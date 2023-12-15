import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link, useLocation, Navigate, useNavigate } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText, Container, Row, Button, Col} from "reactstrap";
import Header from "components/Headers/Header.js";

function Coupon() {
    const [coupons, setCoupons] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    // console.log(location.state);
    
    useEffect(() => {
        fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/coupon-service/coupons`, {
            headers: {
                "Authorization" : localStorage.getItem("login-token")
            }
        })
            .then(response => {
                console.log(response);
                return response.json()
            })
            .then(response => {
                setCoupons(response);
                console.log(response);
            })
            .catch(e => {
                console.log(e);
        });
        console.log(coupons);
    }, [])

    return (
        <>
            <Header />
            <Container className="mt--7" fluid>
                <Row>
                    {coupons.map((coupon) => {
                        return (
                            <Card
                                style={{
                                    margin: "30px",
                                    width: "300px"
                            }}key={coupon.couponCode}>
                                <CardHeader tag="h3">
                                    <Row className="align-items-center">
                                        <Col xs="6">
                                            <h3 className="mb-0">{coupon.couponName}</h3>
                                        </Col>
                                        <Col className="text-right" xs="6">
                                            <Button
                                                color="primary"
                                                onClick={(e) => {
                                                    e.preventDefault();
                                                    e.target.hidden=true;
                                                    fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/coupon-service/userCoupon`, {
                                                        method: "POST",
                                                        headers: {
                                                        "Content-Type": "application/json",
                                                        "Authorization" : localStorage.getItem("login-token")
                                                        },
                                                        body: JSON.stringify({
                                                            couponCode: coupon.couponCode, userId: localStorage.getItem("id")
                                                        }),
                                                    })
                                                    .then(response => {
                                                        if(response.ok) {
                                                            return response.json()
                                                        } else {
                                                            return response.json().then(data => Promise.reject(data));
                                                        }
                                                    }).then(data => {
                                                        console.log(data);
                                                        alert("쿠폰이 발급되었습니다.");
                                                        window.location.reload();
                                                    })
                                                    .catch(e => {
                                                        alert(e.error);
                                                        window.location.reload();
                                                    })
                                                }}
                                                size="sm"
                                                >
                                                발급받기
                                            </Button>
                                        </Col>
                                    </Row>
                                </CardHeader>
                                <CardBody>
                                    <h2>{coupon.couponCode}</h2><br/>
                                    기한 : {coupon.issueDate} ~ {coupon.expirationDate}<br/>
                                    남은 개수 : {coupon.quantity} <br />
                                </CardBody>
                            </Card>
                        )
                    })}
                </Row>
            </Container>
        </>
    );
}

export default Coupon;
