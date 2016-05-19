package tw.edu.nccu.cs.system_programming;

public interface Node {
    String COMMAND_FORMAT = "%8s %-4s %s";

    String[] parse(Context context);
}
