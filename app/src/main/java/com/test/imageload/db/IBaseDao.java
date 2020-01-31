package com.test.imageload.db;

import java.util.List;

/**
 * 数据库操作接口
 */
public interface IBaseDao<T> {

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    Long insert(T entity);


    /**
     * 更新数据
     *
     * @param entity
     * @param where
     * @return
     */
    int update(T entity, T where);

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    int delete(T entity);

    /**
     * 查询数据
     *
     * @param entity
     * @return
     */
    List<T> query(T entity);

    List<T> query(T entity, String orderBy, int startIndex, int limit);

    List<T> query(String sql);

}

