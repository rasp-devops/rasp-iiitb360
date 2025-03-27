package ResourceOperations;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateResOp extends ResOp{
    private String instanceName;
    private Map<String, String> fieldValues;
    private Map<String, String> dependencies;

    // Empty constructor
    public CreateResOp() {
    }

    // Parameterized constructor
    public CreateResOp(String resource, String operation, Map<String, String> fieldValues, String instance,Map<String,Object> expectedValues) {
        super.setResource(resource);
        super.setOperation(operation);
        this.fieldValues = fieldValues;
        this.instanceName = instance;
        super.setExpectedValues(expectedValues);
    }

    // Getter for instanceName
    public String getInstanceName() {
        return instanceName;
    }

    // Setter for instanceName
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Map<String, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public static Map<String,String> processMetadata(String resource,Map<String,String> dependencies) {
        try {
            // Class and instance creation
            String resourcePath = "org.rasp.iiitb720.resource";
            String className = resourcePath + "." + resource;
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // Access the getMetaData() method
            Method getMetaDataMethod = clazz.getMethod("getMetaData");
            Object metaData = getMetaDataMethod.invoke(instance);

            // Access the getFieldsArray() method from metaData
            Method getFieldsArrayMethod = metaData.getClass().getMethod("getFieldsArray");
            Object[] fieldsArray = (Object[]) getFieldsArrayMethod.invoke(metaData);

            Map<String,String> fieldValues = new HashMap<>();
            for (Object field : fieldsArray) {
                // Assuming `field` has methods `isRequired`, `getType`, and `getName`
                Method isRequiredMethod = field.getClass().getMethod("isRequired");
                Method getTypeMethod = field.getClass().getMethod("getType");
                Method getNameMethod = field.getClass().getMethod("getName");
                Method getForeignMethod = field.getClass().getMethod("getForeign");

                boolean isRequired = (boolean) isRequiredMethod.invoke(field);
                String fieldType = (String) getTypeMethod.invoke(field);
                String fieldName = (String) getNameMethod.invoke(field);
                Object foreign =  getForeignMethod.invoke(field);
                if(fieldName.equalsIgnoreCase("id"))
                {
                    continue;
                }
                else if (isRequired && foreign==null) {
                    // Assign a dummy value based on the field type and convert to String
                    String dummyValue;
                    if (fieldType.equals("int") || fieldType.equals("Integer")) {
                        dummyValue = "\""+randomInt(2)+"\"";
                    } else if (fieldType.equals("float") || fieldType.equals("Float")) {
                        dummyValue = "\""+randomFloat(2)+"\"";
                    } else if (fieldType.equals("boolean") || fieldType.equals("Boolean")) {
                        dummyValue = "\""+randomBoolean()+"\"";
                    } else if (fieldType.equals("String")) {
                        dummyValue = "\""+randomString(5)+"\"";
                    } else {
                        dummyValue = "null";  // Default for other types
                    }
                    fieldValues.put(fieldName, dummyValue);
                }
                if(foreign!=null)
                {
                    String foreignClass = (String) foreign.getClass().getMethod("getResource").invoke(foreign);
//                    System.out.printf(foreignClass);
//                    System.out.println(" is the foreign class name");
                    fieldValues.put(fieldName,dependencies.get(foreignClass)+".getId()");
                }
            }
            return fieldValues;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception as needed, e.g., log it or throw a custom exception
        }
        return null;
    }

    public static String randomString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
    public static String randomInt(int n)
    {
        //n is the number of digits
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(10 * Math.random());
            sb.append(index);
        }
        return sb.toString();
    }
    public static String randomFloat(int n)
    {
        //n is the number of digits
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(10 * Math.random());
            sb.append(index);
        }
        sb.append(".");
        for (int i = 0; i < 2; i++) {
            int index = (int)(10 * Math.random());
            sb.append(index);
        }
        return sb.toString();
    }
    public static String randomBoolean()
    {
        int index = (int)(2 * Math.random());
        if(index==0)
        {
            return "true";
        }
        else
        {
            return "false";
        }
    }
}
