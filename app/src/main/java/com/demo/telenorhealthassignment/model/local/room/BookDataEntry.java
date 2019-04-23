package com.demo.telenorhealthassignment.model.local.room;

import com.demo.telenorhealthassignment.util.Constants;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_data_table")
public class BookDataEntry {

    public BookDataEntry(int id, String title, String subTitle,
                         String description, String preview, String createdAt,
                         String updatedAt, int type) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
        this.preview = preview;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.type = type;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    private int bookId;

    @ColumnInfo(name = "id_column")
    private int id;

    @ColumnInfo(name = "title_column")
    private String title;

    @ColumnInfo(name = "sub_title_column")
    private String subTitle;

    @ColumnInfo(name = "description_column")
    private String description;

    @ColumnInfo(name = "preview_column")
    private String preview;

    @ColumnInfo(name = "created_at_column")
    private String createdAt;

    @ColumnInfo(name = "updated_at_column")
    private String updatedAt;

    @ColumnInfo(name = "type_column")
    private int type;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getPreview() {
        return preview;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getType() {
        return type;
    }
}
