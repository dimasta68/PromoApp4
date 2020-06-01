package com.beru007.promoapp;

public class TransactionObject {

    private String tName;
    private double amount;

    public TransactionObject(String tName, double amount) {
        this.tName = tName;
        this.amount = amount;
    }

    public String gettName() {
        return tName;
    }

    public double getAmount() {
        return amount;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}