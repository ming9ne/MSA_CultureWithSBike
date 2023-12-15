import { useEffect, useState } from "react";
// node.js library that concatenates classes (strings)
import classnames from "classnames";
// javascipt plugin for creating charts
import Chart from "chart.js";
// react plugin used to create charts
import { Line, Bar } from "react-chartjs-2";
// reactstrap components
import {
  Button,
  Card,
  CardHeader,
  CardBody,
  NavItem,
  NavLink,
  Nav,
  Progress,
  Table,
  Container,
  Row,
  Col,
} from "reactstrap";

// core components
import {
  chartOptions,
  parseOptions,
  chartExample1,
  chartExample2,
} from "variables/charts.js";

import Header from "components/Headers/Header.js";

const Index = (props) => {
  const [activeNav, setActiveNav] = useState(1);
  const [chart, setChart] = useState("data1");
  const [eventData, setEventData] = useState([]);
  const [eventChartData, setEventChartData] = useState({data1: { datasets:[], labels:[] }, data2: { datasets:[], labels:[] }});
  const [eventAreaData, setEventAreaData] = useState([]);
  const [eventAreaStatistics, setEventAreaStatistics] = useState([]);
  const [couponData, setCouponData] = useState([]);
  const [couponChartData, setCouponChartData] = useState({ datasets:[], labels:[] });
  const [populations, setPopulations] = useState([]);
  const [populationData, setPopulationData] = useState([]);
  

  if (window.Chart) {
    parseOptions(Chart, chartOptions());
  }

  const toggleNavs = (e, index) => {
    e.preventDefault();
    setActiveNav(index);
    setChart("data"+index);
  };

  useEffect(() => {
    console.log("env", process.env.REACT_APP_GATEWAY)

    fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/event-service/statistics`)
      .then(response => response.json())
      .then(data => {
        setEventData(data);
      })
      .catch(e => console.log(e))

    fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/coupon-service/statistics`)
      .then(response => response.json())
      .then(data => {
        setCouponData(data);
      })
      .catch(e => console.log(e))

    fetch(`http://${process.env.REACT_APP_GATEWAY}/api/v1/congestion-service/populations`)
      .then(response => response.json())
      .then(data => {
        setPopulations(data.sort(function(a, b) {
          return b.areaPpltnMax - a.areaPpltnMax;
        }));
      })
      .catch(e => console.log(e))     
  }, [])

  // 문화행사 데이터
  useEffect(() => {
    if(eventData["Monthly Event"]) {
      let labels1 = [];
      let datas1 = [];

      let labels2 = [];
      let datas2 = [];

      Object.keys(eventData["Monthly Event"]).map(data => {
        labels1 = [...labels1, data];
        datas1 = [...datas1, eventData["Monthly Event"][data]];
      })

      Object.keys(eventData["Weekly Event"]).map(data => {
        labels2 = [...labels2, data];
        datas2 = [...datas2, eventData["Weekly Event"][data]];
      })

      console.log("data1", datas1);
      console.log("data2", datas2);
      
      let data = {
        data1: (canvas) => {
          return {
            labels: labels1,
            datasets: [
              {
                label: "Performance",
                data: datas1,
              },
            ],
          };
        },
        data2: (canvas) => {
          return {
            labels: labels2,
            datasets: [
              {
                label: "Performance",
                data: datas2,
              },
            ],
          };
        }
      };
      
      console.log(data);

      setEventChartData(data);

      let areaData = [];
      let keys = Object.keys(eventData["areas"])

      for(let i = 0; i < keys.length; i++) {
        areaData = [...areaData, {areaNm: keys[i], count: eventData["areas"][keys[i]]}];
      }

      setEventAreaData(areaData.sort(function(a, b) {
        return b.count - a.count;
      }));
    }
  }, [eventData])

  // 문화행사 차트
  useEffect(() => {
    if(eventData["Monthly Event"]) {
      let labels1 = [];
      let datas1 = [];

      let labels2 = [];
      let datas2 = [];

      Object.keys(eventData["Monthly Event"]).map(data => {
        labels1 = [...labels1, data];
        datas1 = [...datas1, eventData["Monthly Event"][data]];
      })

      Object.keys(eventData["Weekly Event"]).map(data => {
        labels2 = [...labels2, data];
        datas2 = [...datas2, eventData["Weekly Event"][data]];
      })

      let data = {
        data1: (canvas) => {
          return {
            labels: labels1,
            datasets: [
              {
                label: "Performance",
                data: datas1,
              },
            ],
          };
        },
        data2: (canvas) => {
          return {
            labels: labels2,
            datasets: [
              {
                label: "Performance",
                data: datas2,
              },
            ],
          };
        }
      };
      setEventChartData(data);
    }
  }, [chart])

  // 문화행사 데이터
  useEffect(() => {
    setEventAreaStatistics(eventAreaData.slice(0, 5))
  }, [eventAreaData])

  // 쿠폰 데이터
  useEffect(() => {
    let labels = [];
    let datas = [];
    Object.keys(couponData).reverse().map(data => {
      labels = [...labels, data];
      datas = [...datas, couponData[data]];
    })

    setCouponChartData({
      labels, 
      datasets: [
        {
          label: "Sales",
          data: datas,
          maxBarThickness: 10,
        },
      ],
    })
  }, [couponData])

  // 인구 데이터
  useEffect(() => {
    setPopulationData(populations.slice(0, 5));
  }, [populations])

  return (
    <>
      <Header />
      {/* Page content */}
      <Container className="mt--7" fluid>
      <Row>
          <Col className="mb-5 mb-xl-0" xl="8">
            <Card className="bg-gradient-default shadow">
              <CardHeader className="bg-transparent">
                <Row className="align-items-center">
                  <div className="col">
                    <h6 className="text-uppercase text-light ls-1 mb-1">
                      Overview
                    </h6>
                    <h2 className="text-white mb-0">문화행사</h2>
                  </div>
                  <div className="col">
                    <Nav className="justify-content-end" pills>
                      <NavItem>
                        <NavLink
                          className={classnames("py-2 px-3", {
                            active: activeNav === 1,
                          })}
                          href="#pablo"
                          onClick={(e) => toggleNavs(e, 1)}
                        >
                          <span className="d-none d-md-block">Month</span>
                          <span className="d-md-none">M</span>
                        </NavLink>
                      </NavItem>
                      <NavItem>
                        <NavLink
                          className={classnames("py-2 px-3", {
                            active: activeNav === 2,
                          })}
                          data-toggle="tab"
                          href="#pablo"
                          onClick={(e) => toggleNavs(e, 2)}
                        >
                          <span className="d-none d-md-block">Week</span>
                          <span className="d-md-none">W</span>
                        </NavLink>
                      </NavItem>
                    </Nav>
                  </div>
                </Row>
              </CardHeader>
              <CardBody>
                {/* Chart */}
                <div className="chart">
                  <Line
                    // data={chartExample1[chart]}
                    data={eventChartData[chart]}
                    options={chartExample1.options}
                    getDatasetAtEvent={(e) => console.log(e)}
                  />
                </div> : null
              </CardBody>
            </Card>
          </Col>
          <Col xl="4">
            <Card className="shadow">
              <CardHeader className="bg-transparent">
                <Row className="align-items-center">
                  <div className="col">
                    <h6 className="text-uppercase text-muted ls-1 mb-1">
                      Performance
                    </h6>
                    <h2 className="mb-0">Daily Issued Coupons</h2>
                  </div>
                </Row>
              </CardHeader>
              <CardBody>
                {/* Chart */}
                <div className="chart">
                  <Bar
                    data={couponChartData}
                    options={chartExample2.options}
                  />
                </div>
              </CardBody>
            </Card>
          </Col>
        </Row>
        <Row className="mt-5">
          <Col className="mb-5 mb-xl-0" xl="8">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row className="align-items-center">
                  <div className="col">
                    <h3 className="mb-0">문화행사 열리는 지역</h3>
                  </div>
                  <div className="col text-right">
                    <Button
                      color="primary"
                      href="#pablo"
                      onClick={(e) => {
                        e.preventDefault();
                        const reversedEventAreas = [...eventAreaData].reverse();
                        setEventAreaData(reversedEventAreas);
                      }}
                      size="sm"
                    >
                      정렬 기준 변경
                    </Button>
                  </div>
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th scope="col">문화행사 명</th>
                    <th scope="col">개최수</th>
                  </tr>
                </thead>
                <tbody>
                  {eventAreaStatistics.map((area) => {
                    return (
                      <tr key={area.areaNm}>
                        <th scope="row">{area.areaNm}</th>
                        <td>{area.count}</td>
                      </tr>
                    )
                  })}
                </tbody>
              </Table>
            </Card>
          </Col>
          <Col xl="4">
            <Card className="shadow">
              <CardHeader className="border-0">
                <Row className="align-items-center">
                  <div className="col">
                    <h3 className="mb-0">인구 혼잡도</h3>
                  </div>
                  <div className="col text-right">
                    <Button
                      color="primary"
                      href="#pablo"
                      onClick={(e) => {
                        e.preventDefault()
                        const reversedPopulations = [...populations].reverse();
                        setPopulations(reversedPopulations);
                      }}
                      size="sm"
                    >
                      정렬 기준 변경
                    </Button>
                  </div>
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th scope="col">Area</th>
                    <th scope="col">Visitors</th>
                    <th scope="col" />
                  </tr>
                </thead>
                <tbody>
                {populationData.map(population => {
                  return (
                    <tr key={population.areaNm}>
                      <th scope="row">{population.areaNm}</th>
                      <td>{population.areaPpltnMax}</td>
                      <td>
                        <div className="d-flex align-items-center">
                          <span className="mr-2">{population.areaPpltnMax / 100000 * 100}%</span>
                          <div>
                            <Progress
                              max="100"
                              value={population.areaPpltnMax / 100000 * 100}
                              barClassName="bg-gradient-danger"
                            />
                          </div>
                        </div>
                      </td>
                    </tr>)
                })}
                </tbody>
              </Table>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Index;
