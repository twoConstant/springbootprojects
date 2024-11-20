import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getArticleDetailResDto, putArticle } from "../api/articles_api"; // API 호출 함수
import ArticleEditReqDto from "../dto/article/request/ArticleEditReqDto"


const ArticleEdit = () => {
    const navigate = useNavigate();
    const { article_id } = useParams();


    const [article, setArticle] = useState({
        title: "",
        writer: "",
        content: "",
        password: "",
    });
    const [passwordValid, setPasswordValid] = useState(""); // 별도 상태
    const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 관리

    // 초기 렌더링 시 데이터 로드
    useEffect(() => {
        const fetchArticle = async () => {
            try {
                const data = await getArticleDetailResDto(article_id);
                setArticle(data);
            } catch (error) {
                console.error("Error fetching article:", error);
                setErrorMessage("Failed to load article data.");
            }
        };

        fetchArticle();
    }, [article_id]);

    // 입력 변경 처리
    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "password_valid") {
            setPasswordValid(value);
        } else {
            setArticle((prev) => ({ ...prev, [name]: value }));
        }
    };

    // 수정 요청 처리
    const handleSubmit = async (e) => {
        e.preventDefault();

        // 간단한 유효성 검사
        if (!article.title || !article.content || !article.password || !passwordValid) {
            setErrorMessage("All fields are required!");
            return;
        }

        const request = new ArticleEditReqDto(
            article.title,
            article.writer,
            article.content,
            article.password,
            passwordValid,
        );

        try {
            await putArticle(article_id, request);
            setErrorMessage(""); // 성공 시 에러 메시지 초기화
            navigate(`/articles/${article_id}`);
        } catch (error) {
            console.error("Error updating article:", error);
            setErrorMessage("Failed to update article.");
        }
    };

    const handleGoBack = (article_id) => {
        navigate(`/articles/${article_id}`)
    }

    return (
        <div>
            <h1>수정 페이지</h1>
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="title">제목:</label>
                    <input id="title" name="title" value={article.title} onChange={handleChange} placeholder="Title" />
                </div>
                <div>
                    <label htmlFor="writer">작성자:</label>
                    <input id="writer" name="writer" value={article.writer} onChange={handleChange} placeholder="Writer" />
                </div>
                <div>
                    <label htmlFor="content">내용:</label>
                    <textarea id="content" name="content" value={article.content} onChange={handleChange} placeholder="Content" />
                </div>
                <div>
                    <label htmlFor="password">새 비밀번호:</label>
                    <input id="password" name="password" value={article.password} onChange={handleChange} placeholder="Password" />
                </div>
                <div>
                    <label htmlFor="password_valid">기존 비밀번호 (Validation):</label>
                    <input
                        id="password_valid"
                        name="password_valid"
                        value={passwordValid}
                        onChange={handleChange}
                        placeholder="Password (Validation)"
                    />
                </div>
                <div>
                    <button type="submit">저장하기</button>
                    <button onClick={() => handleGoBack(article_id)} >돌아가기</button>
                </div>
            </form>
        </div>
    );
};

export default ArticleEdit;
