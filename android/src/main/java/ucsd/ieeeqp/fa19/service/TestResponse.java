package ucsd.ieeeqp.fa19.service;

public class TestResponse {
    private String message;

    public TestResponse() {
        this("");
    }

    public TestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TestResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
