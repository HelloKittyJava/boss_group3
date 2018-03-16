package com.itheima.bos.dao.test;

import net.sf.json.JSONObject;

/**
 * ClassName:JsonTest <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:01:41 <br/>
 */
public class JsonTest {

    public static void main(String[] args) {

        Person person = new Person(11, "zhangsan");
        String json = JSONObject.fromObject(person).toString();
        System.out.println(json);

    }

}
