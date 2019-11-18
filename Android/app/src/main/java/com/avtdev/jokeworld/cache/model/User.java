package com.avtdev.jokeworld.cache.model;

import com.parse.ParseUser;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String id;
    private String username;
    private String email;
    private String image;
    private Boolean notificationEnabled;
    private Date createdAt;
    private Date updateAt;

    public User(){}

    public User(ParseUser parseUser){
        this.id = parseUser.getObjectId();
        this.username = parseUser.getUsername();
        this.email = parseUser.getEmail();
        this.image = parseUser.getString("image");
        this.notificationEnabled = parseUser.getBoolean("notificationEnabled");
        this.createdAt = parseUser.getCreatedAt();
        this.updateAt = parseUser.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(Boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
