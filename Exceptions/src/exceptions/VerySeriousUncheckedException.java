package exceptions;

public class VerySeriousUncheckedException extends RuntimeException {

    public VerySeriousUncheckedException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return ("Custom exception: " + super.getMessage());
    }
}
