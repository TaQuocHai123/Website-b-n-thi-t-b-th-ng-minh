package com.project.DuAnTotNghiep.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/guide-order")
    public String guideOrder(Model model) {
        // any model attributes can be added here if needed later
        return "user/guide-order";
    }

    @GetMapping("/policy-check")
    public String policyCheck(Model model) {
        return "user/policy-check";
    }

    @GetMapping("/policy-pay")
    public String policyPay(Model model) {
        return "user/policy-pay";
    }

    @GetMapping("/policy-ship")
    public String policyShip(Model model) {
        return "user/policy-ship";
    }

    @GetMapping("/policy-return")
    public String policyReturn(Model model) {
        return "user/policy-return";
    }

    @GetMapping("/policy-complaint")
    public String policyComplaint(Model model) {
        return "user/policy-complaint";
    }
}
