import CommentAtArticleResDto from "../dto/article/response/CommentAtArticleResDto";
import axiosInstance from "./axiosInstance"

// 게시글 전체 목록 조회
export const getArticleSummaryResDto = async() => {
    console.log("in getArticleSummaryResDto");
    try {
        const response = await axiosInstance.get("/articles");
        return response.data;
    } catch (error) {
        console.log("error");
    }
    
}

// 단일 게시글 조회 요청
export const getArticleDetailResDto = async(article_id) => {
    const response = await axiosInstance.get(`/articles/${article_id}`);
    return response.data;
}

// 게시글 생성 요청
export const postArticleCre = async(request) => {
    const response = await axiosInstance.post(`/articles`, request);
    return response.data;
}

// 게시글 수정 요청
export const putArticle = async(article_id, request) => {
    await axiosInstance.put(`/articles/${article_id}`, request);
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
export const getCommentsAtArticleResDto = async(article_id) => {
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
