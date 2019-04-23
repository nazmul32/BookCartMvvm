package com.demo.telenorhealthassignment.model.local.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface BookDataDao {
    @Query("SELECT * FROM book_data_table order by id_column asc")
    Observable<List<BookDataEntry>> getAll();

    @Query("SELECT * FROM book_data_table WHERE type_column=:bookType")
    Observable<List<BookDataEntry>> getAllBooksByType(int bookType);

    @Query("SELECT * FROM book_data_table WHERE id_column=:id")
    List<BookDataEntry> getBookById(int id);

    @Query("UPDATE book_data_table SET type_column=:bookType WHERE id_column = :id")
    Single<Integer> updateBookType(int id, int bookType);

    @Insert
    void insert(BookDataEntry bookDataEntry);

    @Query("DELETE FROM book_data_table WHERE id_column=:id")
    void delete(int id);
}
