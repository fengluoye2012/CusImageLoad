package com.test.imageload.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;

public class BaseDaoFactory {

    private String dataBasePath;
    private SQLiteDatabase sqLiteDatabase;

    private static BaseDaoFactory instance = new BaseDaoFactory();

    public static BaseDaoFactory getInstance() {
        return instance;
    }

    public BaseDaoFactory() {
        dataBasePath = PathUtils.getInternalAppDataPath() + "/Image.db";
        openDataBase();
    }

    private void openDataBase() {
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dataBasePath, null);
    }

    /**
     * 泛型 的使用
     *
     * @return
     */
    public synchronized <T extends BaseDao<M>, M> BaseDao<M> getDataHelper(Class<T> clazz, Class<M> entityClazz) {
        BaseDao<M> baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClazz, sqLiteDatabase);
        } catch (Exception e) {
            LogUtils.e(Log.getStackTraceString(e));
        }
        return baseDao;
    }

}
