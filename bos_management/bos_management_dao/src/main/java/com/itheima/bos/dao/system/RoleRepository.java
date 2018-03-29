package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Role;

/**
 * ClassName:RoleRepository <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:53:29 <br/>
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 下面的sql语句其实省略了select * ,等于查询了所有的表中的所有字段
    // 查完以后会把查询结果封装给对象,下面的查询结束以后封装了Role和User两个对象
    // 之后会创建一个数组,把这两个对象装在这个数组中,因此会发生类型转换异常
    // 要解决这个错误,我们需要具体指定查询结果,就是增加select r
    @Query("select r from Role r inner join r.users u where u.id = ?")
    List<Role> findbyUid(Long uid);

}
