package mn.michal.onlineshopapp.service;

import mn.michal.onlineshopapp.model.entity.Category;
import mn.michal.onlineshopapp.model.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public HttpEntity<List<Category>> getCategories(Pageable pageable) {
        return ResponseEntity.ok(categoryRepository.findAll(pageable).stream().toList());
    }

    public HttpEntity<?> addCategory(Category category) {
        if (category.getId() != null) {
            if (categoryRepository.existsById(category.getId())) {
                return ResponseEntity.status(409).body("Category with this id already exists");
            }
        }
        categoryRepository.save(category);
        return ResponseEntity.created(
                URI.create("/categories/" + category.getId())
        ).build();
    }
}
