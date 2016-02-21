package com.chen.dao;

import com.chen.model.TUser;

public interface TUserDAO extends BaseDao<TUser> {


    int deleteByPrimaryKey(Long id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);
}