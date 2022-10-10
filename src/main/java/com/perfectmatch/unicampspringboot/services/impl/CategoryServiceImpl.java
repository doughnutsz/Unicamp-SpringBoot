package com.perfectmatch.unicampspringboot.services.impl;

import com.perfectmatch.unicampspringboot.db.SubCategoryDao;
import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.mapper.CategoryMapper;
import com.perfectmatch.unicampspringboot.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryServices {
    @Autowired
    CategoryMapper categoryMapper;

    public List<SubCategoryDao> listSubCategory() {
        return categoryMapper.listSubCategory();
    }

    public List<CategoryDao> listCategory() {
        return categoryMapper.listCategory();
    }

    public SubCategoryDao getSubCategoryById(Long id) {
        return categoryMapper.findSubCategoryById(id);
    }

//    public SubCategoryDao getCategoryByName(String name) {
//        return categoryMapper.findCategoryByName(name);
//    }
//
//    public void addCategory(String name) {
//        categoryMapper.insertCategory(name);
//    }
//
//    public void updateCategory(Long id, String name) {
//        categoryMapper.updateCategory(id.toString(), name);
//    }
}
