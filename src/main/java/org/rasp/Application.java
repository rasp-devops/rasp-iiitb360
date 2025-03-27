package org.rasp;
import org.rasp.iiitb720.Registry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
@ComponentScan(basePackages = {"controller", "platform.webservice.map", "platform.webservice.controller.base"})
public class Application {
    public static void main(String[] args) {
        Registry.register();
        System.out.println("Running Application....");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }
}