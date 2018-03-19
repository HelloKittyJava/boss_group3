package com.itheima.bos.service.base;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.domain.base.TakeTime;

/**
 * ClassName:TakeTimeService <br/>
 * Function: <br/>
 * Date: 2018年3月19日 上午8:51:49 <br/>
 */

public interface TakeTimeService{

    List<TakeTime> findAll();

}
