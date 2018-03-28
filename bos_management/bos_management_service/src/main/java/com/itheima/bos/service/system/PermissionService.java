package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;

/**
 * ClassName:PermissionService <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:43:44 <br/>
 */
public interface PermissionService {

    void save(Permission permission);

    Page<Permission> findAll(Pageable pageable);
}
