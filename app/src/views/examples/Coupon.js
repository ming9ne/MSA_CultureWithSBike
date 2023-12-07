import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link, useLocation, Navigate, useNavigate } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText, Container, Row } from "reactstrap";
import Header from "components/Headers/Header.js";

function Coupon() {
    const [coupons, setCoupons] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    console.log(location.state);
    
    useEffect(() => {
        if(location.state === null) {
            navigate("/");
        } else {
            // console.log(location.state);
            fetch(`http://localhost:8000/api/v1/coupon-service/userCoupons/${localStorage.getItem("id")}`)
                .then(response => response.json())
                .then(response => { 
                    setCoupons(response);
                    console.log(response);
                })
                .catch(e => {
                    console.log(e);
            });
            console.log(coupons);
        }
    }, [])

    if(location.state) {
        return (
            <>
                <Header />
                <Container className="mt--7" fluid>
                    <Row>
                        
                    </Row>
                </Container>
            </>
        );
    }
}

export default Coupon;
