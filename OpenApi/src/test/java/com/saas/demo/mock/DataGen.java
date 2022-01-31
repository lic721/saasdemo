package com.saas.demo.mock;

import com.saas.demo.OpenapiApplication;
import com.saas.demo.common.entity.OrderInfo;
import com.saas.demo.common.entity.Tenant;
import com.saas.demo.common.service.OrderInfoService;
import com.saas.demo.common.service.TenantService;
import com.saas.demo.common.util.RedisUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenapiApplication.class)
public class DataGen {

    @Autowired
    TenantService tenantService;

    @Autowired
    OrderInfoService orderInfoService;

    @Test
    public void redis() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(2L);
        RedisUtil.set("test", orderInfo, 100);
        OrderInfo test = (OrderInfo)RedisUtil.get("test");
        assert test!=null && 2L == test.getId();
    }

    @Test
    public void generateTenant() {
        for (int i = 0; i < 1000; i++) {
            try{
                Tenant tenant = new Tenant();
                tenant.setTenantCode("t" + i);
                tenant.setName("商户" + i);
                tenantService.save(tenant);
                System.out.println("exec:" + i);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    @Test
    public void generateOrderInfo() {
        Random rand = new Random(10);
        Date date = new Date();
        for (int i = 0; i < 100000; i++) {
            try {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setTenantCode("t" + rand.nextInt(1000));
                orderInfo.setOrderNo(UUID.randomUUID().toString());
                orderInfo.setUserId("user" + rand.nextInt(10000));
                // 随机90天内
                orderInfo.setCreateTime(DateUtils.addSeconds(date, -rand.nextInt(7776000)));
                orderInfoService.save(orderInfo);
                System.out.println("exec:" + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
