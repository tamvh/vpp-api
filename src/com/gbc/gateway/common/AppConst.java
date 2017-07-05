/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.common;

/**
 *
 * @author tamvh
 */
public class AppConst {
    public static final int INFO_GENERIC = 1;
    
    public static final int ERROR_GENERIC = -1;
    public static final int NO_ERROR = 0;
    public static final int ERROR_ITEM_NAME_EXIST = 1;
    public static final int ERROR_ITEM_CODE_EXIST = 2;
    public static final int ERROR_BARCODE_EXIST = 3;
    public static final int ERROR_INVALID_USER = 100;
    public static final int ERROR_NOT_LOGIN = 101;
    public static final int HAVE_NOT_PERMISSION = 102;
    public static final int ERROR_CHANGE_PASSWORD = 103;
    public static final int ERROR_CUR_PASSWORD_NOT_MATCHING = 104;
    
    //invoice package
    public static final int INFO_INVC_AUDIT_STATUS_COMPLETED   = 1;
    public static final int INFO_INVC_AUDIT_STATUS_INCOMPLETED = 0;
    
    ///refund package
    public static final int ERROR_NO_DATA                   = 20;
    public static final String INFO_REFUND_TRANS_IN_PROCESS    = "-5";
    public static final String INFO_REFUND_INVALID_INFO        = "-3";
    public static final String INFO_REFUND_ERR                 = "0";
    public static final int INFO_REFUND_SUCC              = 1;
    public static final int INFO_REFUND_IN_PROCESS        = 2;
    public static final int INFO_REFUND_WAITING           = 3;
    
    
    //invoice status
    public static final int INFO_INVC_INIT                   = 0;
    public static final int INFO_INVC_CREATE_SUCC            = 1;
    public static final int INFO_INVC_PAYMENT_SUCC           = 2;
    public static final int INFO_INVC_PAYMENT_CANCEL         = 3;
    public static final int ERR_INVC_PAYMENT_JSON_INVALID    = 4;
    public static final int ERR_INVC_CREATE_PAYMENT_FAILED   = 5;
    public static final int INFO_INVC_REFUND_SUCC            = 6;
    public static final int INFO_INVC_REFUND_IN_PROCESS      = 7;
    public static final int INFO_INVC_REFUND_AUTO            = 8;
    
     //username & password email:
    public static String GMAIL_HOST;
    public static String GMAIL_PORT;
    public static String GMAIL_USER;
    public static String GMAIL_PWD;
	
	//audit status
    public static final int INFO_AUDIT_EMPTY                =  0;
    public static final int INFO_AUDIT_SUCC                 =  1;
    public static final int INFO_AUDIT_HAD_DONE             =  2;
    public static final int INFO_AUDIT_WAIT_PAYMENT         =  3;
    public static final int INFO_AUDIT_CANCEL               =  4;
    public static final int INFO_AUDIT_ERROR                =  5;
    
    // payment method
    public static final int PAYMENT_ZALOPAY                = 1;
    public static final int PAYMENT_VNGCARD                = 2;
    public static final int PAYMENT_CASH                   = 4;
    
    // item kind
    public static final short ITEM_KIND_NEW                = 0x0001;
    public static final short ITEM_KIND_FAVORITE           = 0x0002;
    
    // general id merchant
    public static final int ID_IMG_HOST                     = 1;
    public static final int ID_LAST_UPDATE_ITEM             = 2;
    public static final int MAX_NUMBER_DISCOUNT             = 3;
    public static final int TOTAL_NUMBER_DISCOUNT           = 4;
    
    public static String DEVICE_ID = "";
    
    public static final String MD5_KEY                      = "nuTdPPedhzNj3BRR";
    public static final String HMAC_SHA256_KEY              = "7VShsAFE3S4pS3lijpCkIxCDpzi7ljdS";
    public static final String HMAC_SHA256_KEY_2            = "yvJNXhn7lMr8SOD3ltTdzn2hldWYPri9";
    
    // account type
    public static final int ACCOUNT_TYPE_MASTER             = 1;
    public static final int ACCOUNT_TYPE_SUPER              = 2;
    public static final int ACCOUNT_TYPE_ADMIN              = 3;
    public static final int ACCOUNT_TYPE_ACCOUNTANT         = 4;
    
    // function code
    
    public static final int FUNCTION_CODE_REFUND             = 1304;
    
    
    //log code
    public static final int LOG_TASK_INSERT             = 1;
    public static final int LOG_TASK_DELETE             = 2;
    public static final int LOG_TASK_UPDATE             = 3;
    public static final int LOG_TASK_SEARCH             = 4;
    public static final int LOG_TASK_OTHER              = 5;
    
    public static final String FIELD_NAME_ITEM             = "Sản phẩm";
}
