package testcase.sample;

import org.rasp.iiitb720.resource.Interface;
import platform.resource.BaseResource;
import platform.testcase.*;

import java.util.HashMap;
import java.util.Map;

public class scenario_1 {

    public static void main(String[] args) {
        TestSuite testSuite = new TestSuite("1");

        Interface inter = new Interface();
        String interName = "interface_1";
        inter.setName(interName);
        Map<String, Object> map = new HashMap<>();
        map.put("name", interName);
        testSuite.add(new TestCase("1","add-interface", inter, Interface.class, "add",null));

        TestContext context = new TestContext();
        context.setUsername("admin@rasp.com");
        context.setPassword("admin@123");
        context.setWebserver("http://localhost:8081");
        TestManager.getInstance().execute(context,testSuite);
        TestManager.getInstance().report();
    }
}