import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import CommentCreReqDto from "../dto/comment/request/CommentCreReqDto";
import { postCommentCreReqDto } from "../api/comment_api";

const CommentCre = () => {
    // 변수 선언
    const { article_id } = useParams();
    const [writer, setWriter] = useState("");
    const [content, setContent] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const navigate = useNavigate();

    // 변동 감지 선언
    const handleWriterChange = (event) => {
        setWriter(event.target.value);
    }

    const handleContentChange = (event) => {
        setContent(event.target.value);
    }

    const handleGoArticleDetail = (article_id) => {
        navigate(`/articles/${article_id}`);
    }
    // submit 처리
    const handleSubmit = async (event) => {
        event.preventDefault();

        // 유효성 검사
        if(content === "" || writer === "") {
            setErrorMessage("내용을 채워주세요!");
            return;
        }

        // CommentCreReqDto 객체 생성
        const commentCreReqDto = new CommentCreReqDto(content, writer);

        // api호출하기
        try {
            postCommentCreReqDto(article_id, commentCreReqDto);
            navigate(`/articles/${article_id}`);
        } catch (error) {
            console.error("Error creating article:", error);
            setErrorMessage("Failed to create article. Please try again.");
        }
    }



    return (
        <div>
            <h1>Create Comment</h1>

            {/* 에러 메시지 출력 */}
            {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}

            <form onSubmit={handleSubmit}>
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
                <button type="submit">작성하기</button>
                <button type="button" onClick={ () => handleGoArticleDetail(article_id)}>돌아가기</button>
            </form>
        </div>
    );

}

export default CommentCre;