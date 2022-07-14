package com.demo.crm.web.listener;

import com.demo.crm.settings.domain.DicValue;
import com.demo.crm.settings.service.DicService;
import com.demo.crm.settings.service.impl.DicServiceImpl;
import com.demo.crm.utils.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.util.*;

/*数据字典+缓存*/
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("服务器缓存处理数据字段开始");
        ServletContext application = sce.getServletContext();

        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = dicService.getAll();

        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字段结束");

        System.out.println("服务器缓存Stage2Possibility.properties开始");
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
        System.out.println(pMap);
        System.out.println("服务器缓存Stage2Possibility.properties结束");
    }


}
