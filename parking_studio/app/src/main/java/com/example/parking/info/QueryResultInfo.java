package com.example.parking.info;

/**
 * Created by gouyifan on 10/12/17.
 */

public class QueryResultInfo {
    private CommonRequestHeader header;
    private String orderID;
    private String type;

    public CommonRequestHeader getheader() {
        return header;
    }
    public void setHeader(CommonRequestHeader header) {
        this.header = header;
    }

    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID= orderID;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type= type;
    }

    @Override
    public String toString() {
        return "PaymentInfo [header=" + header +  "orderID=" + orderID + " type=" + type + "]";
    }
}
