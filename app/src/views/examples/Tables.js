import {
  Badge,
  Card,
  CardHeader,
  CardFooter,
  DropdownMenu,
  DropdownItem,
  UncontrolledDropdown,
  DropdownToggle,
  Media,
  Pagination,
  PaginationItem,
  PaginationLink,
  Progress,
  Table,
  Container,
  Row,
  UncontrolledTooltip,
  CardBody,
  Col
} from "reactstrap";
import React, { useState, useEffect } from "react";
// core components
// import "./Table.css";
import Event from "components/Event/Event.js"
import Header from "components/Headers/Header.js";

const Tables = () => {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    setEvents([{
      id: 1,
      title: "마포아트센터 M 레트로 시리즈 2024 신년맞이 어떤가요 #7",
      strtdate: "2023-11-21",
      endDate: "2023-11-22",
      areaNm: "경복궁",
      codename: "콘서트",
      guname: "마포구",
      place: "마포아트센터 아트홀 맥",
      useFee: " R석 66,000원/ S석 55,000원/ A석 44,000원",
      player: "조성모, 뱅크(정시로)",
      program: "",
      orgLink: "https://www.mfac.or.kr/performance/whole_view.jsp?sc_b_category=17&amp;sc_b_code=BOARD_1207683401&amp;pk_seq=2285&amp;page=1",
      mainImg: "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=43bd8ae3612e4cb2bb3a7edf9186efbf&amp;thumb=Y",
      lot: "37.5499060881738",
      lat: "126.945533810385"
    }, {
      id: 2,
      title: "마포아트센터 M 레트로 시리즈 2024 신년맞이 어떤가요 #7",
      strtdate: "2023-11-21",
      endDate: "2023-11-22",
      areaNm: "경복궁",
      codename: "콘서트",
      guname: "마포구",
      place: "마포아트센터 아트홀 맥",
      useFee: " R석 66,000원/ S석 55,000원/ A석 44,000원",
      player: "조성모, 뱅크(정시로)",
      program: "",
      orgLink: "https://www.mfac.or.kr/performance/whole_view.jsp?sc_b_category=17&amp;sc_b_code=BOARD_1207683401&amp;pk_seq=2285&amp;page=1",
      mainImg: "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=43bd8ae3612e4cb2bb3a7edf9186efbf&amp;thumb=Y",
      lot: "37.5499060881738",
      lat: "126.945533810385"
    }, {
      id: 2,
      title: "마포아트센터 M 레트로 시리즈 2024 신년맞이 어떤가요 #7",
      strtdate: "2023-11-21",
      endDate: "2023-11-22",
      areaNm: "경복궁",
      codename: "콘서트",
      guname: "마포구",
      place: "마포아트센터 아트홀 맥",
      useFee: " R석 66,000원/ S석 55,000원/ A석 44,000원",
      player: "조성모, 뱅크(정시로)",
      program: "",
      orgLink: "https://www.mfac.or.kr/performance/whole_view.jsp?sc_b_category=17&amp;sc_b_code=BOARD_1207683401&amp;pk_seq=2285&amp;page=1",
      mainImg: "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=43bd8ae3612e4cb2bb3a7edf9186efbf&amp;thumb=Y",
      lot: "37.5499060881738",
      lat: "126.945533810385"
    }])

    fetch(`http://localhost:8000/api/v1/event-service/events`)
      .then(response => response.json())
      .then(response => {
        console.log(response);
        setEvents(response);
      })
    
  }, []);

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
        {/* Table */}
        <h1>123</h1>
        <Row>
          {events.map((event) => {
              console.log(event);
              return (
                // <Col sm="6">
                  <div className="events">
                    <Event
                      key={event.id}
                      id={event.id}
                      title={event.title}
                      startDate={event.strtdate}
                      endDate={event.endDate}
                      areaNm={event.areaNm}
                      codeName={event.codename}
                      guname={event.guname}
                      place={event.place}
                      useFee={event.useFee}
                      player={event.player}
                      program={event.program}
                      orgLink={event.orgLink}
                      mainImg={event.mainImg}
                      lot={event.lot}
                      lat={event.lat}
                    />
                  </div>
                // </Col>
              );
          })}
        </Row>
      </Container>
    </>
  );
};

export default Tables;
