package CodeWriters;
import java.util.*;
import ResourceOperations.*;

public class ReadCodeWriter extends JavaCodeWriter{
    public StringBuilder giveCode(ResOp r1){
        StringBuilder javaCode = new StringBuilder();
        ReadResOp resop = (ReadResOp)r1;
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
        String filter_name = resop.getName() + "_filters";
        javaCode.append("\t\tMap<String, String> ").append(filter_name).append(" = new HashMap<>();\n");

        for (Map.Entry<String, String> entry : resop.getFilters().entrySet()) {
            javaCode.append("\t\t").append(filter_name).append(".put(\"")
                    .append(entry.getKey()).append("\", \"")
                    .append(entry.getValue()).append("\");\n");
        }

        javaCode.append("\t\ttestSuite.add(new TestCase(\"3\", \"GETALL\", ")
                .append(resop.getResource()).append(".class, ")
                .append(filter_name).append(",").append(expectedValuesMapName).append("));\n");
        return javaCode;
    }
}