package com.zeroonedance.adminapi;

import com.zeroonedance.adminapi.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class JavaAdminServerApplicationTests {

    @Autowired
    private  RedisUtil redisUtil;

    @Test
    void contextLoads() {
        redisUtil.set("ddd", "ddd", 1000);
    }

}
