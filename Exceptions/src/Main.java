import exceptions.LessSeriousCheckedException;
import exceptions.VerySeriousUncheckedException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start");
        try {
            doSomethingVerySerious();
            doSomethingLessSerious();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("End");
        }
    }

    private static void doSomethingVerySerious() {
        try {
            throw new VerySeriousUncheckedException("Something very serious happened");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void doSomethingLessSerious() throws LessSeriousCheckedException {
        throw new LessSeriousCheckedException("Something less serious happened");
    }
}
