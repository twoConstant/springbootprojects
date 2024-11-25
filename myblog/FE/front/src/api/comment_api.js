import CommentPatchReqDto from "../dto/comment/request/CommentPatchReqDto";
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

/**
 * Method : Patch
 * Path : /articles/{article_id}/comments/{comment_id}
 * RequestBody : CommentPatchReqDto
 */
export const patchCommentPatchReqDto = async(article_id, comment_id, input) => {
    console.log("patchCommentPatchReqDto 요청");
    console.log("target article_id: " + article_id);
    console.log("target comment_id: " + comment_id);
    console.log('input :' + input);

    // CommentPatchReqDto 생성
    const commentPatchReqDto = new CommentPatchReqDto(input);
    console.log("commentPatchReqDto: " + commentPatchReqDto);

    try {
        await axiosInstance.patch(`/articles/${article_id}/comments/${comment_id}`, commentPatchReqDto);
    } catch(e) {
        console.error("Patch 실패:", e);
        throw new Error("댓글 수정 중 오류가 발생했습니다."); // 호출부에서 처리하도록 에러 throw
    }
}