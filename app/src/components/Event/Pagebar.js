// ListPagenation.js
import React from "react";

const Pagebar = ({
  limit,
  page,
  setPage,
  blockNum,
  setBlockNum,
  counts,
}) => {
    console.log(counts)
  const createArr = (n) => {
    const iArr = new Array(n);
    for (let i = 0; i < n; i++) iArr[i] = i + 1;
    return iArr;
  };

  const pageLimit = 10;

  const totalPage = Math.ceil(counts / limit);
  const blockArea = Number(blockNum * pageLimit);
  const nArr = createArr(Number(totalPage));
  let pArr = nArr?.slice(blockArea, Number(pageLimit) + blockArea);

  const firstPage = () => {
    setPage(1);
    setBlockNum(0);
  };

  const lastPage = () => {
    setPage(totalPage);
    setBlockNum(Math.ceil(totalPage / pageLimit) - 1);
  };

  const prevPage = () => {
    if (page <= 1) {
      return;
    }
    if (page - 1 <= pageLimit * blockNum) {
      setBlockNum((n) => n - 1);
    }
    setPage((n) => n - 1);
  };

  const nextPage = () => {
    if (page >= totalPage) {
      return;
    }
    if (pageLimit * Number(blockNum + 1) < Number(page + 1)) {
      setBlockNum((n) => n + 1);
    }
    setPage((n) => n + 1);
  };

  return (
    <div className="ListPagenationWrapper">
      <button
        className="moveToFirstPage"
        onClick={() => {
          firstPage();
        }}
      >
        &lt;&lt;
      </button>
      <button
        className="moveToPreviousPage"
        onClick={() => {
          prevPage();
        }}
        disabled={page === 1}
      >
        &lt;
      </button>
      <div className="pageBtnWrapper">
        {pArr.map((n) => (
          <button
            className="pageBtn"
            key={n}
            onClick={() => {
              setPage(n);
            }}
            aria-current={page === n ? "page" : undefined}
          >
            {n}
          </button>
        ))}
      </div>
      <button
        className="moveToNextPage"
        onClick={() => {
          nextPage();
        }}
        disabled={page === totalPage}
      >
        &gt;
      </button>
      <button
        className="moveToLastPage"
        onClick={() => {
          lastPage();
        }}
      >
        &gt;&gt;
      </button>

      <style jsx>
        {`
          .ListPagenationWrapper {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            height: 37px;
            margin: 38px 94px 38px 88px;
          }

          .moveToPreviousPage,
          .moveToNextPage {
            color: #5a5a5a;
            background-color: transparent;
            border: none;
            font-size: 25px;
            cursor: pointer;
          }

          .moveToFirstPage,
          .moveToLastPage {
            width: 115px;
            height: 37px;
            margin: 0 0 0 0;
            border: none;
            color: black;
            background-color: transparent;
            cursor: pointer;
          }

          .pageBtn {
            width: 49px;
            height: 49px;
            margin: 0 10px;
            border: none;
            color: black;
            font-size: 20px;
            opacity: 0.2;

            &:hover {
              background-color: #b42954;
              cursor: pointer;
              transform: translateY(-2px);
            }

            &[disabled] {
              background-color: #e2e2e2;
              cursor: revert;
              transform: revert;
            }

            &[aria-current] {
              background-color: #f5d3dd;
              font-weight: bold;
              cursor: revert;
              transform: revert;
              opacity: 1;
            }
          }
        `}
      </style>
    </div>
  );
};

export default Pagebar;
