import platform.defined.helper.UserHelper;
import platform.defined.resource.User;

public class test {
    public static void main(String[] args){
        User user = new User("admin@rasp.com");
        user.setType("SUPER_ADMIN");
        user.setEmail_id("admin@rasp.com");
        user.setMobile_no("123456789");
        user.setPassword("admin@123");
        user.setName("Super Admin");
        UserHelper.getInstance().AddOrUpdateNoCache(user);
    }
}
