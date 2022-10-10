package com.perfectmatch.unicampspringboot.mapper;

import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.db.SubCategoryDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    SubCategoryDao findSubCategoryById(Long id);

//    SubCategoryDao findCategoryByName(String name);
//
//    void insertCategory(String name);
//
//    void updateCategory(String id, String name);

    List<CategoryDao> listCategory();

    List<SubCategoryDao> listSubCategory();
}
