package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.CategoryDao;

import java.util.List;

public interface CategoryServices {
    List<CategoryDao> listCategory();

    CategoryDao getCategoryById(Long id);

    CategoryDao getCategoryByName(String name);

    void addCategory(String name);

    void updateCategory(Long id, String name);
}
