package com.itheima.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WaybillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WayBillService;

/**
 * ClassName:WayBillServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月25日 上午11:14:05 <br/>
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

    @Autowired
    private WaybillRepository waybillRepository;

    @Override
    public void save(WayBill model) {
        waybillRepository.save(model);

    }
}
