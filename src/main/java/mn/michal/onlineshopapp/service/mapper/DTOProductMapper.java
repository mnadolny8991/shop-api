package mn.michal.onlineshopapp.service.mapper;

import mn.michal.onlineshopapp.model.dto.ProductDTO;
import mn.michal.onlineshopapp.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DTOProductMapper implements Function<ProductDTO, Product> {

    @Override
    public Product apply(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        return product;
    }
}
