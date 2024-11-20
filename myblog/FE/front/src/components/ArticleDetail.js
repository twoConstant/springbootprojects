import React, { useEffect, useState } from "react";
import { getArticleDetailResDto } from "../api/articles_api";
import { useParams, useNavigate } from "react-router-dom";

const ArticleDetail = () => {
    const [article, setArticle] = useState(null);
    const {article_id} = useParams();
    const navigate = useNavigate(); // useNavigate 훅 사용

    // 버튼 클릭 시 ArticleList로 이동
    const handleGoBack = () => {
        navigate("/articles"); // 
    };

    // 버튼 클릭 시 ArticleEdit로 이동
    const handleGoArticleEdit = (article_id) => {
        navigate(`/articles/${article_id}/edit`)
    }



    useEffect(() => {
        // ArticleList 컴포넌트 렌더링시 수행할 동작 정의 블럭
        console.log("useEffect executed for article_id:", article_id);
        const fetchArticle = async () => {
            try {
                const data = await getArticleDetailResDto(article_id); // API 호출
                console.log("Fetched article:", data); // 데이터 확인
                setArticle(data); // 상태 업데이트
            } catch (error) {
                console.error("Error fetching article:", error);
            }
        };

        fetchArticle();    // 여기에는 article_id를 안박아도 되나?
    }, 
    // article_id가 변경될때 마다 useEffect 실행
    [article_id]);

    // article이 갱신이 안됐을때 (초기값이 null)
    if (!article) {
        return <p>로딩중...</p>;
    }

    return (
        <div>
            <h1>{article.title}</h1>
            <p>작성자: {article.writer}</p>
            <p>내용: {article.content}</p>
            <p>조회수: {article.viewCount}, 추천수: {article.starCount}</p>
            <button onClick={ () => handleGoArticleEdit(article_id)}>수정하기</button>
            <button onClick={handleGoBack}>돌아가기</button>
        </div>
        );
};

export default ArticleDetail;

