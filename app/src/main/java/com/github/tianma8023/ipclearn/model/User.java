package com.github.tianma8023.ipclearn.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tianma on 2018/2/28.
 */

public class User implements Parcelable{

    public int userId;
    public String username;

    public User(){

    }

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    private User(Parcel source) {
        userId = source.readInt();
        username = source.readString();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(username);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
