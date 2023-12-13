package mn.michal.onlineshopapp.controller;

import jakarta.validation.Valid;
import mn.michal.onlineshopapp.model.dto.ProductDTO;
import mn.michal.onlineshopapp.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    HttpEntity<List<ProductDTO>> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    HttpEntity<ProductDTO> getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

//    @PutMapping("/{id}")
//    HttpEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductDTO product) {
//        return productService.updateProduct(id, product);
//    }

    @DeleteMapping("/{id}")
    HttpEntity<?> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }

    @PostMapping
    HttpEntity<?> addProduct(@RequestBody @Valid ProductDTO product) {
        return productService.addProduct(product);
    }
}
