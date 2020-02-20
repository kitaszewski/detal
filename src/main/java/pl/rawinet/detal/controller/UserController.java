package pl.rawinet.detal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.model.User;
import pl.rawinet.detal.service.UserServiceImpl;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @GetMapping("/chpass")
    public ModelAndView showChangePassForm(){
        ModelAndView m = new ModelAndView();
        m.setViewName("chpass");
        return m;
    }

    @PostMapping("/chpass")
    public ModelAndView changePassword(@RequestParam String newPass, @RequestParam String confirmNewPass
                                    , @RequestParam String login){
        ModelAndView m = new ModelAndView();
        User u = userService.findUserByLogin(login);

        if(newPass.equals(confirmNewPass) && !newPass.isEmpty()){
            m.addObject("txt", "ok");
            u.setPassword(newPass);
            userService.saveUser(u);
        } else {
            m.addObject("txt", "error");
        }
        m.setViewName("chpass");
        return m;
    }
}
