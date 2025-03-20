package hcmute.techshop.Exception;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {
    public IllegalArgumentException(String message) {
        super(message);
        System.out.println(message);
    }
}
