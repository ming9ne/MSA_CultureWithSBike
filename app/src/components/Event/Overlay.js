import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText } from "reactstrap";

function Overlay(props){
    const [isOpen, setIsOpen] = useState(false)
  
    const markerPosition = {
      lat: 33.450701,
      lng: 126.570667,
    }
  
    return (
      <>
        <RemovableCustomOverlayStyle />
        <Map // 지도를 표시할 Container
          id={`map`}
          center={{
            // 지도의 중심좌표
            lat: 33.451475,
            lng: 126.570528,
          }}
          style={{
            // 지도의 크기
            width: "100%",
            height: "450px",
          }}
          level={3} // 지도의 확대 레벨
        >
          <MapMarker position={markerPosition} onClick={() => setIsOpen(true)} />
          {isOpen && (
            <CustomOverlayMap position={markerPosition}>
              <div className="wrap">
                <div className="info">
                  <div className="title">
                    카카오 스페이스닷원
                    <div
                      className="close"
                      onClick={() => setIsOpen(false)}
                      title="닫기"
                    ></div>
                  </div>
                  <div className="body">
                    <div className="img">
                      <img
                        src="//t1.daumcdn.net/thumb/C84x76/?fname=http://t1.daumcdn.net/cfile/2170353A51B82DE005"
                        width="73"
                        height="70"
                        alt="카카오 스페이스닷원"
                      />
                    </div>
                    <div className="desc">
                      <div className="ellipsis">
                        제주특별자치도 제주시 첨단로 242
                      </div>
                      <div className="jibun ellipsis">
                        (우) 63309 (지번) 영평동 2181
                      </div>
                      <div>
                        <a
                          href="https://www.kakaocorp.com/main"
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
              ;
            </CustomOverlayMap>
          )}
        </Map>
      </>
    )
  }

  export default Overlay;