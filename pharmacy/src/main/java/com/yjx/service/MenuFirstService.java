package com.yjx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjx.entity.MenuFirst;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface MenuFirstService extends IService<MenuFirst> {

    /**
     * 查询可用的一级菜单
     * @return List
     */
    List<MenuFirst> getFirstMenuList(Integer auth);

}
