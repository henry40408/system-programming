package tw.edu.nccu.cs.system_programming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Context {
    final public HashMap<String, Integer> symbols;

    final private Iterator<String> tokens;

    private String currentToken;

    Context(String code) {
        List<String> tokenList = new ArrayList<>();
        for (String token : code.toUpperCase().trim().split(";")) {
            tokenList.add(token);
        }
        tokens = tokenList.iterator();
        symbols = new HashMap<>();
        nextToken();
    }

    String nextToken() {
        currentToken = null;
        if (tokens.hasNext()) {
            currentToken = tokens.next();
        }
        return currentToken;
    }

    String currentToken() {
        return currentToken;
    }

    void skipToken(String token) {
        if (!token.equals(currentToken)) {
            String format = "Warning: %s is expected, but %s is found.";
            throw new RuntimeException(String.format(format, token, currentToken));
        }
        nextToken();
    }
}
