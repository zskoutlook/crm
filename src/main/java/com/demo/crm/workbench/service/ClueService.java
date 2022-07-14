package com.demo.crm.workbench.service;

import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.domain.Clue;
import com.demo.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityIds);


    boolean convert(String clueId, Tran t, String creatBy);
}
