import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

class DependencyManager {
    private Map<String, List<String>> resourceMap;

    public DependencyManager() {
        resourceMap = new HashMap<>();
    }

    public void processFolder(String folderPath,String resourcePath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName().replace(".java", "");

                // Skip files with "Result" suffix
                if (fileName.endsWith("Result")) {
                    continue;
                }

                String className = resourcePath+"."+fileName;

                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.getDeclaredConstructor().newInstance();

                    // Access the getMetaData() method
                    Method getMetaDataMethod = clazz.getMethod("getMetaData");
                    Object metaData = getMetaDataMethod.invoke(instance);

                    // Access the getFieldsArray() method from metaData
                    Method getFieldsArrayMethod = metaData.getClass().getMethod("getFieldsArray");
                    Object[] fieldsArray = (Object[]) getFieldsArrayMethod.invoke(metaData);

                    List<String> resources = new ArrayList<>();

                    for (Object field : fieldsArray) {
                        Method getForeignMethod = field.getClass().getMethod("getForeign");
                        Object foreign = getForeignMethod.invoke(field);

                        if (foreign != null) {
                            Method getResourceMethod = foreign.getClass().getMethod("getResource");
                            String resource = (String) getResourceMethod.invoke(foreign);
                            resources.add(toCamelCase(resource));
                        }
                    }

                    resourceMap.put(fileName, resources);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            System.err.println("No Java files found in the specified folder.");
        }
    }


    public Map<String, List<String>> getAllDependencies() {
        return new HashMap<>(resourceMap);
    }

    private String toCamelCase(String input) {
        String[] parts = input.split("_");
        StringBuilder camelCaseString = new StringBuilder();

        for (String part : parts) {
            if (part.length() > 0) {
                camelCaseString.append(part.substring(0, 1).toUpperCase());
                camelCaseString.append(part.substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }
}