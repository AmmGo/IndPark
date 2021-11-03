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
     * token : f66fa26c08464f139f5fc34ebd191a17
     * userId : 3
     * nickName : 测试企业负责人
     * account : eventEnt
     * phone : 18123456789
     * name : 测试企业负责人
     * sex : 0
     * deptId : 28
     * deptName : 生产运营部
     * enterpriseId : null
     * enterpriseName : null
     * roleId : 1
     * roleName : 生产运营部
     * ico : null
     */

    public String token;
    public int userId;
    public String nickName;
    public String account;
    public String phone;
    public String name;
    public int sex;
    public int deptId;
    public String deptName;
    public String enterpriseId;
    public String enterpriseName;
    public int roleId;
    public String roleName;
    public String ico;

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
