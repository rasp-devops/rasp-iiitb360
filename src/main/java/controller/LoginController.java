package controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import platform.resource.login;
import service.LoginService;

/**
 * Created by shraddha on 06-Oct-16.
 */
@Controller
@RequestMapping("/api/login")
class LoginController extends BaseController {
    protected boolean isLoginRequired() {
        return false;
    }
    public LoginController() {
        super(new login(),new LoginService());
    }
}
