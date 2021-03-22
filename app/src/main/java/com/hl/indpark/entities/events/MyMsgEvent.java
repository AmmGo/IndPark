package com.hl.indpark.entities.events;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MyMsgEvent {
    public List<RecordsBean> records;

    public static class RecordsBean implements Parcelable {

        //parcelable需要
        protected RecordsBean(Parcel in) {
            pushTime = in.readString();
            name = in.readString();
            id = in.readInt();
            read = in.readInt();
        }
        public static final Creator<RecordsBean> CREATOR = new Creator<RecordsBean>() {
            @Override
            public RecordsBean createFromParcel(Parcel in) {
                return new RecordsBean(in);
            }

            @Override
            public RecordsBean[] newArray(int size) {
                return new RecordsBean[size];
            }
        };
        public int id;
        public int read;

        public int enterpriseId;

        public String name;

        public String remarks;

        public String pushTime;

        public Integer isDelete;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(pushTime);
            parcel.writeInt(id);
            parcel.writeInt(read);
        }
    }
}
