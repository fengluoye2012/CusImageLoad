package com.test.imageload.db;

public class DbTest {

    public DbTest() {
        BaseDao<UserInfo> baseDao = BaseDaoFactory.getInstance().getDataHelper(UserInfoDao.class, UserInfo.class);
    }
}
