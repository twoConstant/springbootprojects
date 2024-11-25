import { useEffect, useState } from "react";
import { getCommentsAtArticleResDto } from "../api/articles_api";
import "../styles.css";
import { patchCommentPatchReqDto } from "../api/comment_api";

const CommentsAtArticle = ({ article_id }) => {
    const [comments, setComments] = useState([]);
    const [editingCommentId, setEditingCommentId] = useState(null); // 편집 중인 댓글 ID
    const [editedContent, setEditedContent] = useState(""); // 편집 중인 댓글 내용
    const [expandedComments, setExpandedComments] = useState({}); // childComments 표시 상태

    useEffect(() => {
        const fetchComments = async () => {
            const commentAtArticleResDto = await getCommentsAtArticleResDto(article_id);
            setComments(commentAtArticleResDto);
        };

        fetchComments();
    }, [article_id]);

    // 편집 모드 활성화
    const handleEditClick = (comment) => {
        setEditingCommentId(comment.id); // 현재 편집 중인 댓글 ID 설정
        setEditedContent(comment.content); // 기존 댓글 내용 설정
    };

    // 편집 취소
    const handleCancelClick = () => {
        setEditingCommentId(null); // 편집 중인 댓글 ID 초기화
        setEditedContent(""); // 편집 내용 초기화
    };

    // 댓글 수정 저장
    const handleSaveClick = async (comment_id) => {
        const previousComments = [...comments]; // 이전 상태 백업
        try {
            await patchCommentPatchReqDto(article_id, comment_id, editedContent);

            // 상태 업데이트
            setComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.id === comment_id ? { ...comment, content: editedContent } : comment
                )
            );

            setEditingCommentId(null);
            setEditedContent("");
        } catch (error) {
            console.error("댓글 수정 실패:", error);

            // 실패 시 이전 상태로 복구
            setComments(previousComments);
        }
    };

    // childComments 보기/숨기기 토글
    const toggleChildComments = (comment_id) => {
        setExpandedComments((prevState) => ({
            ...prevState,
            [comment_id]: !prevState[comment_id],
        }));
    };

    const renderComments = (comments) => {
        return comments.map((comment) => (
            <div key={comment.id} className="comment">
                <div className="comment-header">
                    <p className="comment-writer">{comment.writer + ":"}</p>

                    {editingCommentId === comment.id ? (
                        // 편집 중인 댓글
                        <div className="edit-section">
                            <textarea
                                value={editedContent}
                                onChange={(e) => setEditedContent(e.target.value)}
                                className="edit-textarea"
                            />
                            <button onClick={handleCancelClick}>취소</button>
                            <button onClick={() => handleSaveClick(comment.id)}>저장</button>
                        </div>
                    ) : (
                        // 일반 댓글
                        <div className="comment-content-wrapper">
                            <p className="comment-content">{comment.content}</p>
                            <button onClick={() => handleEditClick(comment)} className="edit-button">
                                수정하기
                            </button>
                        </div>
                    )}
                </div>

                {/* 댓글 보기/숨기기 버튼 */}
                {Array.isArray(comment.childComments) && comment.childComments.length > 0 && (
                    <div className="child-comment-toggle">
                        <button
                            onClick={() => toggleChildComments(comment.id)}
                            className="toggle-button"
                        >
                            {expandedComments[comment.id] ? "댓글 숨기기" : "댓글 보기"}
                        </button>
                    </div>
                )}

                {/* childComments */}
                {expandedComments[comment.id] && Array.isArray(comment.childComments) && (
                    <div className="child-comments">
                        {renderComments(comment.childComments)}
                    </div>
                )}
            </div>
        ));
    };

    return (
        <div>
            <h2>댓글</h2>
            {renderComments(comments)}
        </div>
    );
};

export default CommentsAtArticle;
