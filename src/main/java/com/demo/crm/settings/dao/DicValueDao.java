package com.demo.crm.settings.dao;

import com.demo.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueList(String code);
}
