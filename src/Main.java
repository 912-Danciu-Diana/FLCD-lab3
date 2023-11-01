import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("myFile.txt"),
                    new File("Token.in"));
            scanner.scan();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }
}