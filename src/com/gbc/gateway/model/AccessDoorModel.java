/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.model;

import com.gbc.gateway.database.MySqlFactory;
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
public class AccessDoorModel {
    private static AccessDoorModel _instance = null;
    private static final Lock createLock_ = new ReentrantLock();
    protected final Logger logger = Logger.getLogger(this.getClass());
    
    public static AccessDoorModel getInstance() throws IOException {
        if (_instance == null) {
            createLock_.lock();
            try {
                if (_instance == null) {
                    _instance = new AccessDoorModel();
                }
            } finally {
                createLock_.unlock();
            }
        }
        return _instance;
    }
    
    public int matchingUser2Door(String u, String mac) {
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String queryStr;
            String TableName = "access_door";
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();

            queryStr = String.format("SELECT * FROM %1$s"
                    + " WHERE `account_name` = '%2$s' AND `mac_address` = '%3$s",
                    TableName, u, mac);
            System.out.println("Query get matchingUser2Door: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    ret = 1; //tài khoản không tồn tại
                }
            }
            if(ret != 1) {
                queryStr = String.format("INSERT INTO %1$s (account_name, mac_address) VALUES ('%2$s', '%3$s')",
                TableName, u, mac);
                System.out.println("Query insert matchingUser2Door: " + queryStr);
                if (stmt.executeUpdate(queryStr) > 0) {
                    ret = 0;
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
    
    public int checkAccessDoor(String username, String mac_address){
        int ret = -1;
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            String queryStr;
            String TableName = "access_door";        
            connection = MySqlFactory.getConnection();
            stmt = connection.createStatement();
            
            // check full access
            queryStr =String.format("SELECT * FROM %1$s"
                    + " WHERE `account_name` = '%2$s' AND `account_type` = 1", 
                    TableName,  username);
            System.out.println("check full access query: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    ret = 0;
                    return ret;
                }               
            }            
            
            queryStr =String.format("SELECT * FROM %1$s"
                    + " WHERE `account_name` = '%2$s' AND `mac_address` = '%3$s'", 
                    TableName,  username, mac_address);
            System.out.println("checkAccessDoor: " + queryStr);
            stmt.execute(queryStr);
            rs = stmt.getResultSet();
            if (rs != null) {
                if (rs.next()) {
                    ret = 0;
                }               
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AccessDoorModel.class.getName()).log(Level.SEVERE, null, ex);
            ret = -1;
        } finally {
            MySqlFactory.safeClose(rs);
            MySqlFactory.safeClose(stmt);
            MySqlFactory.safeClose(connection);
        }
        return ret;
    } 
}
