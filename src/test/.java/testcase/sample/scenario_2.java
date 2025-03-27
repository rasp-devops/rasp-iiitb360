package testcase.sample;

import org.rasp.iiitb720.resource.*;
import platform.resource.BaseResource;
import platform.testcase.*;

import java.util.HashMap;
import java.util.Map;

public class scenario_2 {
    public static void main(String[] args) {
        TestSuite testSuite = new TestSuite("2");
        
        // Create ResA
        Interface inter = new Interface();
        String interName = "interface_1";
        inter.setName(interName);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", interName);
        testSuite.add(new TestCase("1","add-interface", inter, Interface.class, "add", map1));

        // Create ResD
        Auth auth = new Auth();
        String authName = "auth_1";
        auth.setName(authName);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", authName);
        testSuite.add(new TestCase("2","add-auth", auth, Auth.class, "add", map2));

        // Create 3 ResC
        Availability avail1 = new Availability();
        String avail1Name = "avail_1";
        String avail1Id = "1";
        avail1.setId(avail1Id);
        avail1.setName(avail1Name);
        avail1.setInterface_id(inter.getId());
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name", avail1Name);
        map3.put("id", avail1Id);
        testSuite.add(new TestCase("3","add-avail1", avail1, Availability.class, "add", map3));

        Availability avail2 = new Availability();
        String avail2Name = "avail_2";
        String avail2Id = "2";
        avail2.setId(avail2Id);
        avail2.setInterface_id(inter.getId());
        avail2.setName(avail2Name);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("name", avail2Name);
        testSuite.add(new TestCase("4","add-avail2", avail2, Availability.class, "add",map4));

        Availability avail3 = new Availability();
        String avail3Name = "avail_4";
        String avail3Id = "3";
        avail3.setId(avail3Id);
        avail3.setName("avail3");
        avail1.setInterface_id(inter.getId());
        Map<String, Object> map5 = new HashMap<>();
        map5.put("name", avail3Name);
        testSuite.add(new TestCase("5","add-avail3", avail3, Availability.class, "add",map5));

        // Modify ResA
        inter.setName("Modified_Interface");
        map1.put("name", inter.getName());
        testSuite.add(new TestCase("6","modify-inter", inter, Availability.class, "modify",map1));
        // Modify ResD
        auth.setId("Modified_Auth");
        map2.put("name", auth.getName());
        testSuite.add(new TestCase("7","modify-auth", auth, Availability.class, "modify",map2));
        // Modify ResC
        avail2.setName("Modified_Avail");
        map4.put("name", avail2.getName());
        testSuite.add(new TestCase("8","modify-avail", avail2, Availability.class, "modify",map4));

        // Get all C
//        BaseResource as = new Availability();
////        Map<String, String> bmap = new HashMap<>();
//        testSuite.add(new TestCase("9", "get-all-avails", as, Availability.class, "GET_ALL", null));



        //Adding reservation
        Reservation res1 = new Reservation();
        String res1Name = "res_1";
        String res1Id = "1";
        res1.setId(res1Id);
        res1.setName(res1Name);
        res1.setAvailability_id(avail1.getId());
        Map<String, Object> map6 = new HashMap<>();
        map6.put("name", res1Name);
        testSuite.add(new TestCase("15","add-res1", res1, Reservation.class, "add", map6));

        Reservation res2 = new Reservation();
        String res2Name = "res_2";
        String res2Id = "2";
        res2.setId(res2Id);
        res2.setName(res2Name);
        res2.setAvailability_id(avail2.getId());
        Map<String, Object> map7 = new HashMap<>();
        map7.put("name", res2Name);
        testSuite.add(new TestCase("16","add-res2", res2, Reservation.class, "add", map7));

        // Adding Payments
        Payment pay1 = new Payment();
        String pay1Name = "pay_1";
        String pay1Id = "1";
        pay1.setId(pay1Id);
        pay1.setName(pay1Name);
        pay1.setInterface_id(inter.getId());
        pay1.setAuth_id(auth.getId());
        pay1.setReservation_id(res1.getId());
        Map<String, Object> map8 = new HashMap<>();
        map8.put("name", pay1Name);
        testSuite.add(new TestCase("17","add-pay1", pay1, Payment.class, "add", map8));

        Payment pay2 = new Payment();
        String pay2Name = "pay_2";
        String pay2Id = "2";
        pay2.setId(pay2Id);
        pay2.setName(pay2Name);
        pay2.setInterface_id(inter.getId());
        pay2.setAuth_id(auth.getId());
        pay2.setReservation_id(res2.getId());
        Map<String, Object> map9 = new HashMap<>();
        map9.put("name", pay2Name);
        testSuite.add(new TestCase("18","add-pay2", pay2, Payment.class, "add", map9));

        //Modifying Payments

        pay1.setName("Modified_Payment");
        map8.put("name", pay1.getName());
        testSuite.add(new TestCase("19","modify-pay", pay1, Payment.class, "modify",map8));


        // Creating Notification

        Notification notif1 = new Notification();
        String notif1Name = "pay_1";
        String notif1Id = "1";
        notif1.setId(notif1Id);
        notif1.setName(notif1Name);
        notif1.setAvailability_id(avail1.getId());
        Map<String, Object> map10 = new HashMap<>();
        map10.put("name", notif1Name);
        testSuite.add(new TestCase("20","add-notif1", notif1, Notification.class, "add", map10));

        // Modifying Notification

        notif1.setName("Modified_Notif");
        map10.put("name", notif1.getName());
        testSuite.add(new TestCase("21","modify-notif", notif1, Notification.class, "modify",map10));

        BaseResource nt = new Notification();
//        testSuite.add(new TestCase("27","Getall",nt,Notification.class,"GET_ALL",null));

//        BaseResource as = new Notification();
//        testSuite.add(new TestCase("9", "Getall", as, Notification.class, "GET_ALL", null));

        // Delete created resources

        testSuite.add(new TestCase("10","delete-interface", inter, Interface.class,"delete",null));
        testSuite.add(new TestCase("11","delete-auth", auth, Auth.class,"delete",null));
        testSuite.add(new TestCase("12","delete-avail", avail1, Availability.class,"delete",null));
        testSuite.add(new TestCase("13","delete-avail", avail2, Availability.class,"delete",null));
        testSuite.add(new TestCase("14","delete-avail", avail3, Availability.class,"delete",null));
        testSuite.add(new TestCase("22","delete-notif", notif1, Notification.class,"delete",null));
        testSuite.add(new TestCase("23","delete-pay", pay1, Payment.class,"delete",null));
        testSuite.add(new TestCase("24","delete-pay", pay2, Payment.class,"delete",null));
        testSuite.add(new TestCase("25","delete-res", res1, Reservation.class,"delete",null));
        testSuite.add(new TestCase("26","delete-res", res2, Reservation.class,"delete",null));


        TestContext context = new TestContext();
        context.setUsername("admin@rasp.com");
        context.setPassword("admin@123");
        context.setWebserver("http://localhost:8081");
        TestManager.getInstance().execute(context,testSuite);
        TestManager.getInstance().report();
    }
}
