package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import platform.defined.helper.UserHelper;
import platform.defined.resource.User;


@Controller
public class CreateUserController {

    void create_default_user() {
        User user = new User("admin@rasp.com");
        user.setType("SUPER_ADMIN");
        user.setEmail_id("admin@rasp.com");
        user.setMobile_no("123456789");
        user.setPassword("admin@123");
        user.setName("Super Admin");
        UserHelper.getInstance().AddOrUpdateNoCache(user);
    }

    @RequestMapping(value = "/ui/default_user", method = RequestMethod.GET)
    public @ResponseBody String doGet() {
        create_default_user();
        return "created created successfully";
    }
}