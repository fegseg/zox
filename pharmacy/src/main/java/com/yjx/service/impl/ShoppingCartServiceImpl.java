package com.yjx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjx.entity.ShoppingCart;
import com.yjx.mapper.ShoppingCartMapper;
import com.yjx.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yang123
 * @since 2021-11-23
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public Integer getCartInfo(String drugName, String username) {
        return shoppingCartMapper.getCartInfo(drugName,username);
    }

    @Override
    public Integer getDrugAmount(String drugName, String username) {
        return shoppingCartMapper.getDrugAmount(drugName, username);
    }

}
