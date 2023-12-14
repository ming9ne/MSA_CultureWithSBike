import React, {useState, useEffect, useRef} from "react";

// reactstrap components
import { Button, ButtonGroup, Card, Container, Row } from "reactstrap";

// core components
import Header from "components/Headers/Header.js";
import { useLocation } from "react-router-dom";
import Event from "components/Event/Event";

const {kakao} = window;
let map;

const MapWrapper = () => {
  const location = useLocation();
  const mapRef = useRef(null);
  const events = useRef(null);
  const positions = useRef([{content: null, latlng: null}]);
  const [sbikes, setSbikes] = useState([]);
  // let positions = [{
  //     // content: '<div>카카오</div>',
  //     content: `<div>카카오</div>`,
  //     latlng: new kakao.maps.LatLng(37.566535, 126.9779692)
  //   }];
  console.log("location.state", location.state);
 
  let lot = 126.9779692;
  let lat = 37.566535;

  useEffect(() => {
    if(location.state) {
      console.log("location state is here!", location.state);
      lat = location.state.lat;
      lot = location.state.lot;
      
      positions.current = [{
        // content: '<div>카카오</div>',
        content: `<div>${location.state.title}</div>`,
        latlng: new kakao.maps.LatLng(lat, lot)
      }];

      console.log(positions);

      if(location.sbike) {
        console.log("sbike exists");
        setSbikes([{
          content: `<div>${location.sbike.stationName}</div>`,
          latlng: new kakao.maps.LatLng(location.sbike.stationLatitude, location.sbike.stationLongitude)
        }]);
      }

      makeMap();
    } else {
      console.log("event fetching");
      fetch(`http://localhost:8000/api/v1/event-service/events`)
        .then(response => response.json())
        .then(data => {
          events.current = data;
          // setEvents(data);
        })
        .then(() => {
          console.log(events.current);
          
          events.current.map(event => {
            let position = {
              content: `<div>${event.title}</div>`,
              latlng: new kakao.maps.LatLng(event.lat, event.lot)
            }

            positions.current = [...positions.current, position];
          })
          console.log(positions);
        })
        .then(() => {
          console.log(positions);
          makeMap();
        })
    }
  }, [])

  function makeMap() {
    const container = mapRef.current;
    const options = { //지도를 생성할 때 필요한 기본 옵션
      center: new kakao.maps.LatLng(lat, lot), //지도의 중심좌표.
      level: 6 //지도의 레벨(확대, 축소 정도)
    };
  
    map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
  
    // 지도에 컨트롤을 추가해야 지도위에 표시됩니다
    // kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
    const mapTypeControl = new kakao.maps.MapTypeControl();
    map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

    // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
    const zoomControl = new kakao.maps.ZoomControl();
    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

    // // 마커가 지도 위에 표시되도록 설정합니다
    // marker.setMap(map);

    console.log("마커생성", positions.current);
    for (var i = 0; i < positions.current.length; i ++) {
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
          map: map, // 마커를 표시할 지도
          position: positions.current[i].latlng, // 마커의 위치
      });

      // 마커에 표시할 인포윈도우를 생성합니다 
      var infowindow = new kakao.maps.InfoWindow({
          content: positions.current[i].content // 인포윈도우에 표시할 내용
      });

      // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
      // 이벤트 리스너로는 클로저를 만들어 등록합니다 
      // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
      kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
      kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
    }

    const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
    // 마커 이미지의 이미지 크기 입니다
    const imageSize = new kakao.maps.Size(24, 35);
    // 마커 이미지를 생성합니다    
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
    for (var i = 0; i < sbikes.length; i ++) {
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
        map: map, // 마커를 표시할 지도
        position: sbikes[i].latlng, // 마커의 위치
        image : markerImage
    });
      // 마커에 표시할 인포윈도우를 생성합니다 
      var infowindow = new kakao.maps.InfoWindow({
        content: sbikes[i].content // 인포윈도우에 표시할 내용
      });

      // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
      // 이벤트 리스너로는 클로저를 만들어 등록합니다 
      // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
      kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
      kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
    }

    // 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
    function makeOverListener(map, marker, infowindow) {
      return function() {
          infowindow.open(map, marker);
      };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
    function makeOutListener(infowindow) {
      return function() {
          infowindow.close();
      };
    }
  }

  return (
    <>
      <div
        style={{ height: `600px` }}
        className="map-canvas"
        id="map-canvas"
        ref={mapRef}
      ></div>
    </>
  );
};

const Maps = () => {
  const location = useLocation();
  console.log("location", location);

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
        <Row>
          <div className="col">
          <ButtonGroup>
            <Button color="primary" onClick={() => {
              map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);   
              }}>혼잡도</Button>
            <Button color="primary" onClick={() => {
              map.removeOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC); 
            }}>지도</Button>
          </ButtonGroup>
            <Card className="shadow border-0">   
              <MapWrapper state={location.state}/>
            </Card>
          </div>
        </Row>
      </Container>
    </>
  );
};

export default Maps;
