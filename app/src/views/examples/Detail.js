import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link, useLocation, Navigate, useNavigate } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText, Container, Row } from "reactstrap";
import Header from "components/Headers/Header.js";

function Detail() { // () 안에 정보들
    const[congestion, setCongestion] = useState([]);
    const[population, setPopulation] = useState([]);
    const[sbikes, setSbikes] = useState([]);
    const navigate = useNavigate();
    const location = useLocation();
    console.log(location.state);
    
    useEffect(() => {
        if(location.state === null) {
            navigate("/");
        } else {
            // console.log(location.state);
            fetch(`http://localhost:8000/api/v1/congestion-service/congestion/${location.state.areaNm}`)
                .then(response => response.json())
                .then(response => { 
                    setCongestion(response);
                    console.log(response);
                })
                .catch(e => {
                    console.log(e);
            });

            fetch(`http://localhost:8000/api/v1/congestion-service/population/${location.state.areaNm}`)
                .then(response => response.json())
                .then(response => {
                    setPopulation(response);
                    // console.log(response);
                })
                .catch(e => {
                    console.log(e);
                })

            fetch(`http://localhost:8000/api/v1/sbike-service/listkakao/${location.state.title}`)
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    setSbikes(data);
                })
                .catch(e => {
                    console.log(e);
                })
        };        
    }, [])

    if(location.state) {
        return (
            <>
                <Header />
                <Container className="mt--7" fluid>
                    <Row>
                        <Card
                        style={{
                            margin: "30px",
                            width: "600px"
                        }}>
                        <CardHeader tag="h3">
                            <Link to="/admin/maps" state={location.state} sbike={sbikes} >{location.state.title}</Link>
                        </CardHeader>
                        <CardBody>
                            <CardImg 
                                src={location.state.mainImg} 
                                alt={location.state.title} 
                                title={location.state.title} 
                                style={{
                                    height: 300
                                }}
                                top
                                width="100%"
                            />
                            <CardText>
                                <h1>장소</h1>
                                {location.state.startDate&&location.state.endDate ? <>기간 : {location.state.startDate} ~ {location.state.endDate} <br /></> : <>기간 : 미정</>}
                                {location.state.guname ? <>지역 : {location.state.guname} <br /></> : ""}
                                {location.state.place ? <>장소 : {location.state.place} <br /></> : ""}
                                {location.state.useFee ? <>이용요금 : {location.state.useFee} <br /></> : ""}
                                {location.state.player ? <>출연자정보 : {location.state.player} <br /></> : ""}
                                {location.state.program ? <>프로그램소개 : {location.state.program}</> : ""}<br />
                                <h1>혼잡도</h1>
                                {congestion.areaCongestLvl ? (<>장소 혼잡도 지표 : {congestion.areaCongestLvl}<br /></>) : ""}
                                {congestion.areaCongestMsg ? <>장소 혼잡도 지표 관련 메세지 : {congestion.areaCongestMsg}<br /></> : ""}
                                {population.areaPpltnMin && population.areaPpltnMax ? (
                                    <>인구 수 : {population.areaPpltnMin} ~ {population.areaPpltnMax}(만 명)<br /></>
                                ) : <></>}<br />
                                <h1>가까운 따릉이 대여소</h1>
                                {sbikes.map((sbike) => {
                                    return (
                                        <>
                                            대여소 명 : {sbike.stationName}<br/>
                                            거리 : {sbike.distance}<br/>
                                        </>
                                    );
                                })}
                            </CardText>
                        </CardBody>
                        <CardFooter>
                            <a
                                className="font-weight-bold ml-1"
                                href={location.state.orgLink}
                                rel="noopener noreferrer"
                                target="_blank"
                            >홈페이지</a>
                        </CardFooter>
                    </Card>
                    </Row>
                </Container>
            </>
        );
    }
}

export default Detail;
