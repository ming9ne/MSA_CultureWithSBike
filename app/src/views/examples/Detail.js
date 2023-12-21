import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link, useLocation, Navigate, useNavigate } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText, Container, Row } from "reactstrap";
import Header from "components/Headers/Header.js";

function Detail() { // () 안에 정보들
    const [congestion, setCongestion] = useState([]);
    const [population, setPopulation] = useState([]);
    // const[sbikes, setSbikes] = useState([{
    //     "stationName": "경복궁역 4번 출구 뒤",
    //     "stationLatitude": "37.586150",
    //     "stationLongitude": "126.98705",
    //     "distance": "-1"
    // }, {
    //     "stationName": "경복궁역 5번 출구 뒤",
    //     "stationLatitude": "37.566175",
    //     "stationLongitude": "126.98705",
    //     "distance": "-1"
    // }, {
    //     "stationName": "경복궁역 6번 출구 뒤",
    //     "stationLatitude": "37.572200",
    //     "stationLongitude": "126.98705",
    //     "distance": "-1"
    // }, ]);
    const [sbikes, setSbikes] = useState();
    const navigate = useNavigate();
    const location = useLocation();
    console.log(location.state);
    
    useEffect(() => {
        if(location.state === null) {
            navigate("/");
        } else {
            // console.log(location.state);
            fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/congestion-service/congestion/${location.state.areaNm}`)
                .then(response => response.json())
                .then(response => { 
                    setCongestion(response);
                    console.log(response);
                })
                .catch(e => {
                    console.log(e);
            });

            fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/congestion-service/population/${location.state.areaNm}`)
                .then(response => response.json())
                .then(response => {
                    setPopulation(response);
                    // console.log(response);
                })
                .catch(e => {
                    console.log(e);
                })

            fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/sbike-service/kakaos/${location.state.id}`)
                .then(response => response.json())
                .then(data => {
                    console.log(data[0].sbike);
                    setSbikes(data[0].sbike);
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
                            <Link to="/admin/maps" state={{state: location.state, sbikes}} >{location.state.title}</Link>
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
                                {location.state.strtdate&&location.state.endDate ? <>기간 : {location.state.strtdate} ~ {location.state.endDate} <br /></> : <>기간 : 미정</>}
                                {location.state.guname ? <>지역 : {location.state.guname} <br /></> : ""}
                                {location.state.place ? <>장소 : {location.state.place} <br /></> : ""}
                                {location.state.useFee ? <>이용요금 : {location.state.useFee} <br /></> : ""}
                                {location.state.player ? <>출연자정보 : {location.state.player} <br /></> : ""}
                                {location.state.program ? <>프로그램소개 : {location.state.program}</> : ""}<br />
                                <h1>혼잡도</h1>
                                {congestion.areaCongestLvl ? (<>장소 혼잡도 지표 : {congestion.areaCongestLvl}<br /></>) : ""}
                                {congestion.areaCongestMsg ? <>장소 혼잡도 지표 관련 메세지 : {congestion.areaCongestMsg}<br /></> : ""}
                                {population.areaPpltnMin && population.areaPpltnMax ? (
                                    <>인구 수 : {population.areaPpltnMin} ~ {population.areaPpltnMax} 명<br /></>
                                ) : <></>}<br />
                                <h1>가까운 따릉이 대여소</h1>
                                {sbikes && sbikes.map((sbike, index) => {
                                    return (
                                        <>
                                            {index+1}) <b>{sbike.stationName}</b><br/>
                                            &emsp;&emsp;거치대 개수 : {sbike.rackTotCnt}<br/>
                                            &emsp;&emsp;자전거 주차 총 건수 : {sbike.parkingBikeTotCnt}<br/>
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
