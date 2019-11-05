package ucsd.ieeeqp.fa19.service;

public interface MmResponseCallback<T> {

    void handleResponse(T response);

    void handleError(Throwable t);
}