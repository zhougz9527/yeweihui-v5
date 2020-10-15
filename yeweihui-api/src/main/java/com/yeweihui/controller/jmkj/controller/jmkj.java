package com.yeweihui.controller.jmkj.controller;

import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 基沐科技新加功能
 *
 * */

@RestController
@RequestMapping("jmkj")
public class jmkj {

    @Autowired
    JmkjServiceImpl jmkjServiceImpl;
}
