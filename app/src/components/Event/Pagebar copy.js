import React from "react";
import { Link } from "react-router-dom";
import "./Pagebar.css";
function Pagebar({ totalCount, page, perPage = 1 }) {
    const PER_PAGE = perPage;
    const totalPage = Math.ceil(totalCount / PER_PAGE);
    page = page % 10 === 0 ? page - 1 : page;
    console.log("page", page);

    const pages = Array.from(
        Array(perPage),
        (_, i) => Math.floor(page / 10) * 10 + i + 1
    );

    let prev = pages[0] - 1 === 0 ? 1 : pages[0] - 1;
    let next = pages[perPage - 1] + 1;
    console.log("pages.length", pages.length)

    pages.splice(0, 0, 1);
    pages.splice(1, 0, prev);
    pages.splice(pages.length, 0, totalPage);
    pages.splice(pages.length - 1, 0, next);

    console.log(pages);
    return (
        <div className="pagingBar">
            {pages.map((p, index) => {
                let display = p;
                if (index === 0) {
                    display = "first";
                } else if (index === 1) {
                    display = "prev";
                } else if (index === pages.length - 2) {
                    display = "next";
                } else if (index === pages.length - 1) {
                    display = "last";
                }
                console.log("p", p)
                return (
                    <span className="page_number" key={index}>
                        <Link to={`/admin/lists/${p}`}>{display}</Link>
                    </span>
                );
            })}
        </div>
    );
}

export default Pagebar;
