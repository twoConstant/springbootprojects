import React, { useEffect, useState } from "react";
import { getArticlePage } from "../api/articles_api";
import { Link, useNavigate } from "react-router-dom";
import Pagination from "./Pagination";
import "../styles.css";

const ArticleList = () => {
    const [articles, setArticles] = useState([]);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const pageSize = 5;

    const navigate = useNavigate();

    useEffect(() => {
        const fetchArticles = async () => {
            setIsLoading(true);
            try {
                const data = await getArticlePage(currentPage, pageSize);
                setArticles(data.content);
                setTotalPages(data.totalPages);
            } catch (error) {
                setError("게시글을 가져오는 데 실패했습니다.");
            } finally {
                setIsLoading(false);
            }
        };
        fetchArticles();
    }, [currentPage]);

    // 게시글 작성 페이지 이동
    const handleGoArticleCre = () => {
        navigate("/articles/create");
    };

    // 페이지 변경 핸들러
    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
    };

    return (
        <div className="container">
            <h1>게시판</h1>

            {/* 로딩 중일 때 메시지 */}
            {isLoading && <p>로딩 중입니다...</p>}

            {/* 에러 메시지 */}
            {error && <p style={{ color: "red" }}>{error}</p>}

            {/* 게시글 목록 */}
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

            <button onClick={handleGoArticleCre}>게시글 작성하기</button>

            {/* 페이지네이션 컴포넌트 */}
            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={handlePageChange}
            />
        </div>
    );
};

export default ArticleList;
