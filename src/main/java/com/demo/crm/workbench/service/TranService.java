package com.demo.crm.workbench.service;

import com.demo.crm.workbench.domain.Tran;
import com.demo.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryByTranId(String id);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
