package com.perfectmatch.unicampspringboot.controllers;

import com.perfectmatch.unicampspringboot.db.CategoryDao;
import com.perfectmatch.unicampspringboot.services.CategoryServices;
import com.perfectmatch.unicampspringboot.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller // This means that this class is a Controller
@CrossOrigin
@RequestMapping(path = "/api")
public class CategoryController {
    @Autowired
    CategoryServices categoryServices;

    static class CategoryUpdateBody {
        private final Long id;
        private final String name;

        public CategoryUpdateBody(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @GetMapping("/category/list")
    public ResponseEntity<List<CategoryDao>> listCategory() {
        return new ResponseEntity<>(categoryServices.listCategory(), HttpStatus.OK);
    }

    @GetMapping("/category/info/{id}")
    public ResponseEntity<Map<String, Object>> categoryInfo(
            @PathVariable(name = "id") Long id
    ) {
        Map<String, Object> map = new HashMap<>();
        CategoryDao category = categoryServices.getCategoryById(id);
        if (category == null) {
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        map.put("id", category.getId());
        map.put("name", category.getName());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/category/add")
    public ResponseEntity<Map<String, Object>> addCategory(
            @RequestBody Map<String, String> body,
            @RequestHeader String token
    ) {

        Map<String, Object> map = new HashMap<>();
        if (!JWTUtils.verityAdmin(token)) {
            return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
        }
        String name = body.get("name");
        CategoryDao sameName = categoryServices.getCategoryByName(name);
        if (sameName != null) {
            map.put("state", false);
            map.put("message", "there is a same name category");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        categoryServices.addCategory(name);
        map.put("state", true);
        map.put("message", "add successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/category/set")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @RequestBody CategoryUpdateBody body,
            @RequestHeader String token
    ) {
        Map<String, Object> map = new HashMap<>();
        if (!JWTUtils.verityAdmin(token)) {
            return new ResponseEntity<>(map, HttpStatus.SERVICE_UNAVAILABLE);
        }
        CategoryDao sameId = categoryServices.getCategoryById(body.id);
        if (sameId == null) {
            map.put("state", false);
            map.put("message", "there is no same id category");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        categoryServices.updateCategory(body.id, body.name);
        map.put("state", true);
        map.put("message", "update successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
