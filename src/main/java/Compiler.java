import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Node {
    String COMMAND_FORMAT = "%8s %-4s %s";

    String[] parse(Context context);
}

class Expression implements Node {

    @Override
    public String[] parse(Context context) {
        throw new RuntimeException("Not implemented");
    }

    public String[] parse(Context context, String variable, String expression) {
        return resolve(context, variable, toPostfix(expression), expression);
    }

    private String[] resolve(Context context, String variable, String[] postfix, String expression) {
        final List<String> result = new ArrayList<>();
        final LinkedList<String> stack = new LinkedList<>();

        for (String segment : postfix) {
            if ("+-*/".indexOf(segment) != -1) {
                if (result.isEmpty()) {
                    final String operand = stack.removeLast();
                    result.add(String.format(COMMAND_FORMAT, "", "LDA", operand));
                    result.add(String.format(COMMAND_FORMAT, "", "STA", "TEMP"));
                }

                String operator = "";
                if (segment.equals("+")) {
                    operator = "ADD";
                } else if (segment.equals("-")) {
                    operator = "SUB";
                } else if (segment.equals("*")) {
                    operator = "MUL";
                } else if (segment.equals("/")) {
                    operator = "DIV";
                }

                String operand = stack.removeLast();
                result.add(String.format(COMMAND_FORMAT, "", "LDA", "TEMP"));
                result.add(String.format(COMMAND_FORMAT, "", operator, operand));
                result.add(String.format(COMMAND_FORMAT, "", "STA", "TEMP"));
            } else {
                if (isNumeric(segment)) {
                    stack.add("#" + segment);
                } else {
                    if (!context.symbols.contains(segment)) {
                        String format = "Undeclared symbol: %s in %s=%s";
                        throw new RuntimeException(String.format(format, segment, variable, expression));
                    }
                    stack.add(segment);
                }
            }
        }

        while (!stack.isEmpty()) {
            final String operand = stack.removeLast();
            result.add(String.format(COMMAND_FORMAT, "", "LDA", operand));
            result.add(String.format(COMMAND_FORMAT, "", "STA", "TEMP"));
        }

        result.add(String.format(COMMAND_FORMAT, "", "LDA", "TEMP"));
        result.add(String.format(COMMAND_FORMAT, "", "STA", variable));

        return result.toArray(new String[result.size()]);
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private String[] toPostfix(String infix) {
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

        return output.toArray(new String[output.size()]);
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
        return op.equals("+") || op.equals("-") ? 1 : op.equals("*") || op.equals("/") ? 2 : 0;
    }
}

class Declaration implements Node {

    @Override
    public String[] parse(Context context) {
        final List<String> result = new ArrayList<>();
        final String currentToken = context.currentToken();
        final int firstSpace = currentToken.indexOf(' ');
        final String rest = currentToken.substring(firstSpace + 1);
        final String[] statements = rest.split(",");

        for (String statement : statements) {
            final int equation = statement.indexOf('=');
            if (equation > 0) {
                final String variable = statement.substring(0, equation);

                final String expression = statement.substring(equation + 1);
                final Expression factor = new Expression();

                for (String line : factor.parse(context, variable, expression)) {
                    result.add(line);
                }

                context.symbols.add(variable);
            } else {
                context.symbols.add(statement);
            }
        }

        return result.toArray(new String[result.size()]);
    }
}

class Statement implements Node {
    @Override
    public String[] parse(Context context) {
        final List<String> lines = new ArrayList<>();
        final String[] tokens = context.currentToken().split("\\s+");
        if (tokens[0].equals("INT")) {
            final Declaration declaration = new Declaration();
            for (String line : declaration.parse(context)) {
                lines.add(line);
            }
        } else if (context.currentToken().contains("PRINT")) {
            final int left = context.currentToken().indexOf("(");
            final int right = context.currentToken().indexOf(")");
            final String variable = context.currentToken().substring(left + 1, right);
            lines.add(String.format(COMMAND_FORMAT, "", "WRT", variable));
        } else if (context.currentToken().contains("=")) {
            final String currentToken = context.currentToken();

            final int equation = currentToken.indexOf("=");
            final String variable = currentToken.substring(0, equation);
            final String expression = currentToken.substring(equation + 1);

            final Expression factor = new Expression();
            for (String line : factor.parse(context, variable, expression)) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }
}

class Program implements Node {
    @Override
    public String[] parse(Context context) {
        final List<String> result = new ArrayList<>();
        final List<String> lines = new ArrayList<>();

        while (context.currentToken() != null) {
            Statement statement = new Statement();
            for (String line : statement.parse(context)) {
                lines.add(line);
            }
            context.nextToken();
        }

        result.add("         START");
        result.add(String.format(COMMAND_FORMAT, "TEMP", "RESW", "1"));
        for (String symbol : context.symbols) {
            result.add(String.format(COMMAND_FORMAT, symbol, "RESW", "1"));
        }
        for (String line : lines) {
            result.add(line);
        }
        result.add("         END");

        return result.toArray(new String[result.size()]);
    }
}

class Context {
    final public LinkedList<String> symbols;

    final private Iterator<String> tokens;

    private String currentToken;

    Context(String code) {
        List<String> tokenList = new ArrayList<>();
        for (String token : code.toUpperCase().trim().split(";")) {
            tokenList.add(token);
        }
        tokens = tokenList.iterator();
        symbols = new LinkedList<>();
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