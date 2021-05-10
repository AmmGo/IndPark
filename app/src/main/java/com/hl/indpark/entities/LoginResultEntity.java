package com.hl.indpark.entities;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yjl on 2021/3/8 15:28
 * Function：roleId : 1 :44 网格长  45 副网格长 46 网格员
 * Desc：
 */
public class LoginResultEntity implements Parcelable {
    /**
     * id : 1
     * token : 54f1510b02bb4358a3309bc4aa9535f8
     * date : 2021-03-05 17:33:58
     * expirationTime : 2021-03-05 19:33:58
     * name : 管理员
     * deptId : 11
     * deptName : 工程技术部
     * enterpriseId : 358
     * enterpriseName : 中卫市管委会
     * category : 2
     * roleId : 1
     * roleName : 超级管理员
     * menuList : null
     * authURL : null
     * ico : null
     */
    public int id;
    public String token;
    private String date;
    private String expirationTime;
    private String name;
    private int deptId;
    private String deptName;
    public int enterpriseId;
    public String enterpriseName;
    public int category;
    public int roleId;
    private String roleName;
    private Object menuList;
    private Object authURL;
    private Object ico;

    protected LoginResultEntity(Parcel in) {
        this.token = in.readString();
    }

    public static final Parcelable.Creator<LoginResultEntity> CREATOR = new Parcelable.Creator<LoginResultEntity>() {
        @Override
        public LoginResultEntity createFromParcel(Parcel source) {
            return new LoginResultEntity(source);
        }

        @Override
        public LoginResultEntity[] newArray(int size) {
            return new LoginResultEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.token);
    }
}
