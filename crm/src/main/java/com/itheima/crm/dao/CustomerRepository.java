package com.itheima.crm.dao;
/**
 * ClassName:CustomerRepository <br/>
 * Function: <br/>
 * Date: 2018年3月18日 上午9:00:51 <br/>
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 查询未关联定区的客户
    List<Customer> findByFixedAreaIdIsNull();

    // 查询已关联到指定定区的客户
    List<Customer> findByFixedAreaId(String fixedAreaId);

    // 把关联到指定定区的客户进行解绑操作
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    void unbindCustomerByFixedArea(String fixedAreaId);

    // 把客户绑定到指定的定区
    @Query("update Customer set fixedAreaId = ?2 where id = ?1")
    @Modifying
    void bindCustomer2FixedArea(Long customerId, String fixedAreaId);
}
