package com.yjx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yjx.common.lang.Result;
import com.yjx.common.lang.ResultInfo;
import com.yjx.entity.Orders;
import com.yjx.mapper.OrdersMapper;
import com.yjx.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yang123
 * @since 2021-12-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public Result orderComplaint(String orderId, String reason) {

        try {
            UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("complaint", 1).set("reason", reason).eq("order_id", orderId);
            ordersMapper.update(new Orders(),updateWrapper);
            return Result.success().codeAndMessage("200", "投诉成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("操作失败");
        }
    }

    @Override
    public Result confirmReceipt(String orderId) {

        try {
            UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status", 3).eq("order_id", orderId);
            ordersMapper.update(new Orders(),updateWrapper);
            return Result.success().codeAndMessage("200", "确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().message("操作失败");
        }
    }

    @Override
    public Result getNewComplaintNumsOrderOnMonth() {

        Integer nums = ordersMapper.getNewComplaintNumsOrderOnMonth();
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("responseBody", nums);
    }
}
