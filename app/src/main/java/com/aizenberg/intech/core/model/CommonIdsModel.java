package com.aizenberg.intech.core.model;

/**
 * Created by Yuriy Aizenberg
 */
public class CommonIdsModel extends BaseModel {
    private int id;
    private String title;
    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
