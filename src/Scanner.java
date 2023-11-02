import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Scanner {
    private String sourceCode;
    private final Set<String> RESERVED_WORDS = new HashSet<>();
    private final Set<String> SPECIAL_SYMBOLS = new HashSet<>();

    public Scanner(File sourceFile, File tokenFile) throws FileNotFoundException {
        StringBuilder sourceCodeBuilder = new StringBuilder();
        java.util.Scanner fileScanner = new java.util.Scanner(sourceFile);
        while (fileScanner.hasNext()) {
            sourceCodeBuilder.append(fileScanner.nextLine()).append("\n");
        }
        this.sourceCode = sourceCodeBuilder.toString();
        fileScanner.close();

        fileScanner = new java.util.Scanner(tokenFile);
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine().trim();
            String[] parts = line.split(":", 2);
            if (parts.length < 2) continue;

            String title = parts[0].trim();
            String[] tokens = parts[1].trim().split("\\s+");

            if ("Special symbols".equalsIgnoreCase(title)) {
                SPECIAL_SYMBOLS.addAll(Arrays.asList(tokens));
            } else if ("Reserved words".equalsIgnoreCase(title)) {
                RESERVED_WORDS.addAll(Arrays.asList(tokens));
            }
        }
        fileScanner.close();
    }

    public void scan() {
        SymbolTable st = new SymbolTable();
        List<PIFEntry> pif = new ArrayList<>();
        boolean lexically_correct = true;

        String[] lines = sourceCode.split("\n");
        int currentLine = 0;

        for (String line : lines) {
            currentLine++;
            String processedLine = preprocess(line);

            String[] tokens = processedLine.split("\\s+");
            for (String token : tokens) {
                if (RESERVED_WORDS.contains(token)) {
                    pif.add(new PIFEntry(token, -1));
                } else if (SPECIAL_SYMBOLS.contains(token)) {
                    pif.add(new PIFEntry(token, -1));
                } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    st.add(token);
                    pif.add(new PIFEntry("Id", st.getPosition(token)));
                } else if (token.matches("0|[1-9]\\d*")) {
                    st.add(token);
                    pif.add(new PIFEntry("Const", st.getPosition(token)));
                } else {
                    System.out.println("Lexical error: Unrecognized token '" + token + "' at line " + currentLine);
                    lexically_correct = false;
                }
            }
        }

        writeOutput(pif, st);
        if(lexically_correct){
            System.out.println("Lexically correct");
        }
    }

    private String preprocess(String line) {
        line = line.replace("==", " == ");
        line = line.replace("!=", " != ");
        line = line.replace(">=", " >= ");
        line = line.replace("<=", " <= ");
        line = line.replace(";", " ; ");
        line = line.replace("+", " + ");
        line = line.replace("-", " - ");
        line = line.replace("*", " * ");
        line = line.replace("/", " / ");
        line = line.replace(">", " > ");
        line = line.replace("<", " < ");
        line = line.replace("(", " ( ");
        line = line.replace(")", " ) ");
        line = line.replace("[", " [ ");
        line = line.replace("]", " ] ");
        line = line.replace("{", " { ");
        line = line.replace("}", " } ");
        line = line.replace(",", " , ");
        return line;
    }

    private void writeOutput(List<PIFEntry> pif, SymbolTable st) {
        try (PrintWriter pifOut = new PrintWriter("PIF.out");
             PrintWriter stOut = new PrintWriter("ST.out")) {

            for (PIFEntry entry : pif) {
                pifOut.println(entry);
            }

            st.print(stOut);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    static class PIFEntry {
        String token;
        int position;

        public PIFEntry(String token, int position) {
            this.token = token;
            this.position = position;
        }

        @Override
        public String toString() {
            return token + " " + position;
        }
    }
}
