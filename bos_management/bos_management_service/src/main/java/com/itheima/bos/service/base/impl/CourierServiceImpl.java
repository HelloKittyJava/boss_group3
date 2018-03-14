package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;

/**
 * ClassName:CourierServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午11:41:52 <br/>
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {

        courierRepository.save(courier);
    }
    
    @Override
    public Page<Courier> findAll(Pageable pageable) {
          
        return courierRepository.findAll(pageable);
    }
}
