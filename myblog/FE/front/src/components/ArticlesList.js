import React, { useEffect, useState } from "react";
import { getArticleSummaryResDto } from "../api/articles_api";
import { Link } from "react-router-dom";

const ArticleList = () => {
    const [articles, setArticles] = useState([]);



    useEffect(() => {
        // ArticleList 컴포넌트 렌더링시 수행할 동작 정의 블럭
        const fetchArticles = async () => {
            try {
                const data = await getArticleSummaryResDto(); // API 호출
                console.log("Fetched articles:", data); // 데이터 확인
                setArticles(data); // 상태 업데이트
            } catch (error) {
                console.error("Error fetching articles:", error);
            }
        };

        fetchArticles();
    }, 
    // 의존성 서술 부분 빈배열시 렌더링시 1회만 수행된다는 의미
    []);

    // ArticleList의 return
    return (
        <div>
            <h1>Article List</h1>
            {articles.map((article) => (
                <div key={article.id}>
                    <Link to={`/articles/${article.id}`}>
                        <h6>제목 : {article.title}</h6>
                    </Link>
                    <p>작성자: {article.writer}, 조회수: {article.viewCount}, 추천수: {article.starCount}</p>
                </div>
                ))
            }
        </div>
        );
};

export default ArticleList;