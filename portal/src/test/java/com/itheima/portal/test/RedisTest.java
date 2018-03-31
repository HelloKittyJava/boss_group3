package com.itheima.portal.test;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Pageable pageable = new PageRequest(0, 4);

        WebClient.create(
                "http://localhost:8080/bos_management_web/webService/promotionService/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).query("pageable", pageable)
                .get();

    }
}
