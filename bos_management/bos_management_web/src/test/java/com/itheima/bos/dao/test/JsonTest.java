package com.itheima.bos.dao.test;

import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import com.itheima.crm.domain.Customer;

import net.sf.json.JSONObject;

/**
 * ClassName:JsonTest <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:01:41 <br/>
 */
public class JsonTest {

    public static void main(String[] args) {

        Collection<? extends Customer> collection = WebClient.create(
                "http://localhost:8180/crm/webService/customerService/findAll")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        for (Customer customer : collection) {
            System.out.println(customer);
        }
    }

}
