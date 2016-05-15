public class CompilerRunner {
    static public void main(String[] args) {
        Context context = new Context("INT AAA,BBB=8,CCC=2;AAA=BBB+CCC;PRINT AAA");

        Node program = new Program();
        program.parse(context);
        program.execute();
    }
}
