import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText } from "reactstrap";

function Event({title, startDate, endDate, areaNm, codename, guname, place, useFee, player, program, orgLink, mainImg, lot, lat}) { // () 안에 정보들
    
    const[congestion, setCongestion] = useState([]);
    const[population, setPopulation] = useState([]);


    useEffect(() => {
        fetch(`http://localhost:8000/api/v1/congestion-service/congestion/${areaNm}`)
            .then(response => response.json())
            .then(response => { 
                setCongestion(response);
                // console.log(response);
            }
        )

        fetch(`http://localhost:8000/api/v1/congestion-service/population/${areaNm}`)
            .then(response => response.json())
            .then(response => {
                setPopulation(response);
                // console.log(response);
            })
    }, [])

    return (
        <Card
            style={{
                margin: "30px"
            }}>
            <CardHeader tag="h3">
                <Link to="/admin/maps" state={{lot, lat}}>{title}</Link>
            </CardHeader>
            <CardBody>
                <CardImg 
                    src={mainImg} 
                    alt={title} 
                    title={title} 
                    style={{
                        height: 300
                    }}
                    top
                    width="100%"
                />
                <CardText>
                    기간 : {startDate} ~ {endDate} <br />
                    지역 : {guname} <br />
                    장소 : {place} <br />
                    이용요금 : {useFee} <br />
                    출연자정보 : {player} <br />
                    프로그램소개 : {program}<br />
                    장소 혼잡도 지표 : {congestion.areaCongestLvl}<br />
                    장소 혼잡도 지표 관련 메세지 : {congestion.areaCongestMsg}<br />
                    인구 수 : {population.areaPpltnMin} ~ {population.areaPpltnMax}(만 명)<br />
                </CardText>
            </CardBody>
            <CardFooter>
                <a
                    className="font-weight-bold ml-1"
                    href={orgLink}
                    rel="noopener noreferrer"
                    target="_blank"
                >홈페이지</a>
            </CardFooter>
        </Card>

        // <section className="">
        //     <div className="movie data">
        //         <Link
        //             to="/detail-movie"
        //             state={{ year, title, summary, poster, genres }}
        //         >
        //             <img src={poster} alt={title} title={title} />
        //         </Link>
        //         <h3 className="movie__title">{title}</h3>
        //         <h5 className="movie__year">{year}</h5>
        //         <ul className="movie__genres">
        //             {genres.map((genre, index) => {
        //                 return (
        //                     <li key={index} className="movie__genre">
        //                         {genre}
        //                     </li>
        //                 );
        //             })}
        //         </ul>
        //         <p className="movie__summary">{summary.slice(0, 180)}...</p>
        //     </div>
        // </section>
    );
}

// Event.propTypes = {
//     year: PropTypes.number.isRequired,
//     title: PropTypes.string.isRequired,
//     summary: PropTypes.string.isRequired,
//     poster: PropTypes.string.isRequired,
//     genres: PropTypes.arrayOf(PropTypes.string).isRequired,
//     title
//     startDate
//     endDate
//     areaNo
//     codeName
//     guname
//     place
//     useFee
//     player
//     program
//     orgLink
//     mainImg
//     lot
//     lat
// };

export default Event;
