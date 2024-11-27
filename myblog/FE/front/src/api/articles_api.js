import CommentAtArticleResDto from "../dto/article/response/CommentAtArticleResDto";
import axiosInstance from "./axiosInstance"

// 게시글 전체 목록 조회
export const getArticlePage = async(page, size) => {
    console.log("in getArticlePage");
    try {
        const response = await axiosInstance.get(`/articles?page=${page}&size=${size}`);
        return response.data;
    } catch (error) {
        console.log("error");
    }
}

// 단일 게시글 조회 요청
export const getArticle = async(article_id) => {
    const response = await axiosInstance.get(`/articles/${article_id}`);
    return response.data;
}

// 게시글 생성 요청
export const postArticle = async(request) => {
    const response = await axiosInstance.post(`/articles`, request);
    return response.data;
}

// 게시글 수정 요청
export const putArticle = async(article_id, articleEditReqDto) => {

    try {
        await axiosInstance.put(`/articles/${article_id}`, articleEditReqDto);
    } catch (e) {
        console.error("putArticle 실패:", e);
        throw new Error("putArticle 호출과정에서 실패하였습니다."); // 호출부에서 처리하도록 에러 throw
    }
    
}

// 게시글 추천 클릭
export const patchArticleStar = async(article_id) => {
    console.log("call patchArticleStar");
    try {
        await axiosInstance.patch(`/articles/${article_id}/stars/increment`);
    } catch(e) {
        console.log(e);
    }
} 


// 단일 게시글 전체 댓글 조회
export const getCommentListAtArticle = async(article_id) => {
    try {
        const response = await axiosInstance.get(`/articles/${article_id}/comments`);
        const data = response.data;
        console.log("서버 응답 Json: " + data);
        data.map(
            (element) => {
                new CommentAtArticleResDto(
                    element.id,
                    element.writer,
                    element.content,
                    element.created,
                    element.childComments
                )
            }
        )
        console.log("프론트 응답 객체: " + data);
        return data;
    } catch (e) {
        console.log(e);
    }
}


/**
 * 목적 : 게시글 조회시 게시글 ViewCount++
 * Method : Patch
 * Path : /articles/{article_id}/views/increment
 */
export const patchArticleVeiwCount = async(article_id) => {
    console.log("patchArticleVeiwCount 호출");
    console.log("target article_id: " + article_id);
    try {
        await axiosInstance.patch(`/articles/${article_id}/views/increment`);
    } catch(e) {
        console.error("Patch 실패:", e);
        throw new Error("patchArticleVeiwCount 호출과정에서 실패하였습니다."); // 호출부에서 처리하도록 에러 throw
    }
} 
