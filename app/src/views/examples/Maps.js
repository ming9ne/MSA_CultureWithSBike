import React, {useState, useEffect, useRef} from "react";

// reactstrap components
import { Button, ButtonGroup, Card, Container, Row, CardBody, CardHeader, CardImg, Col } from "reactstrap";

// core components
import Header from "components/Headers/Header.js";
import { useLocation } from "react-router-dom";
import Event from "components/Event/Event";
import { Map, MapMarker, MapTypeControl, ZoomControl, MapTypeId, CustomOverlayMap, MapInfoWindow } from "react-kakao-maps-sdk";
import RemovableCustomOverlayStyle from "components/Event/RemovableCustomOverlayStyle";

const {kakao} = window;
let map;

const Maps = (props) => {
  const location = useLocation();
  const [mapType, setMapType] = useState("ROADMAP");
  const [state, setState] = useState({
    // 지도의 초기 위치
    center: { lat: 37.566535, lng: 126.9779692 },
    // 지도 위치 변경시 panto를 이용할지에 대해서 정의
    isPanto: false,
  })
  const [events, setEvents] = useState([]);
  const [sbikes, setSbikes] = useState();

  useEffect(() => {
    if(location.state) {
      console.log("location state is here!", location.state);
      setState({
        center: { lat: location.state.state.lat, lng: location.state.state.lot },
        isPanto: false,
      })
      setEvents([location.state.state]);
      setSbikes(location.state.sbikes, {isOpen: false});
    } else {
      console.log("event fetching");
      fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/event-service/todayEvents`)
        .then(response => response.json())
        .then(data => {
          console.log(data);
          setEvents(data, {isOpen: false});
          // setEvents(data);
        })
        .catch(e => {
          console.log(e);
        })
    }
  }, [])

  const handleMarkerClick = (clickedPositionIndex) => {
    setEvents((prevPositions) => {
      const newPositions = [...prevPositions];
      newPositions[clickedPositionIndex].isOpen = !newPositions[clickedPositionIndex].isOpen;
      return newPositions;
    });
  };

  const handleMarkerMouse = (clickedPositionIndex) => {
    setSbikes((prevPositions) => {
      const newPositions = [...prevPositions];
      newPositions[clickedPositionIndex].isOpen = !newPositions[clickedPositionIndex].isOpen;
      return newPositions;
    });
  };

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
        <Row>
          <div className="col">
            <ButtonGroup>
              <Button color="primary" onClick={() => {
                setMapType("TRAFFIC");
                }}>혼잡도</Button>
              <Button color="primary" onClick={() => {
                setMapType("ROADMAP");
              }}>지도</Button>
            </ButtonGroup>
            <Card className="shadow border-0">   
              <RemovableCustomOverlayStyle />
              <Map
                center={state.center}
                isPanto={state.isPanto}
                style={{ width: "100%", height: "60vh" }}
                level={6}
              >
                <MapTypeControl position={"TOPRIGHT"} />
                <ZoomControl position={"RIGHT"} />
                <MapTypeId type={mapType} />
                {/* <MapMarker
                  position={{ lat: 37.566535, lng: 126.9779692 }}
                /> */}
                {sbikes && sbikes.map((sbike, index) => (
                  <MapMarker
                    key={`${sbike.stationName}`}
                    position={{lat: sbike.stationLatitude, lng: sbike.stationLongitude}} // 마커를 표시할 위치
                    title={sbike.stationName}
                    image={{
                      src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png",
                      size: { width: 22, height: 26 },
                      options: {
                        spriteSize: { width: 36, height: 98 },
                        spriteOrigin: { x: 10, y: 72 },
                      },
                    }}
                    onMouseOver={() => handleMarkerMouse(index)}
                    // onMouseOut={() => handleMarkerMouse(index)}
                  />
                ))}
                {events && events.map((event, index) => (<>
                  <MapMarker
                    key={`${event.title}`}
                    position={{lat: event.lat, lng: event.lot}} // 마커를 표시할 위치
                    title={event.title} // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
                    onClick={() => handleMarkerClick(index)} 
                  />
                  {event.isOpen && (
                    <CustomOverlayMap position={{lat: event.lat, lng: event.lot}}>
                      <div className="wrap">
                        <div className="info">
                          <div className="title">
                            {event.title}
                            <div
                              className="close"
                              onClick={() => handleMarkerClick(index)}
                              title="닫기"
                            ></div>
                          </div>
                          <div className="body">
                            <div className="img">
                              <img
                                src={event.mainImg}
                                width="73"
                                height="70"
                                alt={event.title}
                              />
                            </div>
                            <div className="desc">
                              <div className="ellipsis">
                                {event.place}
                              </div>
                              <div className="jibun ellipsis">
                                {event.strtdate} ~ {event.endDate}
                              </div>
                              <div>
                                <a
                                  href={event.orgLink}
                                  target="_blank"
                                  className="link"
                                  rel="noreferrer"
                                >
                                  홈페이지
                                </a>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </CustomOverlayMap>
                  )}
                </>
              ))}

              </Map>
              
            </Card>
          </div>
        </Row>
      </Container>
    </>
  );
};

export default Maps;
