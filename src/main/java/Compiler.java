import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

interface Node {
    void parse(Context context);

    void execute();
}

class Declaration implements Node {

    @Override
    public void parse(Context context) {
        String currentToken = context.currentToken();
        final int firstSpace = currentToken.indexOf(' ');
        final String rest = currentToken.substring(firstSpace + 1);
        final String[] statements = rest.split(",");

        for (String statement : statements) {
            final int equation = statement.indexOf('=');
            if (equation > 0) {
                final String variable = statement.substring(0, equation);
                final String expression = statement.substring(equation + 1);
                System.out.println(String.format("assign %s to %s", expression, variable));
            } else {
                System.out.println(String.format("%8s RESW 1", statement));
            }
        }
    }

    @Override
    public void execute() {
        // empty
    }
}

class Statement implements Node {

    @Override
    public void parse(Context context) {
        System.out.println("statement -> " + context.currentToken());
        final String[] tokens = context.currentToken().split("\\s+");
        if (tokens[0].equals("INT")) {
            final Declaration declaration = new Declaration();
            declaration.parse(context);
        }
    }

    @Override
    public void execute() {

    }
}

class Program implements Node {
    @Override
    public void parse(Context context) {
        while (context.currentToken() != null) {
            Statement statement = new Statement();
            statement.parse(context);
            context.nextToken();
        }
    }

    @Override
    public void execute() {
        // empty
    }
}

class Context {
    final private Iterator<String> tokens;
    final private HashMap<String, Integer> memory;

    private String currentToken;

    Context(String code) {
        List<String> tokenList = new ArrayList<>();
        for (String token : code.toUpperCase().trim().split(";")) {
            tokenList.add(token);
        }
        tokens = tokenList.iterator();
        memory = new HashMap<>();
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
            System.err.println(String.format(format, token, currentToken));
        }
        nextToken();
    }
}