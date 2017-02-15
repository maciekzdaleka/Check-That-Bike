package com.example.maciek.myapplication;

/**
 * Created by Maciek on 09/02/2017.
 */

public class Bike {
    private int id;
    private String name, make, model, frame_no, desc;
    private byte [] image;

    public Bike(int id, String name, String make, String model, String frame_no, String desc, byte[] image) {
        this.id = id;
        this.name = name;
        this.make = make;
        this.model = model;
        this.frame_no = frame_no;
        this.desc = desc;
        this.image = image;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFrame_no() {
        return frame_no;
    }

    public void setFrame_no(String frame_no) {
        this.frame_no = frame_no;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
