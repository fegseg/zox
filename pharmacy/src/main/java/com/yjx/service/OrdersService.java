package com.yjx.service;

import com.yjx.common.lang.Result;
import com.yjx.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户进行订单投诉
     *
     * @param orderId 订单编号
     * @param reason  投诉理由
     * @return Result
     */
    Result orderComplaint(String orderId, String reason);

    /**
     * 前台用户确认收货
     *
     * @param orderId 订单号
     * @return Result
     */
    Result confirmReceipt(String orderId);

    /**
     * 最近一个月内的投诉订单数
     *
     * @return 最近一个月内的投诉订单数
     */
    Result getNewComplaintNumsOrderOnMonth();

}
