package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.Standard;

/**
 * ClassName:StandardRepository <br/>
 * Function: <br/>
 * Date: 2018年3月12日 上午11:22:16 <br/>
 */
// 泛型1 : 封装数据的对象的类型
// 泛型2 : 对象的主键的类型
public interface StandardRepository extends JpaRepository<Standard, Long> {

    // SpringDataJPA提供了一套命名规范,遵循这一套规范定义查询类方法
    // 必须以findBy开头,后面跟属性的名字,首字母必须大写
    // 如果有多个条件,使用对应的SQL关键字
    List<Standard> findByName(String name);

    List<Standard> findByNameLike(String name);

    List<Standard> findByNameAndMaxWeight(String name, Integer maxWeight);

    // 查询语句:JPQL === HQL
    @Query("from Standard where name = ? and maxWeight = ?")
    List<Standard> findByNameAndMaxWeight321312(String name, Integer maxWeight);

    // 在?后面追加数字的方式,改变匹配参数的顺序
    @Query("from Standard where name = ?2 and maxWeight = ?1")
    List<Standard> findByNameAndMaxWeight321312(Integer maxWeight, String name);

    // 原生SQL
    @Query(value = "select * from T_STANDARD where C_NAME = ? and C_MAX_WEIGHT = ?",
            nativeQuery = true)
    List<Standard> findByNameAndMaxWeightfdsa321312(String name,
            Integer maxWeight);

    @Modifying
    @Transactional
    @Query("update Standard set maxWeight = ? where name = ?")
    void updateWeightByName(Integer maxWeight, String name);

    @Modifying
    @Transactional
    @Query("delete from Standard where name = ?")
    void deleteByName(String name);

}
