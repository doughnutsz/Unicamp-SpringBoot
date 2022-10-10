package com.perfectmatch.unicampspringboot.services;

import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.db.SubCategoryDao;

import java.util.List;

public interface CategoryServices {
    List<SubCategoryDao> listSubCategory();

    List<CategoryDao> listCategory();

    SubCategoryDao getSubCategoryById(Long id);
//
//    SubCategoryDao getCategoryByName(String name);
//
//    void addCategory(String name);
//
//    void updateCategory(Long id, String name);
}
