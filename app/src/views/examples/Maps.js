import React, {useState} from "react";

// reactstrap components
import { Button, ButtonGroup, Card, Container, Row } from "reactstrap";

// core components
import Header from "components/Headers/Header.js";
import { useLocation } from "react-router-dom";

const {kakao} = window;
let map;

const MapWrapper = ({lot, lat}) => {
  console.log(lot, lat);

  const mapRef = React.useRef(null);
  const [positions, setPositions] = useState([{
      content: '<div>카카오</div>', 
      latlng: new kakao.maps.LatLng(37.5499060881738, 126.945533810385)
  },
  {
      content: '<div>생태연못</div>', 
      latlng: new kakao.maps.LatLng(33.450936, 126.569477)
  },
  {
      content: '<div>텃밭</div>', 
      latlng: new kakao.maps.LatLng(33.450879, 126.569940)
  },
  {
      content: '<div>근린공원</div>',
      latlng: new kakao.maps.LatLng(33.451393, 126.570738)
  }]);
  
  React.useEffect(() => {
    const container = mapRef.current;
    const options = { //지도를 생성할 때 필요한 기본 옵션
      center: new kakao.maps.LatLng(lot, lat), //지도의 중심좌표.
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

    for (var i = 0; i < positions.length; i ++) {
      // 마커를 생성합니다
      var marker = new kakao.maps.Marker({
          map: map, // 마커를 표시할 지도
          position: positions[i].latlng // 마커의 위치
      });

      // 마커에 표시할 인포윈도우를 생성합니다 
      var infowindow = new kakao.maps.InfoWindow({
          content: positions[i].content // 인포윈도우에 표시할 내용
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

  }, []);

  

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

  let lot = 37.566535;
  let lat = 126.9779692;
  let isMarked = false;
  
  if(location.state) {
    lot = location.state.lot;
    lat = location.state.lat;
    isMarked = true;
  }

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
              <MapWrapper lot={lot} lat={lat} isMarked={isMarked}/>
            </Card>
          </div>
        </Row>
      </Container>
    </>
  );
};

export default Maps;
