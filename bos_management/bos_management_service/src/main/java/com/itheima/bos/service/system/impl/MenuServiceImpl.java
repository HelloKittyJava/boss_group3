package com.itheima.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.service.system.MenuService;

/**
 * ClassName:MenuServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午9:20:45 <br/>
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findLevelOne() {

        return menuRepository.findByParentMenuIsNull();
    }

    @Override
    public void save(Menu menu) {
        // 导致异常的原因是parentMenu字段是一个瞬时态的对象
        // 判断用户是否是要添加一个一级菜单
        if (menu.getParentMenu() != null
                && menu.getParentMenu().getId() == null) {
            menu.setParentMenu(null);
        }

        menuRepository.save(menu);
    }
}
