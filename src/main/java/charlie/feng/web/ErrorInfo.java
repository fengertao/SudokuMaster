package charlie.feng.web;

public class ErrorInfo {

    private final String errorToken;
    private final String message;

    public String getErrorToken() {
        return errorToken;
    }

    public String getMessage() {
        return message;
    }

    public ErrorInfo(String errorToken, Exception ex) {
        this.errorToken = errorToken;
        this.message = ex.getLocalizedMessage();
    }
}
