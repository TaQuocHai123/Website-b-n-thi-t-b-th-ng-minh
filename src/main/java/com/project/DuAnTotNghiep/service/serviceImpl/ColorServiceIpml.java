package com.project.DuAnTotNghiep.service.serviceImpl;

import com.project.DuAnTotNghiep.dto.Color.ColorDto;
import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.Size;
import com.project.DuAnTotNghiep.exception.NotFoundException;
import com.project.DuAnTotNghiep.exception.ShopApiException;
import com.project.DuAnTotNghiep.repository.ColorRepo;
import com.project.DuAnTotNghiep.repository.ProductRepository;
import com.project.DuAnTotNghiep.repository.SizeRepository;
import com.project.DuAnTotNghiep.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceIpml implements ColorService {

    @Autowired
    private ColorRepo colorRepo;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Color updateColor(Color color) {
        Color existingColor = colorRepo.findById(color.getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy màu có mã " + color.getCode()) );
        if(!existingColor.getCode().equals(color.getCode())) {
            if(colorRepo.existsByCode(color.getCode())) {
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu " + color.getCode() + " đã tồn tại");
            }
        }
        color.setDeleteFlag(false);
        return colorRepo.save(color);
    }

    @Override
    public Color createColor(Color color) {
        if (color.getCode() == null || color.getCode().trim().isEmpty()) {
            // Generate new code if not provided
            Color lastColor = colorRepo.findFirstByOrderByIdDesc();
            Long nextId = (lastColor == null) ? 1L : lastColor.getId() + 1;
            String colorCode = "MS" + String.format("%04d", nextId);
            color.setCode(colorCode);
        } else {
            String code = color.getCode().trim();
            Optional<Color> existing = colorRepo.findByCode(code);
            if (existing.isPresent()) {
                Color ex = existing.get();
                // If existed but was soft-deleted, restore it and update name
                if (ex.getDeleteFlag() != null && ex.getDeleteFlag()) {
                    ex.setDeleteFlag(false);
                    ex.setName(color.getName());
                    return colorRepo.save(ex);
                }
                throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu " + code + " đã tồn tại");
            }
            color.setCode(code);
        }
        color.setDeleteFlag(false);
        return colorRepo.save(color);
    }

    @Override
    public void delete(Long id) {
        Color existingColor = colorRepo.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy màu có id " + id) );
        existingColor.setDeleteFlag(true);
        colorRepo.save(existingColor);
    }

    @Override
    public List<Color> findAll() {
        return colorRepo.findAllByDeleteFlagFalse();
    }

    @Override
    public Optional<Color> findById(Long id) {
            return colorRepo.findById(id).map(color -> {
                if (color.getDeleteFlag() == null || !color.getDeleteFlag()) {
                    return color;
                }
                return null;
            });
    }

    @Override
    public boolean existsById(Long id) {
        return colorRepo.existsById(id);
    }

    @Override
    public Page<Color> findAll(Integer page, Integer limit) {
        Pageable pageable= PageRequest.of(page,limit);
        return colorRepo.findAll(pageable);
    }

    @Override
    public Page<Color> findAll(Pageable pageable) {
        return colorRepo.findAllByDeleteFlagFalse(pageable);
    }

    @Override
    public List<Color> getColorByProductId(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        return colorRepo.findColorsByProduct(product);
    }

    @Override
    public List<Color> getColorsByProductIdAndSizeId(Long productId, Long sizeId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new NotFoundException("Size not found"));
        return colorRepo.findColorsByProductAndSize(product, size);
    }

    @Override
    public ColorDto createColorApi(ColorDto colorDto) {
        if (colorDto.getCode() == null || colorDto.getCode().trim().isEmpty()) {
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu không được để trống");
        }
        String code = colorDto.getCode().trim();
        Optional<Color> existing = colorRepo.findByCode(code);
        if (existing.isPresent()) {
            Color ex = existing.get();
            if (ex.getDeleteFlag() != null && ex.getDeleteFlag()) {
                ex.setDeleteFlag(false);
                ex.setName(colorDto.getName());
                Color saved = colorRepo.save(ex);
                return new ColorDto(saved.getId(), saved.getCode(), saved.getName());
            }
            throw new ShopApiException(HttpStatus.BAD_REQUEST, "Mã màu đã tồn tại");
        }
        Color color = new Color(null, code, colorDto.getName(), false);
        Color colorNew = colorRepo.save(color);
        return new ColorDto(colorNew.getId(), colorNew.getCode(), colorNew.getName());
    }
}
