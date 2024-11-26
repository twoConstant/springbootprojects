// import React, { useEffect, useState } from "react";
// import { getArticle, patchArticleStar } from "../api/articles_api";
// import { useParams, useNavigate } from "react-router-dom";
// import CommentsAtArticle from "./CommentsAtArticle";

// const ArticleDetail = () => {
//     const [article, setArticle] = useState(null);
//     const {article_id} = useParams();
//     const navigate = useNavigate(); // useNavigate 훅 사용

//     // 버튼 클릭 시 ArticleList로 이동
//     const handleGoBack = () => {
//         navigate("/articles"); // 
//     };

//     // 버튼 클릭 시 ArticleEdit로 이동
//     const handleGoArticleEdit = (article_id) => {
//         navigate(`/articles/${article_id}/edit`)
//     }

//     // 버튼 클릭 시 CommentCre로 이동
//     const handleGoCommentCre = (article_id) => {
//         navigate(`/articles/${article_id}/comment-cre`)
//     }
    
//     // 버튼 클릭 시 요청 이동
//     const handleStarsIncrement = (article_id) => {
//         try {
//             patchArticleStar(article_id);
//             setArticle(
//                 (prev) => ({
//                     ...prev,
//                     starCount: prev.starCount + 1,
//                 })
//             );
//         } catch(e) {
//             console.log(e);
//         }
        
//     }



//     useEffect(() => {
//         // ArticleList 컴포넌트 렌더링시 수행할 동작 정의 블럭
//         console.log("useEffect executed for article_id:", article_id);
//         const fetchArticle = async () => {
//             try {
//                 const data = await getArticle(article_id); // API 호출
//                 console.log("Fetched article:", data); // 데이터 확인
//                 setArticle(data); // 상태 업데이트
//             } catch (error) {
//                 console.error("Error fetching article:", error);
//             }
//         };

//         fetchArticle();    // 여기에는 article_id를 안박아도 되나?
//     }, 
//     // article_id가 변경될때 마다 useEffect 실행
//     [article_id]);

//     // article이 갱신이 안됐을때 (초기값이 null)
//     if (!article) {
//         return <p>로딩중...</p>;
//     }

//     return (
//         <div>
//             <h1>{article.title}</h1>
//             <p>작성자: {article.writer}</p>
//             <p>내용: {article.content}</p>
//             <p>조회수: {article.viewCount}, 추천수: {article.starCount}</p>
//             <button onClick={ () => handleGoArticleEdit(article_id)}>수정하기</button>
//             <button onClick={handleGoBack}>돌아가기</button>
//             <button onClick={ () => handleGoCommentCre(article_id)}>댓글 작성하기</button>
//             <button onClick={ () => handleStarsIncrement(article_id)}>추천</button>
//             {/* 댓글 컴포넌트 */}
//             <CommentsAtArticle article_id = {article_id} />
//         </div>
//         );
// };

// export default ArticleDetail;

import React, { useEffect, useState, useRef } from "react";
import { getArticle, patchArticleStar, patchArticleVeiwCount } from "../api/articles_api";
import { useParams, useNavigate } from "react-router-dom";
import CommentsAtArticle from "./CommentsAtArticle";
import "../styles.css";

const ArticleDetail = () => {
    const [article, setArticle] = useState(null);
    const { article_id } = useParams();
    const navigate = useNavigate();
    const hasIncrementedView = useRef(false); // useRef는 컴포넌트 최상위에서 선언

    const handleGoBack = () => navigate("/articles");
    const handleGoArticleEdit = () => navigate(`/articles/${article_id}/edit`);
    const handleGoCommentCre = () => navigate(`/articles/${article_id}/comment-cre`);

    const handleStarsIncrement = async () => {
        try {
            await patchArticleStar(article_id);
            setArticle((prev) => ({
                ...prev,
                starCount: prev.starCount + 1,
            }));
        } catch (e) {
            console.error(e);
        }
    };

    // 게시글 데이터 로드 및 조회수 증가 처리
    useEffect(() => {
        const fetchDataAndIncrementView = async () => {
            try {
                // 게시글 데이터 로드
                const data = await getArticle(article_id);
                setArticle(data);

                // 조회수 증가 (한 번만 실행)
                if (!hasIncrementedView.current) {
                    hasIncrementedView.current = true; // 조회수 증가 처리 완료
                    await patchArticleVeiwCount(article_id);
                }
            } catch (error) {
                console.error("Error in fetching data or incrementing view count:", error);
            }
        };

        fetchDataAndIncrementView();
    }, [article_id]); // article_id가 변경될 때만 실행

    if (!article) return <p>로딩중...</p>;

    return (
        <div className="container">
            <h1>{article.title}</h1>
            <p>작성자: {article.writer}</p>
            <p>내용: {article.content}</p>
            <p>조회수: {article.viewCount}, 추천수: {article.starCount}</p>
            <button className="primary" onClick={handleGoArticleEdit}>수정하기</button>
            <button className="secondary" onClick={handleGoBack}>돌아가기</button>
            <button className="primary" onClick={handleGoCommentCre}>댓글 작성하기</button>
            <button className="primary" onClick={handleStarsIncrement}>추천</button>
            <CommentsAtArticle article_id={article_id} />
        </div>
    );
};

export default ArticleDetail;
