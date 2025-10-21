package com.project.DuAnTotNghiep.controller.user;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @Value("${maps.api.key:}")
    private String mapsApiKey;

    @GetMapping("getcontact")
    public String getContact(Model model) {
        model.addAttribute("mapsApiKey", mapsApiKey);
        return "user/contact";
    }
}
