package com.itheima.crm.dao;
/**
 * ClassName:CustomerRepository <br/>
 * Function: <br/>
 * Date: 2018年3月18日 上午9:00:51 <br/>
 */

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
