import axios from "axios";

const gsAxios = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL + '/api',
    withCredentials: true,
    headers: {
        'ngrok-skip-browser-warning': 'true',
    },
});

export default gsAxios;
