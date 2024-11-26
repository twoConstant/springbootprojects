import CommentModifyReqDto from "../dto/comment/request/CommentModifyReqDto";
import axiosInstance from "./axiosInstance"

/**
 * Method : POST
 * Path : /articles/{article_id}/comments
 * RequestBody : commentAddReqDto
 */
export const postComment = async(article_id, commentAddReqDto) => {
    console.log("postComment 요청");
    console.log("commentAddReqDto: " + commentAddReqDto);
    try {
        await axiosInstance.post(`/articles/${article_id}/comments`, commentAddReqDto);
    } catch (e) {
        console.log(e);
    }
}

/**
 * Method : Patch
 * Path : /articles/{article_id}/comments/{comment_id}
 * RequestBody : CommentModifyReqDto
 */
export const patchComment = async(article_id, comment_id, input) => {
    console.log("patchComment 요청");
    console.log("target article_id: " + article_id);
    console.log("target comment_id: " + comment_id);
    console.log('input :' + input);

    // commentModifyReqDto 생성
    const commentModifyReqDto = new CommentModifyReqDto(input);
    console.log("commentModifyReqDto: " + commentModifyReqDto);

    try {
        await axiosInstance.patch(`/articles/${article_id}/comments/${comment_id}`, commentModifyReqDto);
    } catch(e) {
        console.error("Patch 실패:", e);
        throw new Error("댓글 수정 중 오류가 발생했습니다."); // 호출부에서 처리하도록 에러 throw
    }
}