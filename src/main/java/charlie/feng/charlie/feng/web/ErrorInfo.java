package charlie.feng.charlie.feng.web;

public class ErrorInfo {
    public final String errorToken;
    public final String message;

    public ErrorInfo(String errorToken, Exception ex) {
        this.errorToken = errorToken;
        this.message = ex.getLocalizedMessage();
    }
}