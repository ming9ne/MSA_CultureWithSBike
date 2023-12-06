import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { Card, CardHeader, CardImg, CardTitle, CardFooter, CardBody, CardText } from "reactstrap";

function Event(props) { // () 안에 정보들
    // {title, startDate, endDate, areaNm, codename, guname, place, useFee, player, program, orgLink, mainImg, lot, lat}
    return (
        <Card
            style={{
                margin: "30px",
                width: "300px"
            }}>
            <CardHeader tag="h3">
                {/* <Link to="/admin/maps" state={{lot, lat}}>{title}</Link> */}
                <Link to="/admin/details" state={props}>{props.title}</Link>
            </CardHeader>
            <CardBody>
                <CardImg 
                    src={props.mainImg} 
                    alt={props.title}
                    title={props.title} 
                    style={{
                        height: 300
                    }}
                    top
                    width="100%"
                />
            </CardBody>
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
