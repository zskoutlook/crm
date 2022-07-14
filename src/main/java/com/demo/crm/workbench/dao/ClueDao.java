package com.demo.crm.workbench.dao;

import com.demo.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
