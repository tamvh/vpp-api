/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import com.gbc.gateway.database.MySqlFactory;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class AccountModel {

    private static AccountModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());

    public static AccountModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new AccountModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }

    public int checkLogin(String username, String password, JsonObject account) {

        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "account";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            queryStr = String.format("SELECT `account_id`, `account_name` FROM %1$s"
                    + " WHERE `account_name` = '%2$s' AND `password` = PASSWORD('%3$s') AND status = 1",
                    accountTableName, username, password);
            System.out.println("Query login: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    account.addProperty("account_id", rs.getInt("account_id"));
                    account.addProperty("account_name", rs.getString("account_name"));
                    ret = 0;
                } else {
                    ret = 2;
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }

    public int changePW(String username, String o_p, String n_p) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String accountTableName = "account";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            queryStr = String.format("SELECT * FROM %1$s"
                    + " WHERE `account_name` = '%2$s' AND `password` = PASSWORD('%3$s') AND status = 1",
                    accountTableName, username, o_p);
            System.out.println("Query change password: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    queryStr = String.format("UPDATE `account` SET `password`= PASSWORD('%1$s') WHERE `account_name` = '%2$s'", n_p, username);
                    if (stmt.executeUpdate(queryStr) > 0) {
                        ret = 0;
                    }
                } else {
                    ret = 1; //tài khoản không tồn tại
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    }
}
