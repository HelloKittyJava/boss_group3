package com.itheima.bos.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Permission;

/**
 * ClassName:PermissonRepository <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:43:15 <br/>
 */
public interface PermissonRepository extends JpaRepository<Permission, Long> {
    // SELECT<br/>
    // * <br/>
    // FROM<br/>
    // T_PERMISSION p<br/>
    // INNER JOIN T_ROLE_PERMISSION rp ON rp.C_PERMISSION_ID = p.C_ID<br/>
    // INNER JOIN T_ROLE r ON r.C_ID = rp.C_ROLE_ID<br/>
    // INNER JOIN T_USER_ROLE ur ON ur.C_ROLE_ID = r.C_ID<br/>
    // INNER JOIN T_USER u ON u.C_ID = ur.C_USER_ID <br/>
    // WHERE<br/>
    // u.C_ID = 227<br/>

    @Query("select p from Permission p inner join p.roles r inner join r.users u where u.id = ?")
    List<Permission> findbyUid(Long uid);

}
