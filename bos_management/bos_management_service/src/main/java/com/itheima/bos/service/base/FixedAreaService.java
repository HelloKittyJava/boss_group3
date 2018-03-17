package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;

/**
 * ClassName:StandardService <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午9:06:59 <br/>
 */
public interface FixedAreaService {

    void save(FixedArea standard);

    Page<FixedArea> findAll(Pageable pageable);

}
