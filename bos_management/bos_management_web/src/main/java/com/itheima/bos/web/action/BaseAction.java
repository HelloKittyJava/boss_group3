package com.itheima.bos.web.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.itheima.bos.domain.base.Area;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CommonAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午12:02:47 <br/>
 */
// public class AreaAction extends BaseAction<Area>
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;
// 反射,内省,动态代理
    @Override
    public T getModel() {
        // 以public class AreaAction extends BaseAction<Area>代码为例
        // 调用下面的代码以后,得到的是AreaAction的字节码
        Class<? extends BaseAction> childClazz = this.getClass();
        // 得到的是BaseAction
        // childClazz.getSuperclass();
        // 得到的是BaseAction<Area>
        Type genericSuperclass = childClazz.getGenericSuperclass();
        // 类型强转
        ParameterizedType parameterizedType =
                (ParameterizedType) genericSuperclass;
        // 获取泛型的数组
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<T> clazz = (Class<T>) actualTypeArguments[0];

        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();  
            
        }
        return model;
    }

}
