package com.chen.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:定义基础的泛型方法
 * Author: wei
 * DateTime: 2016-02-10
 */
public interface BaseDao<T> {

    /**
     * 根据指定条件查询
     * @param object
     * @return List<T>
     */
    @SuppressWarnings("UnnecessaryInterfaceModifier")
    public List<T> searchByCondition(@Param("object") T object);

    /**
     * 根据对象查询
     * @param object
     * @return  List<T>
     */
    public   List<T> searchByObject(T object);

    /**
     * 数据插入
     * @param object
     * @return int
     */
    public int insertByObject(T object);

    /**
     * 数据更新
     * @param object
     * @return int
     */
    public int updateByObject(T object);


    /**
     * 根据指定code 删除
     * @param code
     * @return int
     */
    public int deleteByCode(String code);


    /**
     *
     * @param object
     * @return int
     */
    public int deleteByObject(T object) ;

}
