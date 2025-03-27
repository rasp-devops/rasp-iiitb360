import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DependencyFinder {

    // Method to process all JSON files in a folder and return a map of dependencies
    public Map<String, Set<String>> processFolder(String folderPath) {
        Map<String, Set<String>> dependenciesMap = new HashMap<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    JSONObject jsonObject = readJSONFromFile(file.getAbsolutePath());
                    Set<String> dependencies = findDependencies(jsonObject);
                    dependenciesMap.put(file.getName(), dependencies);
                } catch (IOException e) {
                    System.err.println("Error reading the JSON file " + file.getName() + ": " + e.getMessage());
                }
            }
        } else {
            System.err.println("No JSON files found in the specified folder.");
        }

        return dependenciesMap;
    }

    // Method to read JSON from a file
    public JSONObject readJSONFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        StringBuilder sb = new StringBuilder();
        int ch;
        while ((ch = fileReader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        return new JSONObject(sb.toString());
    }

    // Method to find dependencies in a JSONObject
    public Set<String> findDependencies(JSONObject jsonObject) {
        Set<String> dependencies = new HashSet<>();
        findDependenciesRecursive(jsonObject, dependencies);
        return dependencies;
    }

    private void findDependenciesRecursive(JSONObject jsonObject, Set<String> dependencies) {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                JSONObject nestedObject = (JSONObject) value;
                // Check for dependency indicators
                if (nestedObject.has("datasource")) {
                    JSONObject datasource = nestedObject.getJSONObject("datasource");
                    if (datasource.has("resource")) {
                        String resource = datasource.getString("resource");
                        dependencies.add(resource + ".json");  // Add .json to each dependency
                    }
                }
                // Recursively process nested objects
                findDependenciesRecursive(nestedObject, dependencies);
            }
        }
    }
}
