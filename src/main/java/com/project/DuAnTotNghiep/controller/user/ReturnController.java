package com.project.DuAnTotNghiep.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.project.DuAnTotNghiep.dto.BillReturn.BillReturnCreateDto;
import com.project.DuAnTotNghiep.service.BillReturnService;

@Controller
@RequestMapping("/user/return")
public class ReturnController {
    @Autowired
    private BillReturnService billReturnService;

    @GetMapping("")
    public String showReturnPage(Model model) {
        model.addAttribute("billReturnForm", new BillReturnCreateDto());
        // Thêm các thông tin cần thiết như danh sách đơn hàng
        return "user/return.html";
    }

    @PostMapping("")
    public String submitReturn(@ModelAttribute("billReturnForm") BillReturnCreateDto billReturnForm, Model model) {
        try {
            billReturnService.createBillReturn(billReturnForm);
            model.addAttribute("message", "Yêu cầu đổi trả đã được gửi thành công!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/return.html";
    }
}
