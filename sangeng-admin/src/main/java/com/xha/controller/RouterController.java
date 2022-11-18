package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RouterController {


    @Resource
    private MenuService menuService;

    /**
     * 根据条件查询menu信息
     * @return {@link ResponseResult}
     */
    @GetMapping("/getRouters")
    public ResponseResult selectAllRouterMenu(){
//        查询menu，查询结果是tree形式
       return menuService.selectRouterMenu();
    }
}
