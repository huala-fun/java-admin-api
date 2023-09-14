package com.zeroonedance.adminapi;

import com.zeroonedance.adminapi.utils.JWTUtil;
import com.zeroonedance.adminapi.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;

@SpringBootTest
class JavaAdminServerApplicationTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void contextLoads() {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("ddd", "11");
        objectObjectHashMap.put("sub", "11");

        System.out.println(jwtUtil.generateToken("huawei", objectObjectHashMap));
    }

}
