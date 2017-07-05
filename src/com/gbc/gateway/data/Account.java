/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.gateway.data;

/**
 *
 * @author tamvh
 */
public class Account {
    private long account_id;
    private String account_name;
    private long merchant_id;
    private int status;

    /**
     * @return the account_id
     */
    public long getAccount_id() {
        return account_id;
    }

    /**
     * @param account_id the account_id to set
     */
    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    /**
     * @return the account_name
     */
    public String getAccount_name() {
        return account_name;
    }

    /**
     * @param account_name the account_name to set
     */
    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    /**
     * @return the merchant_id
     */
    public long getMerchant_id() {
        return merchant_id;
    }

    /**
     * @param merchant_id the merchant_id to set
     */
    public void setMerchant_id(long merchant_id) {
        this.merchant_id = merchant_id;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
