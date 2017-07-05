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
}
