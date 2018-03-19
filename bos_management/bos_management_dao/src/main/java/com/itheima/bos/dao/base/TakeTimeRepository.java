package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.TakeTime;

/**
 * ClassName:TakeTimeRepository <br/>
 * Function: <br/>
 * Date: 2018年3月19日 上午8:51:22 <br/>
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Long> {

}
