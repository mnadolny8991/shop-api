package mn.michal.onlineshopapp.service.mapper;

import mn.michal.onlineshopapp.model.dto.ProductDTO;
import mn.michal.onlineshopapp.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ProductDTOMapper implements Function<Product, ProductDTO> {

    @Override
    public ProductDTO apply(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory().getId()
        );
    }
}
