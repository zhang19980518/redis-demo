package com.example.redisdemo.service.impl;


import com.example.redisdemo.entity.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * String key value存值
     *
     * @param key
     * @param value
     */
    public void setKeyAndValue(String key, String value, Long time, TimeUnit timeUnit) {

        //noinspection rawtypes
        ValueOperations operations = stringRedisTemplate.opsForValue();

        operations.set(key, value, time, timeUnit);
    }

    public List<Student> getKeyAndValue(String key) throws Exception {
        Object value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new Exception("data is null");
        }
            ObjectMapper objectMapper = new ObjectMapper();
            List<Student> list=objectMapper.readValue((String) value, new TypeReference<List<Student>>(){});
            System.out.println(list.toString());
            return objectMapper.readValue((String) value, new TypeReference<List<Student>>() {
            });
        }


}
