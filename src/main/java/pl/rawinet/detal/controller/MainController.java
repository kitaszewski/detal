package pl.rawinet.detal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.rawinet.detal.service.NoticeServiceImpl;

@Controller
public class MainController {
    @Autowired
    NoticeServiceImpl noticeService;

    @GetMapping("/")
    public ModelAndView getMain() {
        ModelAndView m = new ModelAndView();
        m.addObject("notices", noticeService.getAllCustomersNotices(99999));
        m.setViewName("index");
        return m;
    }

    @GetMapping("/sandbox")
    public ModelAndView showSandbox() {
        ModelAndView m = new ModelAndView();
        m.addObject("txt", "brak zadan");
        m.setViewName("sandbox");
        return m;
    }
}
