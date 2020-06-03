package com.example.redisdemo.controller;


import ch.qos.logback.core.util.TimeUtil;
import com.example.redisdemo.entity.Student;
import com.example.redisdemo.service.cache.UserCache;
import com.example.redisdemo.service.impl.RedisImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class DemoController {
//
//    @Resource
//    private RedisImpl redisImpl;


    @Autowired
    private UserCache userCache;
    @GetMapping("/get")
    public String setValue() {


        return userCache.getStudent();
//        List<Student> list = new ArrayList<>();
//        for(int i=0;i<2;i++){
//            Student student = new Student();
//            student.setAge(18+i);
//            student.setSex(0);
//            student.setName("张"+i);
//          list.add(student);
//        }
//        ObjectMapper objectMapper=new ObjectMapper();
//        try {
//            String value=objectMapper.writeValueAsString(list);
//            redisImpl.setKeyAndValue("student", value, 10L, TimeUnit.SECONDS);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return "info:success" + ",timestamp:" + System.currentTimeMillis();
    }
//
//    @GetMapping("/get")
//    public String getValue() {
//
//        List<Student> result = null;
//        try {
//            result = redisImpl.getKeyAndValue("student");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result.toString();
//    }
}
