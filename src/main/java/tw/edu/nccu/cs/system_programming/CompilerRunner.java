package tw.edu.nccu.cs.system_programming;

import java.io.*;

public class CompilerRunner {
    static public void main(String[] args) {
        if (args.length < 2) {
            System.err.println("usage: java -jar compiler.jar foo.c bar.asm");
            return;
        }

        try {
            FileReader fileReader = new FileReader(args[0]);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = "", code = "";
            while ((line = bufferedReader.readLine()) != null) {
                code += line;
            }
            bufferedReader.close();

            Context context = new Context(code);
            Program program = new Program();

            FileWriter fileWriter = new FileWriter(args[1]);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String out : program.parse(context)) {
                bufferedWriter.write(out + "\n");
            }
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
