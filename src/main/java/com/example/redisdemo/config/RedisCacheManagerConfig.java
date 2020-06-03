package com.example.redisdemo.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Joert
 */
@Configuration
@EnableCaching
public class RedisCacheManagerConfig {

    /**
     * 临时缓存，过期时间：10分钟
     */
    public static final String KEY_CACHE_TEMPORARY = "temporary";

    /**
     * 过期时间：1小时
     */
    public static final String KEY_CACHE_HOURLY = "hourly";

    /**
     * 过期时间：6小时
     */
    public static final String KEY_CACHE_6HOUR = "6hour";

    /**
     * 过期时间：1天
     */
    public static final String KEY_CACHE_DAILY = "daily";

    /**
     * 永不过期的缓存
     */
    public static final String KEY_CACHE_PERMANENT = "permanent";


    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public KeyGenerator simpleKeyGenerator() {
        return (o, method, objects) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(o.getClass().getSimpleName());
            stringBuilder.append(".");
            stringBuilder.append(method.getName());
            stringBuilder.append("[");
            for (Object obj : objects) {
                stringBuilder.append(obj.toString());
            }
            stringBuilder.append("]");
            System.out.println(stringBuilder.toString()+"--------------");
            return stringBuilder.toString();
        };
    }
//
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return new RedisCacheManager(
//                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
//                // 默认策略，未配置的 key 会使用这个
//                this.getRedisCacheConfigurationWithTtl(600),
//                // 指定 key 策略
//                this.getRedisCacheConfigurationMap()
//        );
//    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(2);
        configurationMap.put(KEY_CACHE_TEMPORARY, this.getRedisCacheConfigurationWithTtl(10 * 60));
        configurationMap.put(KEY_CACHE_HOURLY, this.getRedisCacheConfigurationWithTtl(60 * 60));
        configurationMap.put(KEY_CACHE_6HOUR, this.getRedisCacheConfigurationWithTtl(6 * 60 * 60));
        configurationMap.put(KEY_CACHE_DAILY, this.getRedisCacheConfigurationWithTtl(24 * 60 * 60));
        configurationMap.put(KEY_CACHE_PERMANENT, this.getRedisCacheConfigurationWithTtl(-1));
        return configurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }


}
