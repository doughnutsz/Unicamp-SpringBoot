package com.perfectmatch.unicampspringboot.db;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubCategoryDao {
    private Long id;
    private Long category_id;
    private String name;
}
