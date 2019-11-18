package charlie.feng.web;

public class ErrorInfo {

    private String errorToken;
    private String message;

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
