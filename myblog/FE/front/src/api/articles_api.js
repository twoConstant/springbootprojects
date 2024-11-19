import axiosInstance from "./axiosInstance"

export const getArticleSummaryResDto = async() => {
    const response = await axiosInstance.get("/api/articles");
    return response.data;
}

export const getArticleDetailResDto = async(article_id) => {
    const response = await axiosInstance.get(`/api/articles/${article_id}`);
    return response.data;
}