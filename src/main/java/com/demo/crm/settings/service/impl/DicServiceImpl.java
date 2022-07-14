package com.demo.crm.settings.service.impl;

import com.demo.crm.settings.dao.DicTypeDao;
import com.demo.crm.settings.dao.DicValueDao;
import com.demo.crm.settings.domain.DicType;
import com.demo.crm.settings.domain.DicValue;
import com.demo.crm.settings.service.DicService;
import com.demo.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = null;//SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = null; //SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
        Map<String, List<DicValue>> map = new HashMap<>();

        List<DicType> dList = dicTypeDao.getTypeList(); //字典类型列表
        for (DicType dicType : dList) {
            String code = dicType.getCode();
            dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
            List<DicValue> dvList = dicValueDao.getValueList(code); //字典值列表
            map.put(code,dvList);

        }
        return map;
    }
}
