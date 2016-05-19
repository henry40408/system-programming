import java.util.Arrays;
import java.util.ListIterator;

public class CompilerRunner {
    static public void main(String[] args) {
        Context context = new Context("INT AAA,BBB=8,CCC=2;AAA=BBB+CCC;PRINT(AAA);");
        Program program = new Program();

        ListIterator<String> it = Arrays.asList(program.parse(context)).listIterator();
        while(it.hasNext()) {
            System.out.println(String.format("%s", it.next()));
        }
    }
}
