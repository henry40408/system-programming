public class CompilerRunner {
    static public void main(String[] args) {
        Context context = new Context("INT AAA,BBB=8,CCC=1+BBB*(3-2);AAA=BBB+CCC;PRINT AAA");
        Program program = new Program();
        program.parse(context);
    }
}
