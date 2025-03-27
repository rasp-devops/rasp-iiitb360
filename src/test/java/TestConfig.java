import org.yaml.snakeyaml.Yaml;
import ResourceOperations.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConfig {
    private String name = "";
    List<ResOp> testUnits = new ArrayList<>();
    private TestPlan tp = new TestPlan();

    public void parseYamlFile(String yamlFileName)
    {

        Map<String,String> instanceToResource = new HashMap<>();
        try (InputStream inputStream = new FileInputStream(yamlFileName)) {
            // Parse YAML file into a Map
            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(inputStream);

            // Extract the "resOps" list from the YAML data
            String planName = (String) yamlData.get("name");
            this.name = planName;
            List<Map<String, Object>> resOpsList = (List<Map<String, Object>>) yamlData.get("Plan");
            // Iterate through the YAML data and create ResOp objects
            for (Map<String, Object> resOpData : resOpsList) {
                String resource = (String) resOpData.get("resource");
                String operation = (String) resOpData.get("operation");
                Map<String, Object> expectedValues = resOpData.containsKey("verify") ? (Map<String, Object>) resOpData.get("verify") : null;
                if (operation.equals("C"))
                {
                    String instanceName = (String) resOpData.get("instanceName");
                    List<String> dependentOn = (List<String>) resOpData.getOrDefault("dependentOn", new ArrayList<>());
                    instanceToResource.put(instanceName,resource.toLowerCase());
                    Map<String,String> dependencyMap = new HashMap<>();
                    for (String dependency: dependentOn)
                    {
                        dependencyMap.put(instanceToResource.get(dependency),dependency);
                    }
                    ResOp tu = new CreateResOp(resource,operation, CreateResOp.processMetadata(resource,dependencyMap), instanceName, expectedValues);
                    this.testUnits.add(tu);
                    tp.addResOp(tu);
                }
                else if(operation.equals("R"))
                {
                    List<Map<String, String>> filtersList = (List<Map<String, String>>) resOpData.getOrDefault("filters", new ArrayList<>());

                    Map<String, String> filters = new HashMap<>();
                    for(Map<String, String> filter: filtersList)
                    {
                        filters.putAll(filter);
                    }
//                    System.out.println(filters);
                    String name = (String) resOpData.get("name");
                    ResOp tu = new ReadResOp(resource,name,operation,filters, expectedValues);
                    tp.addResOp(tu);
                    this.testUnits.add(tu);
                }

                else if (operation.equals("U"))
                {
                    String instanceName = (String) resOpData.get("instanceName");
                    List<String> dependentOn = (List<String>) resOpData.getOrDefault("dependentOn", new ArrayList<>());

                    // Create a dependency map
                    Map<String, String> dependencyMap = new HashMap<>();
                    for (String dependency : dependentOn) {
                        dependencyMap.put(instanceToResource.get(dependency), dependency);
                    }

                    // Extract fieldValues (fields to update)
                    Map<String, String> fieldValues = (Map<String, String>) resOpData.getOrDefault("fields", new HashMap<>());

                    // Process metadata (only for the fields user wants to update)
                    Map<String, String> updatedFields = UpdateResOp.processMetadata(resource, fieldValues, dependencyMap);

                    // Create the UpdateResOp object
                    ResOp tu = new UpdateResOp(resource, operation, updatedFields, instanceName);

                    // Add to test plan and test units list
//                    this.testUnits.add(tu);
                    tp.addResOp(tu);
                }

            }
//            System.out.println(instanceToResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResOp> getList()
    {
        return testUnits;
    }
    public TestPlan getTestPlan()
    {
        tp.setPlanName(name);
        return tp;
    }

    public String getName()
    {
        return name;
    }
}