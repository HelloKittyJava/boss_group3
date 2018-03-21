package com.itheima.portal.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ClassName:RedisTest <br/>
 * Function: <br/>
 * Date: 2018年3月21日 上午8:41:42 <br/>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void test() {

//        redisTemplate.opsForValue().set("name", "zhangsan");
        // 设置数据的有效期，过期以后会被自动删除
        // 参数3：时长
        // 参数4：时间单位
        // 1000 * 60 * 60
//        redisTemplate.opsForValue().set("tel", "110", 10, TimeUnit.SECONDS);
        
        redisTemplate.delete("name");
        
        
        
        
        
        
        
        
        
        
        
        
    }
}
