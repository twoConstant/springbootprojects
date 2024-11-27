// import React, { useEffect, useState, useRef } from "react";
// import { getArticle, patchArticleStar, patchArticleVeiwCount } from "../api/articles_api";
// import { useParams, useNavigate } from "react-router-dom";
// import CommentsAtArticle from "./CommentsAtArticle";
// import "../styles.css";

// const ArticleDetail = () => {
//     const [article, setArticle] = useState(null);
//     const { article_id } = useParams();
//     const navigate = useNavigate();
//     const hasIncrementedView = useRef(false); // useRef는 컴포넌트 최상위에서 선언

//     const handleGoBack = () => navigate("/articles");
//     const handleGoArticleEdit = () => navigate(`/articles/${article_id}/edit`);
//     const handleGoCommentCre = () => navigate(`/articles/${article_id}/comment-cre`);

//     const handleStarsIncrement = async () => {
//         try {
//             await patchArticleStar(article_id);
//             setArticle((prev) => ({
//                 ...prev,
//                 starCount: prev.starCount + 1,
//             }));
//         } catch (e) {
//             console.error(e);
//         }
//     };

//     // 게시글 데이터 로드 및 조회수 증가 처리
//     useEffect(() => {
//         const fetchDataAndIncrementView = async () => {
//             try {
//                 // 게시글 데이터 로드
//                 const data = await getArticle(article_id);
//                 setArticle(data);

//                 // 조회수 증가 (한 번만 실행)
//                 if (!hasIncrementedView.current) {
//                     hasIncrementedView.current = true; // 조회수 증가 처리 완료
//                     await patchArticleVeiwCount(article_id);
//                 }
//             } catch (error) {
//                 console.error("Error in fetching data or incrementing view count:", error);
//             }
//         };

//         fetchDataAndIncrementView();
//     }, [article_id]); // article_id가 변경될 때만 실행

//     if (!article) return <p>로딩중...</p>;

//     return (
//         <div className="container">
//             <h1>{article.title}</h1>
//             <p>작성자: {article.writer}</p>
//             <p>내용: {article.content}</p>
//             <p>조회수: {article.viewCount}, 추천수: {article.starCount}</p>
//             <button className="primary" onClick={handleGoArticleEdit}>수정하기</button>
//             <button className="secondary" onClick={handleGoBack}>돌아가기</button>
//             <button className="primary" onClick={handleGoCommentCre}>댓글 작성하기</button>
//             <button className="primary" onClick={handleStarsIncrement}>추천</button>
//             <CommentsAtArticle article_id={article_id} />
//         </div>
//     );
// };

// export default ArticleDetail;


import React, { useEffect, useState, useRef } from "react";
import { getArticle, patchArticleStar, patchArticleVeiwCount } from "../api/articles_api";
import { useParams, useNavigate } from "react-router-dom";
import CommentsAtArticle from "./CommentsAtArticle";
import { QuillDeltaToHtmlConverter } from "quill-delta-to-html"; // Delta to HTML 변환기
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

    // Delta JSON 데이터를 HTML로 변환
    const deltaOps = JSON.parse(article.content);
    const converter = new QuillDeltaToHtmlConverter(deltaOps.ops, {});
    const contentHtml = converter.convert();

    return (
        <div className="container">
            <h1>{article.title}</h1>
            <p>작성자: {article.writer}</p>
            <div
                className="article-content"
                dangerouslySetInnerHTML={{ __html: contentHtml }}
            /> {/* 변환된 HTML을 출력 */}
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
