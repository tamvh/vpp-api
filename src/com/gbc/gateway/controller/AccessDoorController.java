/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.controller;

import com.gbc.gateway.common.AppConst;
import com.gbc.gateway.common.CommonModel;
import com.gbc.gateway.common.JsonParserUtil;
import com.gbc.gateway.model.AccessDoorModel;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author tamvh
 */
public class AccessDoorController extends HttpServlet{
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
            case "access":
                content = access(req, data);
                break; 
            case "matchinguser2door":
                content = matchinguser2door(req, data);
                break; 
        }
        
        CommonModel.out(content, resp);
    }

    private String access(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String userName = jsonObject.get("u").getAsString();
                String mac_address = jsonObject.get("m").getAsString();

                if (userName.isEmpty() || mac_address.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = AccessDoorModel.getInstance().checkAccessDoor(userName, mac_address);
                    if(ret == 0) {
                        content = CommonModel.FormatResponse(AppConst.NO_ERROR, "access");
                    } else {
                        content = CommonModel.FormatResponse(AppConst.ERROR_GENERIC, "no access");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".login: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }

    private String matchinguser2door(HttpServletRequest req, String data) {
        String content;
        int ret = AppConst.ERROR_GENERIC;
        
        try {
            JsonObject jsonObject = JsonParserUtil.parseJsonObject(data);
            if (jsonObject == null) {
                content = CommonModel.FormatResponse(ret, "Invalid parameter");
            } else {                
                String userName = jsonObject.get("u").getAsString();
                String mac_address = jsonObject.get("m").getAsString();

                if (userName.isEmpty() || mac_address.isEmpty()) {
                    content = CommonModel.FormatResponse(ret, "Invalid parameter");
                } else {
                    ret = AccessDoorModel.getInstance().matchingUser2Door(userName, mac_address);
                    switch (ret) {
                        case 0:
                            content = CommonModel.FormatResponse(AppConst.NO_ERROR, "matching success");
                            break;
                        case 1:
                            content = CommonModel.FormatResponse(AppConst.ERROR_USER_AND_MACADDRES_MATCHING_IS_EXISTED, "this maching is existed");
                            break;
                        default:
                            content = CommonModel.FormatResponse(AppConst.ERROR_USER_AND_MACADDRES_NOT_MATCHING, "matching faile");
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(getClass().getSimpleName() + ".matchinguser2door: " + ex.getMessage(), ex);
            content = CommonModel.FormatResponse(ret, ex.getMessage());
        }
        
        return content;
    }
}
