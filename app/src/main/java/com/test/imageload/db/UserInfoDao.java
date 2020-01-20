package com.test.imageload.db;

public class UserInfoDao extends BaseDao<UserInfo> {

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(user_Id int,name varchar(20),password varchar(10))";
    }
}
