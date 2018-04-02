package com.itheima.bos.service.take_delivery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.WayBill;

/**  
 * ClassName:WayBillService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 上午11:13:53 <br/>       
 */
public interface WayBillService {

    void save(List<WayBill> list);

    Page<WayBill> findAll(Pageable pageable);

    

}
  
