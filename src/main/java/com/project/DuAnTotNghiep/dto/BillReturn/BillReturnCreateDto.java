package com.project.DuAnTotNghiep.dto.BillReturn;

import com.project.DuAnTotNghiep.dto.Product.ProductDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class BillReturnCreateDto {
    private Long billId;
    private String reason;
    private Boolean shipping;
}
