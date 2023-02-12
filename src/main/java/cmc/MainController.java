package cmc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/index.html")
    public String main1() throws Exception{
        return "main1";
    }

    @GetMapping("/")
    public String main2() throws Exception{
        return "main2";
    }

}