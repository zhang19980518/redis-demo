package com.example.redisdemo.service.cache;

import com.example.redisdemo.entity.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Component
public class UserCache {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Cacheable(value = "user",keyGenerator = "simpleKeyGenerator")
    public String getStudent() {

        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.setAge(18 + i);
            student.setSex(0);
            student.setName("张" + i);
            list.add(student);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String value = objectMapper.writeValueAsString(list);
            stringRedisTemplate.opsForValue().set("user",value);
            return value;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}


