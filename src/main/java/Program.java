public class Program implements Node {

    @Override
    public void parse(Context context) {
        System.out.println("[DEBUG] begin of PROGRAM");
        while (true) {
            if (context.currentLine() == null) {
                System.out.println("[DEBUG] end of PROGRAM");
                break;
            } else {
                System.out.println(context.currentLine());
                context.nextLine();
            }
        }
    }

    @Override
    public void execute() {

    }
}
