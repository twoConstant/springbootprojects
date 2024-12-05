import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ReactQuill from "react-quill"; // ReactQuill 라이브러리
import "quill/dist/quill.snow.css"; // Quill 테마 CSS
import { getArticle, putArticle } from "../api/articles_api";
import ArticleEditReqDto from "../dto/article/request/ArticleEditReqDto";
import "../csses/articleEdit.css"; // CSS 파일 import

const ArticleEdit = () => {
    const navigate = useNavigate();
    const { article_id } = useParams();

    const [articleDetailResDto, setArticleDetailResDto] = useState(null); // 원본 데이터 유지
    const [editorContent, setEditorContent] = useState(""); // Quill 에디터 데이터
    const [passwordValid, setPasswordValid] = useState(""); // 비밀번호 유효성
    const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 관리

    // 초기 렌더링 시 데이터 로드
    useEffect(() => {
        const fetchArticle = async () => {
            try {
                const articleDetailResDto = await getArticle(article_id);
                setArticleDetailResDto(articleDetailResDto);

                // Delta JSON 데이터를 Quill 에디터에 반영
                const initialContent = JSON.parse(articleDetailResDto.content);
                setEditorContent(initialContent); // Delta JSON 그대로 설정
            } catch (error) {
                console.error("Error fetching article:", error);
                setErrorMessage("Failed to load article data.");
            }
        };

        fetchArticle();
    }, [article_id]);

    // Quill 에디터에서 내용 변경 처리
    const handleContentChange = (content, delta, source, editor) => {
        setEditorContent(editor.getContents()); // Delta JSON으로 저장
    };

    // 입력 필드 변경 처리
    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "password_valid") {
            setPasswordValid(value);
        } else {
            setArticleDetailResDto((prev) => ({
                ...prev,
                [name]: value,
            }));
        }
    };

    // 수정 요청 처리
    const handleSubmit = async (e) => {
        e.preventDefault();

        // 간단한 유효성 검사
        if (
            !articleDetailResDto.title ||
            !editorContent ||
            !articleDetailResDto.password ||
            !passwordValid
        ) {
            setErrorMessage("All fields are required!");
            return;
        }

        // DTO 생성 (Delta JSON 전송)
        const articleEditReqDto = new ArticleEditReqDto(
            articleDetailResDto.title,
            articleDetailResDto.writer,
            JSON.stringify(editorContent), // Delta JSON 데이터를 문자열로 전송
            articleDetailResDto.password,
            passwordValid
        );

        try {
            await putArticle(article_id, articleEditReqDto);
            navigate(`/articles/${article_id}`);
        } catch (error) {
            console.error("Article 수정 실패:", error);
            setErrorMessage("Failed to update the article.");
        }
    };

    const handleGoBack = () => {
        navigate(`/articles/${article_id}`);
    };

    if (!articleDetailResDto) return <p>로딩 중...</p>;

    return (
        <div className="container">
            <h1>수정 페이지</h1>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">제목:</label>
                    <input
                        id="title"
                        name="title"
                        value={articleDetailResDto.title}
                        onChange={handleChange}
                        placeholder="Enter Title"
                    />
                </div>
                <div>
                    <label htmlFor="writer">작성자:</label>
                    <input
                        id="writer"
                        name="writer"
                        value={articleDetailResDto.writer}
                        onChange={handleChange}
                        placeholder="Enter Writer"
                    />
                </div>
                <div>
                    <label htmlFor="content">내용:</label>
                    <ReactQuill
                        theme="snow"
                        value={editorContent} // Delta JSON 데이터를 Quill로 설정
                        onChange={handleContentChange} // Quill 내용 변경 처리
                        className="quill-container"
                    />
                </div>
                <div>
                    <label htmlFor="password">새 비밀번호:</label>
                    <input
                        id="password"
                        name="password"
                        value={articleDetailResDto.password}
                        onChange={handleChange}
                        placeholder="Enter New Password"
                    />
                </div>
                <div>
                    <label htmlFor="password_valid">기존 비밀번호 (Validation):</label>
                    <input
                        id="password_valid"
                        name="password_valid"
                        value={passwordValid}
                        onChange={handleChange}
                        placeholder="Enter Validation Password"
                    />
                </div>
                <div>
                    <button type="submit">저장하기</button>
                    <button type="button" onClick={handleGoBack}>
                        돌아가기
                    </button>
                </div>
            </form>
        </div>
    );
};

export default ArticleEdit;
