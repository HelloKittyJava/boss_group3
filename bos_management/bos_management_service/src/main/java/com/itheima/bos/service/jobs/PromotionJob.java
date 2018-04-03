package com.itheima.bos.service.jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.take_delivery.PromotionRepository;
import com.itheima.bos.domain.take_delivery.Promotion;

/**  
 * ClassName:PromotionJob <br/>  
 * Function:  <br/>  
 * Date:     2018年4月2日 上午10:17:41 <br/>       
 */
@Component
@Transactional
public class PromotionJob {

    @Autowired
    private PromotionRepository promotionRepository;
    
    
    public void checkDate(){
       
        List<Promotion> list = promotionRepository.findAll();
        Date currentDate = new Date();

        for (Promotion promotion : list) {
            Date startDate = promotion.getStartDate();
            Date endDate = promotion.getEndDate();
           
            if (!(currentDate.after(startDate) && currentDate.before(endDate))) {
                //System.out.println("=====");
                promotion.setStatus("2");
            }
           
        }

    }
    
    
}
  
