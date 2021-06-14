package com.example.miniproj;

public class OrderModel {
  String location,time , total,userCartItem ,userName ,phoneNo;

  long priority;

    public OrderModel() {
        //for firebase
    }

    public OrderModel(String location, String time, String total, String userCartItem, String userName, String phoneNo, long priority) {
        this.location = location;
        this.time = time;
        this.total = total;
        this.userCartItem = userCartItem;
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.priority = priority;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getTotal() {
        return total;
    }

    public String getUserCartItem() {
        return userCartItem;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public long getPriority() {
        return priority;
    }
}
