package com.itheima.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.service.take_delivery.OrderService;

/**
 * ClassName:OrderServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月23日 上午9:59:08 <br/>
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void saveOrder(Order order) {
        // 把瞬时态的Area转换为持久态的Area
        Area sendArea = order.getSendArea();
        if (sendArea != null) {
            // 持久态对象
            Area sendAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    sendArea.getProvince(), sendArea.getCity(),
                    sendArea.getDistrict());
            order.setSendArea(sendAreaDB);
        }

        Area recArea = order.getRecArea();
        if (recArea != null) {
            // 持久态对象
            Area recAreaDB = areaRepository.findByProvinceAndCityAndDistrict(
                    recArea.getProvince(), recArea.getCity(),
                    recArea.getDistrict());
            order.setRecArea(recAreaDB);
        }
        orderRepository.save(order);
    }

}
