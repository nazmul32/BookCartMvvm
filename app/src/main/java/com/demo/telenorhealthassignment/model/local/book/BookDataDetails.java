package com.demo.telenorhealthassignment.model.local.book;

import android.os.Parcel;
import android.os.Parcelable;

public class BookDataDetails implements Parcelable {
    private int id;
    private String title;
    private String subTitle;
    private String description;
    private String preview;
    private String createdAt;
    private String updatedAt;
    private int type;

    public BookDataDetails() {}

    protected BookDataDetails(Parcel in) {
        id = in.readInt();
        title = in.readString();
        subTitle = in.readString();
        description = in.readString();
        preview = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        type = in.readInt();
    }

    public static final Creator<BookDataDetails> CREATOR = new Creator<BookDataDetails>() {
        @Override
        public BookDataDetails createFromParcel(Parcel in) {
            return new BookDataDetails(in);
        }

        @Override
        public BookDataDetails[] newArray(int size) {
            return new BookDataDetails[size];
        }
    };

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(description);
        dest.writeString(preview);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(type);

    }
}
