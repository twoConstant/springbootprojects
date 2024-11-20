import { useState } from "react";
import ArticleCreReqDto from "../dto/article/request/ArticleCreReqDto";
import { useNavigate } from "react-router-dom";
import { postArticleCre } from "../api/articles_api";

const ArticleCre = () => {
    // 변수 선언
    const [title, setTitle] = useState("");
    const [writer, setWriter] = useState("");
    const [content, setContent] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState(""); // 에러 메시지 상태 추가
    const navigate = useNavigate();

    // 변동 감지 선언
    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    }

    const handleWriterChange = (event) => {
        setWriter(event.target.value);
    }

    const handleContentChange = (event) => {
        setContent(event.target.value);
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    }

    // submit 처리
    const handleSubmit = async (event) => {
        event.preventDefault();

        // 유효성 검사
        if(title === "" || content === "" || password === "") {
            setErrorMessage("내용을 채워주세요!");
            return;
        }

        // ArticleCreReqDto 객체 생성
        const request = new ArticleCreReqDto(title, writer, content, password);

        // api호출하기
        try {
            const data = await postArticleCre(request);
            const article_id = data.id;
            console.log("response article_id: " + article_id );
            navigate(`/articles/${article_id}`);
        } catch (error) {
            console.error("Error creating article:", error);
            setErrorMessage("Failed to create article. Please try again.");
        }
    }

    return (
        <div>
            <h1>Create Article</h1>

            {/* 에러 메시지 출력 */}
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}

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
                    <textarea
                        value={content}
                        onChange={handleContentChange} // 입력값 변경 처리
                        placeholder="Enter Content (Required)"
                    />
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
            </form>
        </div>
    );

}

export default ArticleCre;