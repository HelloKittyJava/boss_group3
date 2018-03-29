package com.itheima.bos.service.system;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

/**
 * ClassName:MenuService <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午9:20:32 <br/>
 */
public interface MenuService {

    List<Menu> findLevelOne();

    void save(Menu menu);

    Page<Menu> findAll(Pageable pageable);

    List<Menu> findbyUser(User user);

}
