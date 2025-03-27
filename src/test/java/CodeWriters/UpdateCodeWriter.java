package CodeWriters;

import ResourceOperations.*;

import java.util.Map;

public class UpdateCodeWriter extends JavaCodeWriter {

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public StringBuilder giveCode(ResOp r1) {
        StringBuilder javaCode = new StringBuilder();
        UpdateResOp resop = (UpdateResOp) r1;
        String expectedValuesMapName = null;
        if (resop.getExpectedValues() != null)
        {
            expectedValuesMapName = resop.getName() + "_expectedValues";
            javaCode.append("\t\tMap<String, Object> ").append(expectedValuesMapName).append(" = new HashMap<>();\n");
            for (Map.Entry<String, Object> entry : resop.getExpectedValues().entrySet()) {
                javaCode.append("\t\t").append(expectedValuesMapName).append(".put(\"")
                        .append(entry.getKey()).append("\", \"")
                        .append(entry.getValue()).append("\");\n");
            }
        }

//        javaCode.append("        " + resop.getResource() + " " + resop.getInstanceName() + " = getExisting" + resop.getResource() + "Instance();\n");

        for (Map.Entry<String, String> entry : resop.getFieldValues().entrySet()) {
            javaCode.append("        " + resop.getInstanceName() + ".set" + capitalize(entry.getKey()) + "(" + entry.getValue() + ");\n");
        }

        if (resop.getOperation().equals("U")) {
            javaCode.append("        testSuite.add(new TestCase(\"1\", \"modify-" + resop.getInstanceName() + "\", " + resop.getInstanceName() + ", " + resop.getResource() + ".class, \"modify\", null));\n");
        }

        return javaCode;
    }
}
