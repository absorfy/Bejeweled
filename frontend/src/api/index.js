import axios from "axios";

const rawBase = import.meta.env?.VITE_BASE_URL?.trim();
const baseURL = (rawBase ? rawBase.replace(/\/+$/, "") : "") + "/api";

const gsAxios = axios.create({
    baseURL,
    withCredentials: true,
    headers: {
        "ngrok-skip-browser-warning": "true",
    },
});

export default gsAxios;