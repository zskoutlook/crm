package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryByTranId(String id);
}
