import styles from "./LoginModal.module.css";
import {useState} from "react";
import handleRegister from "./LoginModalOperations";
import {usePlayer} from "../../PlayerContext";
import DefaultButton from "../../DefaultButton";
import ModalWindow from "./ModalWindow";

export default function LoginModal({ isOpen, onClose, setModalMessage }) {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const { setPlayerLogin } = usePlayer();

  function handleSubmit(e) {
    e.preventDefault()
    handleRegister(login, password, setPlayerLogin, setModalMessage)
      .then(() => {
        setLogin("null")
        setPassword("null")
        onClose();
      })
  }

  return (
    <ModalWindow onClose={onClose} isOpen={isOpen}>
      <div className={styles.modal}>
        <h2 className={styles.title}>Log In</h2>
        <form
          onSubmit={handleSubmit}
        >
          <div className="mb-3">
            <label className="form-label">Login</label>
            <input maxLength={20} minLength={3} type="text" className="form-control" onChange={e => setLogin(e.target.value)} />
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input maxLength={20} minLength={3} type="password" className="form-control" onChange={e => setPassword(e.target.value)} />
          </div>
          <div className="d-flex justify-content-end gap-2 mt-4">
            <DefaultButton buttonClickHandler={onClose} textValue={"Cancel"} />
            <DefaultButton type={"submit"} textValue={"Log In"} />
          </div>
        </form>
      </div>
    </ModalWindow>
  );
}
