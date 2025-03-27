import CodeWriters.*;
import ResourceOperations.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class TestGenerator {
    public void generate(TestPlan plan) throws Exception{
        //write the code in the file to generate the test suite
        //name of the file in plan.getPlanName()
        ObjectMapper mapper = new ObjectMapper();
        String className = capitalize(plan.getPlanName()) + "Test";
        String outputDir = "src/test/java/generated";

        // Generate Java test class code
        StringBuilder javaCode = new StringBuilder();
        javaCode.append("package generated;\n\n")
                .append("import platform.resource.BaseResource;\n")
                .append("import platform.testcase.*;\n")
                .append("import org.rasp.iiitb720.resource.*;\n")
                .append("import java.util.HashMap;\n")
                .append("import java.util.Map;\n\n")
                .append("public class ").append(className).append(" {\n\n")
                .append("    public static void main(String[] args) {\n")
                .append("        TestSuite testSuite = new TestSuite(\"1\");\n\n");

        for (ResOp resOp : plan.getResOps()) {
            writeTestCode(resOp,javaCode);
        }
        // Set up the test context and execute the tests
        javaCode.append("        TestContext context = new TestContext();\n")
                .append("        context.setUsername(\"admin@rasp.com\");\n")
                .append("        context.setPassword(\"admin@123\");\n")
                .append("        context.setWebserver(\"http://localhost:8081\");\n")
                .append("        TestManager.getInstance().execute(context, testSuite);\n")
                .append("        TestManager.getInstance().report();\n");

        // Closing the class
        javaCode.append("    }\n")
                .append("}\n");

        // Write the generated test class code to a file
        FileWriter fileWriter = new FileWriter(new File(outputDir, className + ".java"));
        fileWriter.write(javaCode.toString());
        fileWriter.close();

        System.out.println("Test file for " + className + " plan generated successfully!");
    }

    // Helper method to capitalize the first letter of a string
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    private void writeTestCode(ResOp resop,StringBuilder javaCode)
    {
        javaCode.append(JavaCodeWriter.getCodeWriter(resop).giveCode(resop));

    }


}
