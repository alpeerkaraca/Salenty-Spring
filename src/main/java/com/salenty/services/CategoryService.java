package com.salenty.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salenty.model.Category;
import com.salenty.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public String getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId).get().getCategoryName();
    }
}
