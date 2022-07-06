package com.demo.crm.settings.test;

import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
        System.out.println(1);

        //失效时间
        String i = "2055-10-10 10:10:10";

        //获取当前时间
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataStr = sf.format(date);

        System.out.println(dataStr);

        //使用自己的根据  获取当前时间
        String time = DateTimeUtil.getSysTime();
        System.out.println(time);

        //按位对比字符串  >0前数大   <0后数大
        int i1 = i.compareTo(time);
        System.out.println(i1);


        //判断锁定状态
        String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号已锁定");
        }

        //判断IP是否被允许
        String ip = "127.0.0.3";
        String okIp = "127.0.0.1,127.0.0.2";
        if (okIp.contains(ip)){
            //包含在内，所以ip有效
            System.out.println("有效的IP地址允许访问相同");
        }else {
            System.out.println("IP地址受限，请联系管理员");
        }


        //MD5加密使用工具
        String pw = "123";
        String md5 = MD5Util.getMD5(pw);
        System.out.println(md5);        //202cb962ac59075b964b07152d234b70

        //密码要加盐加密

    }
}
