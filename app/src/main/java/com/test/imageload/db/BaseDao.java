package com.test.imageload.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.test.imageload.db.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模仿GreenDao 如何实现对象关系映射数据库
 *
 * @param <T>
 */
public abstract class BaseDao<T> implements IBaseDao<T> {

    /**
     * 保证实例化一次
     */
    private boolean isInit = false;
    /**
     * 持有操作数据库表所对应的Java类型
     */
    private Class<T> entityCls;

    private SQLiteDatabase database;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 维护表名与成员变量名的映射关系
     */
    private HashMap<String, Field> cacheMap;

    /**
     * 字段名称--类型 对应关系
     */
    Map<String, Object> fileMap;

    /**
     * 初始化，保证只初始化一次
     *
     * @return
     */
    protected synchronized boolean init(Class<T> entityCls, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.entityCls = entityCls;
            this.database = sqLiteDatabase;

            if (entityCls.getAnnotation(DbTable.class) == null) {
                throw new IllegalStateException("请添加DbTable注解");
            } else {
                tableName = entityCls.getSimpleName();
            }

            if (!database.isOpen()) {
                return false;
            }

            if (!StringUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }
            cacheMap = new HashMap<>();
            //initCacheMap();
            initFileMap();
            isInit = true;
        }

        return isInit;
    }

    /**
     * 维护映射关系
     */
    private void initCacheMap() {
        //第一条数据，查询0个数据
        String sql = " select * from " + this.tableName + " limit 1,0 ";
        Cursor cursor = null;
        cursor = database.rawQuery(sql, null);

        //表的列名数组
        String[] columnNames = cursor.getColumnNames();

        //拿到所有的成员变量数组
        Field[] fields = entityCls.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }

        //开始找对应关系
        for (String columnName : columnNames) {
            for (Field field : fields) {

            }
        }
    }


    /**
     * 成员变量名称--类型的对应关系
     */
    public void initFileMap() {
        fileMap = new HashMap<>();
        //拿到所有的成员变量数组
        Field[] fields = entityCls.getFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            if (type == Integer.TYPE) {
                fileMap.put(field.getName(), Integer.TYPE);
            } else if (type == Long.TYPE) {
                fileMap.put(field.getName(), Long.TYPE);
            } else if (type == String.class) {
                fileMap.put(field.getName(), String.class);
            } else if (type == Short.TYPE) {
                fileMap.put(field.getName(), Short.class);
            } else if (type == Object.class) {
                fileMap.put(field.getName(), Object.class);
            }
        }

        LogUtils.i("......");
    }


    @Override
    public Long insert(T entity) {
        getValues(entity);
        return null;
    }


    @Override
    public int update(T entity, T where) {
        return 0;
    }

    @Override
    public int delete(T entity) {
        return 0;
    }

    @Override
    public List<T> query(T entity) {
        return null;
    }

    @Override
    public List<T> query(T entity, String orderBy, int startIndex, int limit) {
        return null;
    }

    @Override
    public List<T> query(String sql) {
        return null;
    }

    //获取字段名称和其value 对应关系
    protected void getValues(T entity) {
        Map<String, Object> values = new HashMap<>();
    }

    /**
     * 创建表
     *
     * @return
     */
    protected abstract String createTable();
}
