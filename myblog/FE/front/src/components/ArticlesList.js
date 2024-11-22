// import React, { useEffect, useState } from "react";
// import { getArticleSummaryResDto } from "../api/articles_api";
// import { Link } from "react-router-dom";

// const ArticleList = () => {
//     // 상태값 정의
//     const [articles, setArticles] = useState([]); // 초기값을 빈 배열로 설정
//     const [error, setError] = useState(null); // 에러 상태
//     const [isLoading, setIsLoading] = useState(true); // 로딩 상태

//     useEffect(() => {
//         const fetchArticles = async () => {
//             try {
//                 const data = await getArticleSummaryResDto(); // API 호출
//                 console.log("Fetched articles:", data); // 데이터 확인
//                 setArticles(data); // 상태 업데이트
//             } catch (error) {
//                 console.error("Error fetching articles:", error);
//                 setError("게시글을 가져오는 데 실패했습니다."); // 에러 메시지 설정
//             } finally {
//                 setIsLoading(false); // 로딩 상태 종료
//             }
//         };

//         fetchArticles();
//     }, []); // 컴포넌트 렌더링 시 한 번 실행

//     return (
//         <div>
//             <h1>게시판</h1>
//             {/* 로딩 중일 때 메시지 */}
//             {isLoading && <p>로딩 중입니다...</p>}

//             {/* 에러 메시지 */}
//             {error && <p style={{ color: "red" }}>{error}</p>}

//             {/* 게시글 목록 렌더링 */}
//             {articles && articles.length > 0 ? (
//                 articles.map((article) => (
//                     <div key={article.id}>
//                         <Link to={`/articles/${article.id}`}>
//                             <h6>제목 : {article.title}</h6>
//                         </Link>
//                         <p>
//                             작성자: {article.writer}, 조회수: {article.viewCount}, 추천수: {article.starCount}
//                         </p>
//                     </div>
//                 ))
//             ) : (
//                 // 데이터가 없고 에러가 없는 경우 메시지 출력
//                 !isLoading && !error && <p>게시글이 없습니다.</p>
//             )}
//         </div>
//     );
// };

// export default ArticleList;

import React, { useEffect, useState } from "react";
import { getArticleSummaryResDto } from "../api/articles_api";
import { Link } from "react-router-dom";
import "../styles.css"; // CSS 파일 import

const ArticleList = () => {
    const [articles, setArticles] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchArticles = async () => {
            try {
                const data = await getArticleSummaryResDto();
                setArticles(data);
            } catch (error) {
                setError("게시글을 가져오는 데 실패했습니다.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchArticles();
    }, []);

    return (
        <div className="container">
            <h1>게시판</h1>
            {isLoading && <p>로딩 중입니다...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}
            {articles && articles.length > 0 ? (
                articles.map((article) => (
                    <div key={article.id} className="article-item">
                        <Link to={`/articles/${article.id}`}>
                            <h6>제목 : {article.title}</h6>
                        </Link>
                        <p>작성자: {article.writer}, 조회수: {article.viewCount}, 추천수: {article.starCount}</p>
                    </div>
                ))
            ) : (
                !isLoading && !error && <p>게시글이 없습니다.</p>
            )}
        </div>
    );
};

export default ArticleList;
