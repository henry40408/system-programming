import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Context {
    private Iterator<String> lines;
    private String currentLine;

    Context(String input) {
        List<String> linesParsed = new ArrayList<>();

        for (String line : input.trim().split(";")) {
            linesParsed.add(line);
        }

        lines = linesParsed.iterator();
        nextLine();
    }

    String nextLine() {
        currentLine = null;
        if (lines.hasNext()) {
            currentLine = lines.next();
        }
        return currentLine;
    }

    String currentLine() {
        return currentLine;
    }

    void skipLine(String line) {
        if (!line.equals(currentLine)) {
            String format = "Warning: %s is expected, but %s is found.";
            System.err.println(String.format(format, line, currentLine));
        }
        nextLine();
    }

    int currentNumber() {
        return Integer.parseInt(currentLine);
    }
}