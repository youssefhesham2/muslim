package com.example.muslim.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntintyModel {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name ="bsmla")
    String bsmla;
    @ColumnInfo(name = "zkr")
    String zkr;
    @ColumnInfo(name = "zakrdetils")
    String zkrdetils;
    @ColumnInfo(name ="numberofloop")
    int loop;

    public EntintyModel(String bsmla, String zkr, String zkrdetils, int loop) {
        this.bsmla = bsmla;
        this.zkr = zkr;
        this.zkrdetils = zkrdetils;
        this.loop = loop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBsmla() {
        return bsmla;
    }

    public void setBsmla(String bsmla) {
        this.bsmla = bsmla;
    }

    public String getZkr() {
        return zkr;
    }

    public void setZkr(String zkr) {
        this.zkr = zkr;
    }

    public String getZkrdetils() {
        return zkrdetils;
    }

    public void setZkrdetils(String zkrdetils) {
        this.zkrdetils = zkrdetils;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }
}
