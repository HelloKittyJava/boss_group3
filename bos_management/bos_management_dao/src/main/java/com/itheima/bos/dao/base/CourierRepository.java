package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**
 * ClassName:CourierRepository <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午11:42:27 <br/>
 */
// JpaSpecificationExecutor接口不能单独使用
// JpaSpecificationExecutor接口一般都是和JpaRepository接口一起使用
public interface CourierRepository extends JpaRepository<Courier, Long>,
        JpaSpecificationExecutor<Courier> {

    // 根据ID更改删除的标志位
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(Long id);

    List<Courier> findByDeltagIsNull();
}
