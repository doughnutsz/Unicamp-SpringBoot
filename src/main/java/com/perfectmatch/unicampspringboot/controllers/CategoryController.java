package com.perfectmatch.unicampspringboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.db.SubCategoryDao;
import com.perfectmatch.unicampspringboot.services.CategoryServices;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class CategoryController {
    @Autowired
    CategoryServices categoryServices;

    //    static class CategoryUpdateBody {
//        private final Long id;
//        private final String name;
//
//        public CategoryUpdateBody(Long id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//    }
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    static class subCategoryView {
//        private Long subcategory_id;
//        private String subcategory_name;
//
//        public subCategoryView(SubCategoryDao dao) {
//            this.subcategory_id = dao.getId();
//            this.subcategory_name = dao.getName();
//        }
//    }
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    static class categoryView {
//        private Long category_id;
//        private String category_name;
//        private List<subCategoryView> subcategory;
//
//        public categoryView(CategoryDao dao) {
//            this.category_id = dao.getId();
//            this.category_name = dao.getName();
//            this.subcategory = new ArrayList<>();
//        }
//    }
    private Map<String, Object> GenSubCategoryView(SubCategoryDao dao) {
        Map<String, Object> map = new HashMap<>();
        map.put("subcategory_id", dao.getId());
        map.put("subcategory_name", dao.getName());
        return map;
    }

    private Map<String, Object> GenCategoryView(CategoryDao dao, Map<Long, List<Map<String, Object>>> mp) {
        Map<String, Object> map = new HashMap<>();
        map.put("category_id", dao.getId());
        map.put("category_name", dao.getName());
        List<Map<String, Object>> list = new ArrayList<>();
        map.put("subcategory", list);
        mp.put(dao.getId(), list);
        return map;
    }

    @GetMapping("/category/list")
    public ResponseEntity<List<Map<String, Object>>> listCategory() {
        //return new ResponseEntity<>(categoryServices.listSubCategory(), HttpStatus.OK);

        List<Map<String, Object>> list = new ArrayList<>();
        List<CategoryDao> categories = categoryServices.listCategory();
        List<SubCategoryDao> subCategories = categoryServices.listSubCategory();
        Map<Long, List<Map<String, Object>>> map = new HashMap<>();
        for (CategoryDao category : categories) {
            Map<String, Object> view = GenCategoryView(category, map);
            list.add(view);
        }
        for (SubCategoryDao subCategory : subCategories) {
            List<Map<String, Object>> val = map.get(subCategory.getCategory_id());
            val.add(GenSubCategoryView(subCategory));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

//    @GetMapping("/category/info/{id}")
//    public ResponseEntity<Map<String, Object>> categoryInfo(
//            @PathVariable(name = "id") Long id
//    ) {
//        Map<String, Object> map = new HashMap<>();
//        SubCategoryDao category = categoryServices.getCategoryById(id);
//        if (category == null) {
//            return ResponseUtils.notFound();
//        }
//        return new ResponseEntity<>(MyUtils.Object2Map(category), HttpStatus.OK);
//    }
//
//    @PostMapping("/category/add")
//    public ResponseEntity<Map<String, Object>> addCategory(
//            @RequestBody Map<String, String> body,
//            @RequestHeader String token
//    ) {
//
//        Map<String, Object> map = new HashMap<>();
//        if (!JWTUtils.verityAdmin(token)) {
//            return ResponseUtils.unavailable();
//        }
//        String name = body.get("name");
//        SubCategoryDao sameName = categoryServices.getCategoryByName(name);
//        if (sameName != null) {
//            map.put("state", false);
//            map.put("message", "there is a same name category");
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }
//        categoryServices.addCategory(name);
//        map.put("state", true);
//        map.put("message", "add successfully");
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }
//
//    @PostMapping("/category/set")
//    public ResponseEntity<Map<String, Object>> updateCategory(
//            @RequestBody CategoryUpdateBody body,
//            @RequestHeader String token
//    ) {
//        Map<String, Object> map = new HashMap<>();
//        if (!JWTUtils.verityAdmin(token)) {
//            return ResponseUtils.unavailable();
//        }
//        SubCategoryDao sameId = categoryServices.getCategoryById(body.id);
//        if (sameId == null) {
//            return ResponseUtils.notFound();
//        }
//        categoryServices.updateCategory(body.id, body.name);
//        return ResponseUtils.success("update");
//    }
}
