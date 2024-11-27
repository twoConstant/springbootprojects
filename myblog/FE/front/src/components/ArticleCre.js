import { useState, useRef, useEffect } from "react";
import ArticleCreReqDto from "../dto/article/request/ArticleCreReqDto";
import { useNavigate } from "react-router-dom";
import { postArticle } from "../api/articles_api";
import Quill from "../config/Quill";
import "quill/dist/quill.snow.css"; // Quill snow 테마 CSS import
import "../csses/articleCre.css"; // CSS 파일 import

const ArticleCre = () => {
    // 변수 선언
    const [title, setTitle] = useState("");
    const [writer, setWriter] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 상태 추가
    const navigate = useNavigate();

    // Quill 관련 변수 선언
    const editorRef = useRef(null);
    const quillInstance = useRef(null);

    // Quill 에디터 초기화
    useEffect(() => {
        if (!quillInstance.current) {
            quillInstance.current = new Quill(editorRef.current, {
                theme: "snow",
                modules: {
                    toolbar: [
                        [{ 'header': [1, 2, false] }],
                        ['bold', 'italic', 'underline'],
                        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
                        ['link', 'image'],
                        ['clean'] // 포맷 지우기 버튼
                    ],
                },
            });
        }

        return () => {
            // 컴포넌트가 사라질 때 인스턴스를 정리합니다.
            if (quillInstance.current) {
                quillInstance.current = null;
            }
        };
    }, []);

    // 변동 감지 선언
    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleWriterChange = (event) => {
        setWriter(event.target.value);
    };

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    // submit 처리
    const handleSubmit = async (event) => {
        event.preventDefault();

        // Quill 에디터에서 내용 가져오기
        const content = JSON.stringify(quillInstance.current.getContents());

        // 유효성 검사
        if (title === "" || content === "" || password === "") {
            setErrorMessage("내용을 채워주세요!");
            return;
        }

        // ArticleCreReqDto 객체 생성
        const request = new ArticleCreReqDto(title, writer, content, password);

        // API 호출하기
        try {
            const data = await postArticle(request);
            const article_id = data.id;
            console.log("response article_id: " + article_id);
            navigate(`/articles/${article_id}`);
        } catch (error) {
            console.error("Error creating article:", error);
            setErrorMessage("Failed to create article. Please try again.");
        }
    };

    return (
        <div className="container">
            <h1>Create Article</h1>

            {/* 에러 메시지 출력 */}
            {errorMessage && <p className="error-message">{errorMessage}</p>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>제목:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={handleTitleChange} // 입력값 변경 처리
                        placeholder="Enter Title (Required)"
                    />
                </div>
                <div>
                    <label>작성자:</label>
                    <input
                        type="text"
                        value={writer}
                        onChange={handleWriterChange} // 입력값 변경 처리
                        placeholder="Enter Writer (Optional)"
                    />
                </div>
                <div>
                    <label>내용:</label>
                    <div ref={editorRef} className="quill-container" style={{ height: "300px" }} />
                </div>
                <div>
                    <label>비밀번호:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={handlePasswordChange} // 입력값 변경 처리
                        placeholder="Enter Password (Required)"
                    />
                </div>
                <button type="submit">Submit</button>
                <button type="button" onClick={() => navigate("/articles")}>돌아가기</button>
            </form>
        </div>
    );
};

export default ArticleCre;
