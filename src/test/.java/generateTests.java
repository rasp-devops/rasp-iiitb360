import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

public class generateTests {

    public static void main(String[] args) {
        String jsonFilePath = "src/main/java/org/rasp/iiitb720/batch.json"; // Path to your JSON schema file
        String outputDir = "src/test/java/generated"; // Directory to save generated Java files

        try {
            // Create output directory if it does not exist
            File dir = new File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Parse the JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(jsonFilePath));

            // Get the first key in the JSON as the resource name
            Iterator<String> fieldNames = rootNode.fieldNames();
            String resourceName = fieldNames.next();  // The first key in the JSON
            JsonNode resourceNode = rootNode.path(resourceName);

            String className = capitalize(resourceName) + "Test";

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

            // Initialize the resource
            String resourceVarName = resourceName.toLowerCase();
            javaCode.append("        ").append(capitalize(resourceName)).append(" ").append(resourceVarName).append(" = new ").append(capitalize(resourceName)).append("();\n");

            JsonNode fieldsNode = resourceNode.path("fields");
            Iterator<Entry<String, JsonNode>> fields = fieldsNode.fields();
            String keyFieldName = "";

            while (fields.hasNext()) {
                Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                JsonNode fieldDetails = field.getValue();

                // Check if the field is transient
                boolean isTransient = fieldDetails.path("transient_field").asBoolean(false);
                if (isTransient) {
                    continue; // Skip transient fields
                }

                String fieldType = mapJsonTypeToJavaType(fieldDetails.path("type").asText());
                boolean isRequired = fieldDetails.path("required").asBoolean();

                if (isRequired && keyFieldName.isEmpty()) {
                    keyFieldName = fieldName;  // Assume the first required field is the key field
                }

                // Set the field value in the resource
                javaCode.append("        ").append(resourceVarName).append(".set").append(capitalize(fieldName))
                        .append("(\"").append(fieldName).append("Value\");\n");
            }

            // Add the test case
            javaCode.append("\n        Map<String, Object> map = new HashMap<>();\n")
                    .append("        map.put(\"").append(keyFieldName).append("\", ").append(resourceVarName).append(".get").append(capitalize(keyFieldName)).append("());\n")
                    .append("        testSuite.add(new TestCase(\"1\",\"add-").append(resourceVarName).append("\", ")
                    .append(resourceVarName).append(", ").append(capitalize(resourceName)).append(".class, \"add\", map));\n\n");

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

            System.out.println("Test file generated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String mapJsonTypeToJavaType(String jsonType) {
        switch (jsonType) {
            case "String":
                return "String";
            case "Boolean":
                return "boolean";
            case "Integer":
            case "int":
                return "int";
            case "Long":
                return "long";
            case "Double":
                return "double";
            default:
                return "String"; // Default to String if type is unknown
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
