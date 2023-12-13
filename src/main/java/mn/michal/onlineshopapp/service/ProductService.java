package mn.michal.onlineshopapp.service;

import mn.michal.onlineshopapp.model.dto.ProductDTO;
import mn.michal.onlineshopapp.model.entity.Category;
import mn.michal.onlineshopapp.model.entity.Product;
import mn.michal.onlineshopapp.model.repository.CategoryRepository;
import mn.michal.onlineshopapp.model.repository.ProductRepository;
import mn.michal.onlineshopapp.service.mapper.DTOProductMapper;
import mn.michal.onlineshopapp.service.mapper.ProductDTOMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductDTOMapper productDTOMapper;
    DTOProductMapper dtoProductMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductDTOMapper productDTOMapperService,
                          DTOProductMapper dtoProductMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productDTOMapper = productDTOMapperService;
        this.dtoProductMapper = dtoProductMapper;
    }

    public HttpEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productRepository.findAll(pageable)
                .stream()
                .map(productDTOMapper)
                .toList());
    }

    public HttpEntity<ProductDTO> getProduct(Long id) {
        return productRepository.findById(id)
                .map(productDTOMapper)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public HttpEntity<?> deleteProduct(Long id) {
        var productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private HttpEntity<?> errorEntity(String message) {
        return ResponseEntity
                .status(HttpStatus.valueOf(409))
                .body(message);
    }



    public HttpEntity<?> addProduct(ProductDTO productDTO) {
        Product product = dtoProductMapper.apply(productDTO);
        if (productDTO.getId() != null) {
            if (productRepository.existsById(productDTO.getId())) {
                return errorEntity("Product with such id already exists");
            }
            product.setId(product.getId());
        }
        if (productRepository.existsByName(productDTO.getName())) {
            return errorEntity("Product with such name already exists");
        }
        Long categoryId = productDTO.getCategoryId();
        if (categoryId != null) {
            var categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) return errorEntity("There is no such category");
            categoryOpt.ifPresent(product::setCategory);
        }
        productRepository.save(product);
        return ResponseEntity.created(URI.create("/customer/" + product.getId())).build();
    }
}
