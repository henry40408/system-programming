public class Compiler {
    public String change(String current, String gathered) {
        if (gathered.equals("INT")) {
            System.out.println("declaration");
            return "";
        } else if (current.equals(" ")) {
            return gathered;
        } else if (current.equals(",")) {
            System.out.println("comma = " + gathered);
            return "";
        } else if (current.equals("=")) {
            System.out.println("assign = " + gathered);
            return "";
        } else if (current.equals(";")) {
            System.out.println("end");
            return "";
        } else {
            return gathered + current;
        }
    }

    public void doCompile(String gathered, String rest) {
        final String current = rest.substring(0, 1);
        final String newRest = rest.substring(1);

        final String newGathered = change(current, gathered);
        System.out.println("gathered = '" + newGathered + "', rest = '" + newRest + "'");

        if (newRest.length() > 0) {
            doCompile(newGathered, newRest);
        }
    }

    public void compileLine(String line) {
        doCompile("", line);
    }
}
