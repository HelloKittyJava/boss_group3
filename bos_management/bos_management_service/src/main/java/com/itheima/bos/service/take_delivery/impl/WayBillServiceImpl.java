package com.itheima.bos.service.take_delivery.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.WaybillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WayBillService;

/**
 * ClassName:WayBillServiceImpl <br/>
 * Function: 
 * Date: 2018年3月25日 上午11:14:05 <br/>
 */
@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

    @Autowired
    private WaybillRepository waybillRepository;

	@Override
	public Page<WayBill> findAll(Pageable pageable) {
		return waybillRepository.findAll(pageable);
	}

	@Override
	public void save(List<WayBill> list) {
		waybillRepository.save(list);
		
	}

    @Override
    public List<WayBill> findByWayBillNum(WayBill wayBill) {
       
        if (wayBill.getWayBillNum() != null) {
            return waybillRepository.findByWayBillNum(wayBill.getWayBillNum());
        }
        return null;
    }

    @Override
    public void add(WayBill model) {
         
        if (model.getOrder() != null && model.getOrder().getId() == null) {
            model.setOrder(null);
        }
        String wayBillNum = model.getWayBillNum();
        List<WayBill> list = waybillRepository.findByWayBillNum(wayBillNum);
        if (!list.isEmpty()) {
            WayBill wayBill = list.get(0);
            model.setId(wayBill.getId());
        }
        waybillRepository.save(model);
        
    }
}
