package com.itheima.bos.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.User;

/**
 * ClassName:UserService <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午12:05:18 <br/>
 */
public interface UserService {

    void save(User user, Long[] roleIds);

    Page<User> findAll(Pageable pageable);

}
