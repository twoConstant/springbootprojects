import axios from "axios";
import BASE_URL from "../config/apiConfig";

// Axios 인스턴스 생성하기
const axiosInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 5000,
});

export default axiosInstance;