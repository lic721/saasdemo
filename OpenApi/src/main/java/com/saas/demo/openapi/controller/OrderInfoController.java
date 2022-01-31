package com.saas.demo.openapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saas.demo.common.base.response.Result;
import com.saas.demo.common.base.response.ResultCode;
import com.saas.demo.common.entity.OrderInfo;
import com.saas.demo.common.service.OrderInfoService;
import com.saas.demo.common.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderInfo/{tenantCode}")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation(value = "创建订单")
    @PostMapping(value = "/createOrder")
    public Result<OrderInfo> createOrder(@PathVariable String tenantCode, @RequestBody OrderInfo orderInfo) {
        orderInfo.setId(null);
        orderInfo.setTenantCode(tenantCode);
        orderInfo.setOrderNo(UUID.randomUUID().toString());
        orderInfoService.save(orderInfo);
        Result<OrderInfo> result = new Result<>(ResultCode.OK.val(), ResultCode.OK.msg(), orderInfo);

        return result;
    }


    @ApiOperation(value = "查询订单")
    @PostMapping(value = "/findOrder")
    public Result<List<OrderInfo>> findOrder(@PathVariable String tenantCode, @RequestBody(required = false) OrderInfo orderInfo) {

        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(OrderInfo::getTenantCode, tenantCode);
        if (!StringUtils.isBlank(orderInfo.getOrderNo())) {
            queryWrapper.eq(OrderInfo::getOrderNo, orderInfo.getOrderNo());
        }
        if (!StringUtils.isBlank(orderInfo.getUserId())) {
            queryWrapper.eq(OrderInfo::getUserId, orderInfo.getUserId());
        }
        queryWrapper.orderByDesc(OrderInfo::getCreateTime);
        queryWrapper.last("limit 100");
        List<OrderInfo> list = orderInfoService.list(queryWrapper);
        Result<List<OrderInfo>> result = new Result<>(ResultCode.OK.val(), ResultCode.OK.msg(), list);
        return result;
    }
}
