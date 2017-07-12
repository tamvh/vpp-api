/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.controller;

import com.gbc.gateway.common.AppConst;
import com.gbc.gateway.common.CommonFunction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.gbc.gateway.common.CommonModel;
import com.gbc.gateway.common.JsonParserUtil;
import com.gbc.gateway.model.AccountModel;
import com.google.gson.JsonObject;
/**
 *
 * @author tamvh
 */
public class LoginController extends HttpServlet {
    protected final Logger logger = Logger.getLogger(this.getClass());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp);
    }
    
    private void handle(HttpServletRequest req, HttpServletResponse resp) {
        try {
            processs(req, resp);
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".handle: " + ex.getMessage(), ex);
        }
    }

    private void processs(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = (req.getPathInfo() != null) ? req.getPathInfo() : "";
        String cmd = req.getParameter("cm") != null ? req.getParameter("cm") : "";
        String data = req.getParameter("dt") != null ? req.getParameter("dt") : "";
        String content = "";
            
        pathInfo = pathInfo.toLowerCase();
        CommonModel.prepareHeader(resp, CommonModel.HEADER_JS);
        
        switch (cmd) {            
            case "logout":
                content = logout(req);
                break;
            case "login":
                content = login(req, data);
                break;
            case "changepassword":
                content = changepassword(req, data);
                break;
            case "insert_account":
                content = insertAccount(req, data);
                break;
            case "block_account":
                content = blockAccount(req, data);
                break;
            
        }
        
        CommonModel.out(content, resp);
    }

    private String logout(HttpServletRequest req) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        
        try {
            CommonFunction.deleteSession(req);
            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "");
        } catch (Exception ex) {
            logger.error(getClass().getSimpleName() + ".logout: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        return content;
    }

    private String login(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String userName = jsonObject.get("u").getAsString();
                String password = jsonObject.get("p").getAsString();

                if (userName.isEmpty() || password.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    JsonObject account = new JsonObject();
                    ret = AccountModel.getInstance().checkLogin(userName, password, account);
                    switch (ret) {
                        case 0:         
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "", account);
                            break;
                        case 1:
                            content = CommonModel.FormatResponse(AppConst.ERROR_INVALID_USER, "Tài khoản đã bị khóa!");
                            break;
                        case 2:
                            content = CommonModel.FormatResponse(AppConst.ERROR_INVALID_USER, "Tên đăng nhập hoặc mật khẩu không đúng!");
                            break;                   
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "Đăng nhập thất bại!");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".login: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }
    
    private String changepassword(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String userName = jsonObject.get("u").getAsString();
                String o_p = jsonObject.get("o_p").getAsString();
                String n_p = jsonObject.get("n_p").getAsString();

                if (userName.isEmpty() || o_p.isEmpty() || n_p.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = AccountModel.getInstance().changePW(userName, o_p, n_p);
                    switch (ret) {
                        case 0:         
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "change password success");
                            break;                 
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "change password faile");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".changepassword: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }

    private String insertAccount(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String u = jsonObject.get("u").getAsString();

                if (u.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = AccountModel.getInstance().insertAccount(u);
                    switch (ret) {
                        case 0:         
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "insert account success");
                            break;                 
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "insert account faile");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".changepassword: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }

    private String blockAccount(HttpServletRequest req, String data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
