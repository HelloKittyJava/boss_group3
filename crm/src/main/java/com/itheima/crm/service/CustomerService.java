package com.itheima.crm.service;
/**
 * ClassName:CustomerService <br/>
 * Function: <br/>
 * Date: 2018年3月18日 上午9:01:19 <br/>
 */

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CustomerService {

    @GET
    @Path("/findAll")
    List<Customer> findAll();
}
