package com.yjx.service.impl;

import com.yjx.entity.MenuSecond;
import com.yjx.mapper.MenuSecondMapper;
import com.yjx.service.MenuSecondService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yang123
 * @since 2021-11-22
 */
@Service
public class MenuSecondServiceImpl extends ServiceImpl<MenuSecondMapper, MenuSecond> implements MenuSecondService {

    @Autowired
    MenuSecondMapper menuSecondMapper;

    @Override
    public List<MenuSecond> getSecondMenuList(String fatherId, Integer auth) {
        return menuSecondMapper.getSecondMenuList(fatherId, auth);
    }
}
