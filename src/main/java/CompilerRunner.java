public class CompilerRunner {
    static public void main(String[] args) {
        Compiler compiler = new Compiler();
        compiler.compileLine("INT AAA, BBB=8, CCC=2; AAA = BBB + CCC;");
    }
}
