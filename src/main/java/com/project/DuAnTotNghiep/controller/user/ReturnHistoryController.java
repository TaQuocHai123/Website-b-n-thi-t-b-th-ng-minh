package com.project.DuAnTotNghiep.controller.user;

import com.project.DuAnTotNghiep.dto.BillReturn.BillReturnCreateDto;
import com.project.DuAnTotNghiep.dto.BillReturn.BillReturnDto;
import com.project.DuAnTotNghiep.dto.BillReturn.SearchBillReturnDto;
import com.project.DuAnTotNghiep.service.BillReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user/return-history")
public class ReturnHistoryController {
    @Autowired
    private BillReturnService billReturnService;

    @GetMapping("")
    public String showReturnHistory(Model model, Principal principal) {
        SearchBillReturnDto searchDto = new SearchBillReturnDto();
        // Có thể set thêm điều kiện tìm kiếm theo tài khoản
        List<BillReturnDto> returns = billReturnService.getAllBillReturns(searchDto);
        model.addAttribute("returns", returns);
        return "user/return-history.html";
    }
}
