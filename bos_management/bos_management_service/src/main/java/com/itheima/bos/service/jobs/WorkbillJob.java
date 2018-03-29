package com.itheima.bos.service.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.take_delivery.WorkbillRepository;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.utils.MailUtils;

/**
 * ClassName:WorkbillJob <br/>
 * Function: <br/>
 * Date: 2018年3月29日 上午11:41:50 <br/>
 */
@Component
public class WorkbillJob {
    @Autowired
    private WorkbillRepository workbillRepository;

    public void sendMail() {
        List<WorkBill> list = workbillRepository.findAll();

        String emailBody = "编号\t快递员\t取件状态\t时间<br/>";

        for (WorkBill workBill : list) {
            emailBody += workBill.getId() + "\t"
                    + workBill.getCourier().getName() + "\t"
                    + workBill.getPickstate() + "\t"
                    + workBill.getBuildtime().toLocaleString() + "<br/>";
        }
        MailUtils.sendMail("tom@store.com", "工单信息统计", emailBody);
        
        System.out.println("邮件已经发送");
    }
}
