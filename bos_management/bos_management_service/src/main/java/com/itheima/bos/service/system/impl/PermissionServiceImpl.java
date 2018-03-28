package com.itheima.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.PermissonRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;

/**
 * ClassName:PermissionServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:44:20 <br/>
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissonRepository permissonRepository;

    @Override
    public void save(Permission permission) {

        permissonRepository.save(permission);

    }

    @Override
    public Page<Permission> findAll(Pageable pageable) {

        return permissonRepository.findAll(pageable);
    }

}
