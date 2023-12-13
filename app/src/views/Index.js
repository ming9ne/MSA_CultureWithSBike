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
  const [chart, setChart] = useState("Data1");
  const [eventData, setEventData] = useState([]);
  const [eventChartData, setEventChartData] = useState({data1: { datasets:[], labels:[] }, data2: { datasets:[], labels:[] }});
  const [eventAreaData, setEventAreaData] = useState([]);
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
    setChart("Data"+index);
  };

  useEffect(() => {
    fetch(`http://localhost:8000/api/v1/event-service/statistics`)
      .then(response => response.json())
      .then(data => {
        setEventData(data);
      })
      .catch(e => console.log(e))

    fetch(`http://localhost:8000/api/v1/coupon-service/statistics`)
      .then(response => response.json())
      .then(data => {
        setCouponData(data);
      })
      .catch(e => console.log(e))

    fetch(`http://localhost:8000/api/v1/congestion-service/populations`)
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

      setEventChartData(
        {
          Data1: {
            labels: labels1, 
            datasets: [
              {
                label: "Performance",
                data: datas1,
              },
            ],
          },
          Data2: {
          labels: labels2, 
          datasets: [
            {
              label: "Performance",
              data: datas2,
            },
          ]}
        }
      )

      let keys = Object.keys(eventData["areas"])
      console.log(keys);
      console.log(eventData["areas"]);
      // console.log(eventData["areas"].slice(0, 5));
      setEventAreaData(eventData["areas"]);

      for(let i = 0; i < 5; i++) {
        setEventAreaData(...eventAreaData, eventData["areas"][keys[i]]);
        console.log(eventData["areas"][keys[i]]);
      }
    }
  }, [eventData])

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
    // console.log(populationData);
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
                    // data={chartExample1[chartExample1Data]}
                    data={eventChartData[chart]}
                    options={chartExample1.options}
                    getDatasetAtEvent={(e) => console.log(e)}
                  />
                </div>
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
                      onClick={(e) => e.preventDefault()}
                      size="sm"
                    >
                      See all
                    </Button>
                  </div>
                </Row>
              </CardHeader>
              <Table className="align-items-center table-flush" responsive>
                <thead className="thead-light">
                  <tr>
                    <th scope="col">Page name</th>
                    <th scope="col">Visitors</th>
                  </tr>
                </thead>
                <tbody>
                  {eventAreaData.map(area => {
                    <tr>
                    <th scope="row">/argon/</th>
                    <td>4,569</td>
                    <td>340</td>
                    <td>
                      <i className="fas fa-arrow-up text-success mr-3" /> 46,53%
                    </td>
                  </tr>
                  })}
                  
                  <tr>
                    <th scope="row">/argon/index.html</th>
                    <td>3,985</td>
                    <td>319</td>
                    <td>
                      <i className="fas fa-arrow-down text-warning mr-3" />{" "}
                      46,53%
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">/argon/charts.html</th>
                    <td>3,513</td>
                    <td>294</td>
                    <td>
                      <i className="fas fa-arrow-down text-warning mr-3" />{" "}
                      36,49%
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">/argon/tables.html</th>
                    <td>2,050</td>
                    <td>147</td>
                    <td>
                      <i className="fas fa-arrow-up text-success mr-3" /> 50,87%
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">/argon/profile.html</th>
                    <td>1,795</td>
                    <td>190</td>
                    <td>
                      <i className="fas fa-arrow-down text-danger mr-3" />{" "}
                      46,53%
                    </td>
                  </tr>
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
