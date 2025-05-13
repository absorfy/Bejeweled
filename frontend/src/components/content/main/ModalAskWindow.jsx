import styles from "./LoginModal.module.css";
import DefaultButton from "../../DefaultButton";
import ModalWindow from "./ModalWindow";

export default function ModalAskWindow({ isOpen, onClose, onAccept, textValue }) {

  return (
    <ModalWindow onClose={onClose} isOpen={isOpen}>
      <div className={styles.modal}>
        <span className={styles.title}>{textValue}</span>
        <br/>
        <div className="d-flex justify-content-end gap-2 mt-4">
          <DefaultButton textValue={onAccept ? "Cancel" : "Close"} buttonClickHandler={onClose} />
          {onAccept && <DefaultButton textValue={"Accept"} buttonClickHandler={() => { onClose(); onAccept(); }} />}
        </div>
      </div>
    </ModalWindow>
  )
}
