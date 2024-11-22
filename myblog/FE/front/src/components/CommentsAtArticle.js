// import { useEffect, useState } from "react";
// import { getCommentsAtArticleResDto } from "../api/articles_api";

// const CommentsAtArticle = ({article_id}) => {
//     const [comments, setComments] = useState([]);

//     useEffect(() => {
//         const fetchComments = async () => {
//             const commentAtArticleResDto = await getCommentsAtArticleResDto(article_id);
//             console.log(commentAtArticleResDto);
//             setComments(commentAtArticleResDto);
//         };

//         fetchComments();
//     },
//     [article_id]
//     );

//     const renderComments = (comments) => {
//         return comments.map((comment) => (
//             <div key={comment.id}>
//                 <p>작성자: {comment.writer}</p>
//                 <p>내용: {comment.content}</p>
//                 {Array.isArray(comment.childComments) && comment.childComments.length > 0 && (
//                     <div style={{ marginLeft: "20px" }}> {/* 자식 댓글 들여쓰기 */}
//                         {renderComments(comment.childComments)}
//                     </div>
//                 )}
//             </div>
//         ));
//     }

//     return (
//         <div>
//             <h2>댓글</h2>
//             {renderComments(comments)} {/* 댓글 렌더링 */}
//         </div>
//     )
// }

// export default CommentsAtArticle;

import { useEffect, useState } from "react";
import { getCommentsAtArticleResDto } from "../api/articles_api";
import "../styles.css";

const CommentsAtArticle = ({ article_id }) => {
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchComments = async () => {
            const commentAtArticleResDto = await getCommentsAtArticleResDto(article_id);
            setComments(commentAtArticleResDto);
        };

        fetchComments();
    }, [article_id]);

    const renderComments = (comments) => {
        return comments.map((comment) => (
            <div key={comment.id} className="comment">
                <p>작성자: {comment.writer}</p>
                <p>내용: {comment.content}</p>
                {Array.isArray(comment.childComments) && comment.childComments.length > 0 && (
                    <div style={{ marginLeft: "20px" }}>
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
