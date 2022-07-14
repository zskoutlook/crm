package com.demo.crm.workbench.service.impl;

import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.workbench.dao.CustomerDao;
import com.demo.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public List<String> getCustomerName(String name) {
        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

        return customerDao.getCustomerName(name);
    }
}
