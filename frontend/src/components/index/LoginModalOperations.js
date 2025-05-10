import gsAxios from "../../api";


export default async function handleRegister(login, password, setPlayerLogin) {
  try {
    const response = await gsAxios.post("/player/login", {
      login,
      password,
    });

    setPlayerLogin(login)
    console.log("Login success:", response.data);
  } catch (error) {
    console.error("Login failed:", error.response.data);
    alert(error.response.data);
  }
}
