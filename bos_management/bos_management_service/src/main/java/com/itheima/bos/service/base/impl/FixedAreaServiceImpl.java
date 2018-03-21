package com.itheima.bos.service.base.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;

/**
 * ClassName:StandardServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午9:07:13 <br/>
 */
@Transactional
@Service // spring的注解,业务层代码
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private TakeTimeRepository takeTimeRepository;

    @Autowired
    private SubAreaRepository subAreaRepository;

    // 保存
    @Override
    public void save(FixedArea standard) {
        fixedAreaRepository.save(standard);
    }

    // 分页查询
    @Override
    public Page<FixedArea> findAll(Pageable pageable) {

        return fixedAreaRepository.findAll(pageable);
    }

    @Override
    public void associationCourierToFixedArea(Long fixedAreaId, Long courierId,
            Long takeTimeId) {
        // 代码执行成功以后,快递员表发生update操作,快递员和定区中间表会发生insert操作

        // 持久态对象
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);

        // 建立快递员和时间的关联
        courier.setTakeTime(takeTime);
        // 建立快递员和定区的关联
        // 下面的写法是错的,因为在Courier实体类中fixedAreas字段的上方添加了mappedBy属性
        // 就代表快递员放弃了对关系的维护
        // courier.getFixedAreas().add(fixedArea);
        fixedArea.getCouriers().add(courier);

    }

    // 关联分区到指定的定区
    @Override
    public void assignSubAreas2FixedArea(Long fixedAreaId, Long[] subAreaIds) {

        // 关系是由分区在维护
        // 先解绑，把当前定区绑定的所有分区全部解绑
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Set<SubArea> subareas = fixedArea.getSubareas();
        for (SubArea subArea : subareas) {
            subArea.setFixedArea(null);
        }

        // 再绑定
        for (Long subAreaId : subAreaIds) {
            SubArea subArea = subAreaRepository.findOne(subAreaId);
            subArea.setFixedArea(fixedArea);
        }
    }

}
