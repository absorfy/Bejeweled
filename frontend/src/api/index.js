import axios from "axios";

const gsAxios = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true,
    headers: {
        'ngrok-skip-browser-warning': 'true',
    },
});

export default gsAxios;
