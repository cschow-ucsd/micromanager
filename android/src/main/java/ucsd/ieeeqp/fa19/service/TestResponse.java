package ucsd.ieeeqp.fa19.service;

public class TestResponse {
    private boolean OK;
    private String body;

    public TestResponse(boolean OK, String body) {
        this.OK = OK;
        this.body = body;
    }

    public boolean isOK() {
        return OK;
    }

    public String getBody() {
        return body;
    }
}
