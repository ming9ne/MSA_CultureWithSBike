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
        fetch(`http://localhost:8000/api/v1/coupon-service/coupons`)
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
                            }}>
                                <CardHeader tag="h3">
                                    <Row className="align-items-center">
                                        <Col xs="6">
                                            <h3 className="mb-0">{coupon.couponCode}</h3>
                                        </Col>
                                        <Col className="text-right" xs="6">
                                            <Button
                                                color="primary"
                                                href="#pablo"
                                                onClick={(e) => {
                                                    e.preventDefault();
                                                    fetch(`http://localhost:8000/api/v1/coupon-service/userCoupon`, {
                                                        method: "POST",
                                                        headers: {
                                                        "Content-Type": "application/json",
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
                                                        navigate("/admin/coupons");
                                                    })
                                                    .catch(e => {
                                                        alert(e.error);
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
