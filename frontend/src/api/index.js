import axios from "axios";

const gsAxios = axios.create({
    baseURL: 'http://localhost:8080/api/',
});

export default gsAxios;
