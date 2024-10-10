package com.hana.hana_spring.web;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.utils.Result;

@RestController
@RequestMapping("favorites")
@CrossOrigin("*")
@LoginValidate
public class FavoriteCtr {
    @GetMapping
    public Result get_favorites() {
        return Result.success();
    }

}
