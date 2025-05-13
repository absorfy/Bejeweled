import gsAxios from "../../../api";


export default async function handleRegister(login, password, setPlayerLogin, setModalMessage) {
  try {
    const response = await gsAxios.post("/player/login", {
      login,
      password,
    });

    setPlayerLogin(login)
    console.log("Login success:", response.data);
  } catch (error) {
    setModalMessage(error.response.data)
  }
}
