package com.itheima.bos.dao.test;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.SubArea;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

/**
 * ClassName:StandardRepositoryTest <br/>
 * Function: <br/>
 * Date: 2018年3月12日 上午11:25:29 <br/>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private SubAreaRepository subAreaRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Test
    public void test1() {
        List<Standard> list = standardRepository.findAll();
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }

    @Test
    public void test2() {
        Standard standard = new Standard();
        standard.setName("张三三");
        standard.setMaxWeight(100);

        standardRepository.save(standard);
    }

    @Test
    public void test3() {
        Standard standard = new Standard();
        standard.setId(2L);
        standard.setName("张三三");
        standard.setMaxWeight(9999);

        // save兼具保存和修改的功能,
        // 修改的话,必须传入id
        standardRepository.save(standard);
    }

    @Test
    public void test4() {
        Standard standard = standardRepository.findOne(2L);
        System.out.println(standard);
    }

    @Test
    public void test5() {
        standardRepository.delete(2L);
    }

    @Test
    public void test6() {
        // 根据名字进行查找
        List<Standard> list = standardRepository.findByName("张三");
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    @Test
    public void test7() {
        // 根据名字进行查找
        List<Standard> list = standardRepository.findByNameLike("%张三%");
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    @Test
    public void test8() {
        // 根据名字进行查找
        List<Standard> list =
                standardRepository.findByNameAndMaxWeight("张三", 100);
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    @Test
    public void test9() {
        // 根据名字进行查找
        List<Standard> list =
                standardRepository.findByNameAndMaxWeight321312("张三", 100);
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    @Test
    public void test10() {
        // 根据名字进行查找
        List<Standard> list =
                standardRepository.findByNameAndMaxWeight321312(100, "张三");
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    @Test
    public void test11() {
        // 根据名字进行查找
        List<Standard> list =
                standardRepository.findByNameAndMaxWeightfdsa321312("张三", 100);
        for (Standard standard : list) {
            System.out.println(standard);
        }

    }

    // 在测试用例中使用事务注解,方法执行完成以后,事务回滚了
    @Test
    public void test12() {
        standardRepository.updateWeightByName(200, "张三");

    }
    
    @Test
    public void test13() {
        standardRepository.deleteByName("张三");

    }

    @Test
    public void test14(){
//        List<Object[]> list = subAreaRepository.xjbcByAreaId();
        List<Object[]> list = areaRepository.xjbcCharts();
        ArrayList<Map<String,Object>> arrayList = new ArrayList();
        for (Object[] objects : list) {
            HashMap<String, Object> map = new HashMap<>();
            for (Object o : objects) {
                if(o instanceof Long){
                    map.put("data",new Long[]{(Long) o});
                }else{
                    map.put("name",(String)o);
                }
            }
            arrayList.add(map);
        }
        String json = new Gson().toJson(arrayList);
        System.out.println(json);
    }
}
