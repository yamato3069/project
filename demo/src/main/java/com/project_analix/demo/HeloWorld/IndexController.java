package com.project_analix.demo.HeloWorld;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class IndexController {

    @RequestMapping(value = {"/", ""})
    public String index(Model model) {
        model.addAttribute("message", "ハローワールド!");
        return "index";
    }

}
