package ResourceOperations;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UpdateResOp extends ResOp {
    private String instanceName;
    private Map<String, String> fieldValues;
    private String name;

    // Empty constructor
    public UpdateResOp() {
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    // Parameterized constructor
    public UpdateResOp(String resource, String operation, Map<String, String> fieldValues, String instance) {
        super.setResource(resource);
        super.setOperation(operation);
        this.fieldValues = fieldValues;
        this.instanceName = instance;
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

    public static Map<String, String> processMetadata(String resource, Map<String, String> fieldValues, Map<String, String> dependencies) {
        try {
            // Load class dynamically
            String resourcePath = "org.rasp.iiitb720.resource";
            String className = resourcePath + "." + resource;
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();

            // Get metadata
            Method getMetaDataMethod = clazz.getMethod("getMetaData");
            Object metaData = getMetaDataMethod.invoke(instance);
            Method getFieldsArrayMethod = metaData.getClass().getMethod("getFieldsArray");
            Object[] fieldsArray = (Object[]) getFieldsArrayMethod.invoke(metaData);

            Map<String, String> updatedFields = new HashMap<>();

            for (Object field : fieldsArray) {
                Method getNameMethod = field.getClass().getMethod("getName");
                Method getForeignMethod = field.getClass().getMethod("getForeign");

                String fieldName = (String) getNameMethod.invoke(field);
                Object foreign = getForeignMethod.invoke(field);

                if (!fieldValues.containsKey(fieldName)) {
                    continue; // Skip fields the user does not want to update
                }

                if (foreign != null) {
                    String foreignClass = (String) foreign.getClass().getMethod("getResource").invoke(foreign);
                    updatedFields.put(fieldName, dependencies.get(foreignClass) + ".getId()");
                } else {
                    updatedFields.put(fieldName, "\"" + fieldValues.get(fieldName) + "\"");
                }
            }
            return updatedFields;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
