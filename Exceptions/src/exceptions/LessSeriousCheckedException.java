package exceptions;

import java.io.IOException;

public class LessSeriousCheckedException extends IOException {

    public LessSeriousCheckedException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return ("Custom exception: " + super.getMessage());
    }


}
