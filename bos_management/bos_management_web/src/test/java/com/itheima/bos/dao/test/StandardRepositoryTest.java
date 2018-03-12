package com.itheima.bos.dao.test;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
}
