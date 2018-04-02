package com.itheima.bos.dao.base;

import java.util.List;
import java.util.Map;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import org.springframework.data.jpa.repository.Query;

/**
 * ClassName:SubAreaRepository <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:40:01 <br/>
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {

    // 查询未关联定区的分区
    List<SubArea> findByFixedAreaIsNull();

    // 查询关联到指定定区的分区
    // 使用SpringDataJPA的命名规范进行查询的时候，
    // 如果字段是对象，必须是单一对象，不能是集合
    // 传入的参数必须指定id属性
    List<SubArea> findByFixedArea(FixedArea fixedArea);

    @Query("select a.id from SubArea s inner join s.area a GROUP BY a.id")
    List<Long> groupByProvinceName();

    @Query("select count(a.id) from SubArea s inner join s.area a GROUP BY a.id")
    List<Long> groupByCityCount();


//    SELECT * from T_AREA a, (SELECT count(*),C_AREA_ID FROM T_SUB_AREA GROUP BY C_AREA_ID) m WHERE a.C_ID = m.C_AREA_ID

//    SELECT count(*),C_AREA_ID FROM T_SUB_AREA GROUP BY C_AREA_ID
    @Query("select count(area.id),area.id from SubArea GROUP BY area.id")
    List<Object[]> xjbcByAreaId();
}
