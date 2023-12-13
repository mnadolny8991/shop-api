package mn.michal.onlineshopapp.controller;

import jakarta.validation.Valid;
import mn.michal.onlineshopapp.model.entity.Category;
import mn.michal.onlineshopapp.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    HttpEntity<List<Category>> getCategories(Pageable pageable) {
        return categoryService.getCategories(pageable);
    }

    @PostMapping
    HttpEntity<?> addCategory(@RequestBody @Valid Category category) {
        return categoryService.addCategory(category);
    }
}
