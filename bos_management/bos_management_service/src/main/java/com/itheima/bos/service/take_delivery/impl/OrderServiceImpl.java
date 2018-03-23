package com.itheima.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.take_delivery.OrderRepository;
import com.itheima.bos.dao.take_delivery.WorkbillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService;

/**
 * ClassName:OrderServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月23日 上午9:59:08 <br/>
 * 
 * 根据发件地址完全匹配(发件地址数据来自CRM系统中customer表,而且这个客户一定要和某一个定区关联)
 * 根据发件地址模糊匹配(发件地址数据来自后台系统中subArea表,而且这个分区一定要和某一个定区关联,这个分区所属的区域数据一定要对得上号)
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private WorkbillRepository workbillRepository;

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
        // 保存订单
        order.setOrderNum(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setOrderTime(new Date());
        orderRepository.save(order);

        // 自动分单

        String sendAddress = order.getSendAddress();

        if (StringUtils.isNotEmpty(sendAddress)) {
            // ---根据发件地址完全匹配
            // 让crm系统根据发件地址查询定区ID
            // 做下面的测试之前,必须在定区中关联一个客户,下单的页面填写的地址,必须和这个客户的地址一致
            String fixedAreaID = WebClient.create(
                    "http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAdddress")
                    .type(MediaType.APPLICATION_JSON)
                    .query("address", sendAddress)
                    .accept(MediaType.APPLICATION_JSON).get(String.class);

            if (StringUtils.isNotEmpty(fixedAreaID)) {
                // 根据定区ID查询定区
                FixedArea fixedArea = fixedAreaRepository
                        .findOne(Long.parseLong(fixedAreaID));
                if (fixedArea != null) {
                    // 查询快递员
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (!couriers.isEmpty()) {
                        // 根据快递员的上班时间/收派能力/忙闲程度
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        // 指派快递员
                        order.setCourier(courier);
                        // 生成工单
                        WorkBill workBill = new WorkBill();
                        workBill.setAttachbilltimes(0);
                        workBill.setBuildtime(new Date());
                        workBill.setCourier(courier);
                        workBill.setOrder(order);
                        workBill.setPickstate("新单");
                        workBill.setRemark(order.getRemark());
                        workBill.setSmsNumber("111");
                        workBill.setType("新");

                        workbillRepository.save(workBill);
                        // 发送短信,推送一个通知
                        // 中断代码的执行
                        order.setOrderType("自动分单");
                        return;
                    }
                }
            } else {
                // 持久态
                // 定区关联分区,在页面上填写的发件地址,必须是对应的分区的关键字或者辅助关键字
                Area sendArea2 = order.getSendArea();
                if (sendArea2 != null) {
                    Set<SubArea> subareas = sendArea2.getSubareas();
                    for (SubArea subArea : subareas) {
                        String keyWords = subArea.getKeyWords();
                        String assistKeyWords = subArea.getAssistKeyWords();
                        if (sendAddress.contains(keyWords)
                                || sendAddress.contains(assistKeyWords)) {
                            FixedArea fixedArea2 = subArea.getFixedArea();

                            if (fixedArea2 != null) {
                                // 查询快递员
                                Set<Courier> couriers =
                                        fixedArea2.getCouriers();
                                if (!couriers.isEmpty()) {
                                    Iterator<Courier> iterator =
                                            couriers.iterator();
                                    Courier courier = iterator.next();
                                    // 指派快递员
                                    order.setCourier(courier);
                                    // 生成工单
                                    WorkBill workBill = new WorkBill();
                                    workBill.setAttachbilltimes(0);
                                    workBill.setBuildtime(new Date());
                                    workBill.setCourier(courier);
                                    workBill.setOrder(order);
                                    workBill.setPickstate("新单");
                                    workBill.setRemark(order.getRemark());
                                    workBill.setSmsNumber("111");
                                    workBill.setType("新");

                                    workbillRepository.save(workBill);
                                    // 发送短信,推送一个通知
                                    // 中断代码的执行
                                    order.setOrderType("自动分单");
                                    return;
                                }
                            }

                        }
                    }
                }
            }

        }

        // ---根据发件地址模糊匹配

        order.setOrderType("人工分单");
    }

}
