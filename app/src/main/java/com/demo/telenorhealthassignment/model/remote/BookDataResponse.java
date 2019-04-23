package com.demo.telenorhealthassignment.model.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookDataResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("data")
    private List<BookData> bookDataList;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<BookData> getBookDataList() {
        return bookDataList;
    }

    public void setBookDataList(List<BookData> bookDataList) {
        this.bookDataList = bookDataList;
    }

    public class BookData {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("subTitle")
        private String subTitle;
        @SerializedName("description")
        private String description;
        @SerializedName("preview")
        private String preview;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("updatedAt")
        private String updatedAt;

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
            this.subTitle = subTitle;
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

        @Override
        public String toString() {
            return "BookData{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", subTitle='" + subTitle + '\'' +
                    ", description='" + description + '\'' +
                    ", preview='" + preview + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }
    }

}
