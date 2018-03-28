package com.itheima.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;

/**
 * ClassName:RoleServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:54:27 <br/>
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

}
