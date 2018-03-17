package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.FixedArea;

/**
 * ClassName:StandardRepository <br/>
 * Function: <br/>
 * Date: 2018年3月12日 上午11:22:16 <br/>
 */
// 泛型1 : 封装数据的对象的类型
// 泛型2 : 对象的主键的类型
public interface FixedAreaRepository extends JpaRepository<FixedArea, Long> {

}
