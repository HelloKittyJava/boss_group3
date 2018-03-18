package com.itheima.crm.service;
/**
 * ClassName:CustomerService <br/>
 * Function: <br/>
 * Date: 2018年3月18日 上午9:01:19 <br/>
 */

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CustomerService {

    @GET
    @Path("/findAll")
    List<Customer> findAll();

    // 查询未关联定区的客户
    @GET
    @Path("/findCustomersUnAssociated")
    List<Customer> findCustomersUnAssociated();

    // 查询已关联到指定定区的客户
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    List<Customer> findCustomersAssociated2FixedArea(
            @QueryParam("fixedAreaId") String fixedAreaId);

    // 定区ID,要关联的数据
    // 根据定区ID,把关联到这个定区的所有客户全部解绑
    // 要关联的数据和定区Id进行绑定
    @PUT
    @Path("/assignCustomers2FixedArea")
    void assignCustomers2FixedArea(
            @QueryParam("customerIds") Long[] customerIds,
            @QueryParam("fixedAreaId") String fixedAreaId);
}
