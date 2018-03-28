package com.itheima.bos.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctc.wstx.util.StringUtil;
import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.dao.system.PermissonRepository;
import com.itheima.bos.dao.system.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
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
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PermissonRepository permissonRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

    @Override
    public void save(Role role, String menuIds, Long[] permissionIds) {
        roleRepository.save(role);

        if (StringUtils.isNotEmpty(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                Menu menu = new Menu();
                menu.setId(Long.parseLong(menuId));
                role.getMenus().add(menu);
            }
        }

        if (permissionIds != null && permissionIds.length > 0) {
            for (Long permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(permissionId);
                role.getPermissions().add(permission);

            }
        }

    }

    private void method1(Role role, String menuIds, Long[] permissionIds) {
        if (StringUtils.isNotEmpty(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                Menu menu = menuRepository.findOne(Long.parseLong(menuId));
                role.getMenus().add(menu);
            }
        }

        if (permissionIds != null && permissionIds.length > 0) {
            for (Long permissionId : permissionIds) {
                Permission permission =
                        permissonRepository.findOne(permissionId);
                role.getPermissions().add(permission);

            }
        }
    }

}
