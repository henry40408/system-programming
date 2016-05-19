import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

interface Node {
    void parse(Context context);

    void execute();
}

class Expression implements Node {

    @Override
    public void parse(Context context) {
        throw new NotImplementedException();
    }

    @Override
    public void execute() {
        // empty
    }

    public void parse(Context context, String expression) {
        System.out.println(toPostfix(expression));
    }

    private String toPostfix(String infix) {
        final String[] chopped = chop(infix);
        final LinkedList<String> stack = new LinkedList<>();
        final LinkedList<String> output = new LinkedList<>();

        for (String segment : chopped) {
            if (segment.equals("(")) {
                stack.add(segment);
            } else if ("+-*/".indexOf(segment) != -1) {
                while (!stack.isEmpty() && priority(stack.getLast()) >= priority(segment)) {
                    output.add(stack.removeLast());
                }
                stack.add(segment);
            } else if (segment.equals(")")) {
                while (!stack.getLast().equals("(")) {
                    output.add(stack.removeLast());
                }
                stack.removeLast();
            } else {
                output.add(segment);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.removeLast());
        }

        return Arrays.toString(output.toArray());
    }

    private String[] chop(String infix) {
        final LinkedList<String> stack = new LinkedList<>();
        String gathered = "";

        for (char c : infix.toCharArray()) {
            if ("+-*/()".indexOf(c) != -1) {
                if (gathered.length() > 0) {
                    stack.add(gathered);
                    gathered = "";
                }
                stack.add(c + "");
            } else {
                gathered += c;
            }
        }

        if (gathered.length() > 0) {
            stack.add(gathered);
        }

        return stack.toArray(new String[stack.size()]);
    }

    private static int priority(String op) {
        return op.equals("+") || op.equals("-") ? 1 :
                op.equals("*") || op.equals("/") ? 2 : 0;
    }
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

                final Expression expression1 = new Expression();
                expression1.parse(context, expression);
            } else {
                System.out.println(String.format("%8s RESW 1", statement));
                context.memory.add(statement);
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
    final public LinkedList<String> memory;

    final private Iterator<String> tokens;

    private String currentToken;

    Context(String code) {
        List<String> tokenList = new ArrayList<>();
        for (String token : code.toUpperCase().trim().split(";")) {
            tokenList.add(token);
        }
        tokens = tokenList.iterator();
        memory = new LinkedList<>();
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