import axiosInstance from "./axiosInstance"

export const getArticleSummaryResDto = async() => {
    console.log("in getArticleSummaryResDto");
    try {
        const response = await axiosInstance.get("/articles");
        return response.data;
    } catch (error) {
        console.log("error");
        return;
    }
    
}

export const getArticleDetailResDto = async(article_id) => {
    const response = await axiosInstance.get(`/articles/${article_id}`);
    return response.data;
}

export const postArticleCre = async(request) => {
    const response = await axiosInstance.post(`/articles`, request);
    return response.data;
}

export const putArticle = async(article_id, request) => {
    await axiosInstance.put(`/articles/${article_id}`, request);
}

