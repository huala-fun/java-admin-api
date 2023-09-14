package com.zeroonedance.adminapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * redis配置
 *
 */
@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 创建一个新的RedisTemplate实例
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 为template设置连接工厂
        template.setConnectionFactory(connectionFactory);
        // 为template设置默认的序列化器，用于将Java对象序列化为JSON格式
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        // 为template设置键的序列化器，用于将字符串序列化为Redis中的键
        template.setKeySerializer(new StringRedisSerializer());
        // 返回创建好的template实例
        return template;
    }




}
