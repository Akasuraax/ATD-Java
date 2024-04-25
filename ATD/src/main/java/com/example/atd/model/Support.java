package com.example.atd.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Support {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("forname")
    private String forname;

    @SerializedName("tickets")
    private List<Ticket> ticketList;


    public Support(int id, String name, String forname, List<Ticket> ticketList) {
        this.id = id;
        this.name = name;
        this.forname = forname;
        this.ticketList = ticketList;
    }

    public Support() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForname() {
        return forname;
    }

    public void setForname(String forname) {
        this.forname = forname;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
}
