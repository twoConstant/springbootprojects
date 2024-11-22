import axiosInstance from "./axiosInstance"

/**
 * Method : POST
 * Path : /articles/{article_id}/comments
 * RequestBody : CommentCreReqDto
 */
export const postCommentCreReqDto = async(article_id, commentCreReqDto) => {
    console.log("postCommentCreReqDto 요청");
    console.log("commentCreReqDto: " + commentCreReqDto);
    try {
        await axiosInstance.post(`/articles/${article_id}/comments`, commentCreReqDto);
    } catch (e) {
        console.log(e);
    }
}
