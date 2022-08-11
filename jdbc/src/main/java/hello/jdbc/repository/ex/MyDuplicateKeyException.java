package hello.jdbc.repository.ex;

// 직접 만든 예외 -> JDBC나 JPA와 같은 특정 기술에 종속적이지않다! => 서비스 계층의 순수성을 유지할 수 있다.
public class MyDuplicateKeyException extends MyDbException {

    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
