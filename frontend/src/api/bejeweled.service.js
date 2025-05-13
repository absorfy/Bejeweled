import gsAxios from "./index";

export const startGame = () => gsAxios.get('/bejeweled/start');

export const swapGems = (row1, col1, row2, col2) =>
  gsAxios.post('/bejeweled/swap', { row1, col1, row2, col2 });

export const getHint = () => gsAxios.get('/bejeweled/hint');

export const testField = () => gsAxios.get('/bejeweled/test');
