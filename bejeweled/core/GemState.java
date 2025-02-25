package bejeweled.core;

public enum GemState {
    IDLE("@"),
    FALLING("!"),
    IN_COMBINATION("$");

    private final String code;

    GemState(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
