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
import { useParams } from "react-router-dom";
// core components
// import "./Table.css";
import Event from "components/Event/Event.js"
import Header from "components/Headers/Header.js";
import Pagebar from "components/Event/Pagebar";

const List = () => {
  const [allEvents, setAllEvents] = useState([]);
  const [events, setEvents] = useState([]);
  const [limit, setLimit] = useState(20); // 한 페이지에 보여줄 데이터의 개수
  const [page, setPage] = useState(1); // 페이지 초기 값은 1페이지
  const [counts, setCounts] = useState(1); // 데이터의 총 개수를 setCounts 에 저장해서 사용
  const [blockNum, setBlockNum] = useState(0); // 한 페이지에 보여 줄 페이지네이션의 개수를 block으로 지정하는 state. 초기 값은 0
  let params = useParams();

  useEffect(() => {
    // const page = params.page || 1;
    console.log("page", page);
    fetch(`http://localhost:8000/api/v1/event-service/events`)
      .then(response => response.json())
      .then(response => {
        console.log(response);
        setAllEvents(response);
        setCounts(response.length);
      })
      .catch(e => {
        console.log(e);
      })
    
    console.log("events", events);
    setCounts(events.length);

    

    // fetch(`http://localhost:8000/api/v1/event-service/events/${page}`)
    //   .then(response => response.json())
    //   .then(response => {
    //     // console.log(response);
    //     setEvents(response);
    //   })
    //   .catch(e => {
    //     console.log(e);
    //   })
    
  }, []);

  useEffect(() => {
    const startIndex = (page - 1) * limit;
    const endIndex = startIndex + limit;
    const slicedEvents = allEvents.slice(startIndex, endIndex);
    setEvents(slicedEvents);
    window.scrollTo(0, 0);
  }, [page, limit, allEvents]);

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
        {/* Table */}
        <Row>
          {events.map((event) => {
              // console.log(event);
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
        {/* <Pagebar totalCount={totalCount} page={params.page} perPage={10} /> */}
        <Pagebar
        limit={limit}
        page={page}
        setPage={setPage}
        blockNum={blockNum}
        setBlockNum={setBlockNum}
        counts={counts}
      />
      </Container>
    </>
  );
};

export default List;
