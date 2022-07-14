package com.demo.crm.workbench.service.impl;

import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.SqlSessionUtil;
import com.demo.crm.utils.UUIDUtil;
import com.demo.crm.vo.PaginationVO;
import com.demo.crm.workbench.dao.*;
import com.demo.crm.workbench.domain.*;
import com.demo.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private ClueDao clueDao =null;
    private ClueActivityRelationDao clueActivityRelationDao =null;
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue clue) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        int total = clueDao.getTotalCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);
        PaginationVO<Clue> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(dataList);
        return paginationVO;
    }

    @Override
    public Clue detail(String id) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        return clueDao.detail(id);
    }

    @Override
    public boolean unbund(String id) {
        clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        boolean flag = true;
        int count =  clueActivityRelationDao.unbund(id);
        if(count !=1){
            flag = false;
        }
        return flag;

    }

    @Override
    public boolean bund(String clueId, String[] activityIds) {
        boolean flag = true;
        for (String activityId : activityIds) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setActivityId(activityId);
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
            int count = clueActivityRelationDao.bund(clueActivityRelation);
            if (count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String creatBy) {
        boolean flag = true;
        String creatTime = DateTimeUtil.getSysTime();

        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        Clue clue = clueDao.getById(clueId);
        String company = clue.getCompany();

        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        Customer customer = customerDao.getCustomerByName(company);
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(creatBy);
            customer.setCreateTime(creatTime);
            customer.setContactSummary(clue.getContactSummary());
            customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }

        }


        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(clue.getId());
        contacts.setCreateTime(creatTime);
        contacts.setCreateBy(creatBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
        int count2 = contactsDao.save(contacts);
        if (count2!=1){
            flag =false;
        }


        clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(creatBy);
            customerRemark.setCreateTime(creatTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag=false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(creatBy);
            contactsRemark.setCreateTime(creatTime);
            contactsRemark.setContactsId(contacts.getId());

            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4!=1){
                flag=false;
            }

        }


        clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());

            contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag=false;
            }
        }



        if (t!=null){
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());
            tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
            int count6 = tranDao.save(t);
            if(count6!=1){
                flag=false;
            }


            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(creatBy);
            tranHistory.setCreateTime(creatTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
            int count7 = tranHistoryDao.save(tranHistory);
            if(count7!=1){
                flag=false;
            }
        }

        for (ClueRemark clueRemark : clueRemarkList) {
            clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag=false;
            }
        }

        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
            int count9 =clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){
                flag=false;
            }
        }

        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        int count10 =clueDao.delete(clueId);
        if(count10!=1){
            flag=false;
        }

        return flag;
    }
}
