package com.demo.crm.workbench.service.impl;

import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.workbench.dao.CustomerDao;
import com.demo.crm.workbench.dao.TranDao;
import com.demo.crm.workbench.dao.TranHistoryDao;
import com.demo.crm.workbench.domain.Customer;
import com.demo.crm.workbench.domain.Tran;
import com.demo.crm.workbench.domain.TranHistory;
import com.demo.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public boolean save(Tran tran, String customerName) {
        boolean flag = true;
        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());

            int count1 = customerDao.save(customer);
            if(count1!=1){
                flag = false;
            }
        }


        tran.setCustomerId(customer.getId());
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        int count2 = tranDao.save(tran);
        if(count2!=1){
            flag=false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
        int count3 = tranHistoryDao.save(tranHistory);
        if(count3!=1){
            flag=false;
        }


        return flag;
    }

    @Override
    public Tran detail(String id) {
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

        return tranDao.detail(id);
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String id) {
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
        return tranHistoryDao.getHistoryByTranId(id);
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        int count1 = tranDao.changeStage(tran);
        if(count1!=1){
            flag=false;
        }


        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setTranId(tran.getId());
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
        int count2 = tranHistoryDao.save(tranHistory);
        if(count2!=1){
            flag=false;
        }


        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        Map<String,Object> map = new HashMap<>();

        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        int total = tranDao.getTotal();

        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        List<Map<String,Object>> dataList = tranDao.getCharts();

        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }
}
