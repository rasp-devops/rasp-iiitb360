public class TestPlanner_config
{
    String resourcePath = "org.rasp.iiitb720.resource";
    //    public TestPlan plan(DependencyGraph depGraph, ResourceMetaData resourceMetaData) {
    public static void main(String[] args)
    {
        TestPlanner_config testPlanner = new TestPlanner_config();
        TestConfig testConfig = new TestConfig();
        testConfig.parseYamlFile("src/test/java/config1.yaml");
        TestGenerator tg = new TestGenerator();
        try {
            tg.generate(testConfig.getTestPlan());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}