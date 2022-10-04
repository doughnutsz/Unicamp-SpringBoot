package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.CategoryDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryDao findCategoryById(Long id);

    CategoryDao findCategoryByName(String name);

    void insertCategory(String name);

    void updateCategory(String id, String name);

    List<CategoryDao> listCategory();
}
