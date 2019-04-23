package com.demo.telenorhealthassignment.model.repository;

import android.app.Application;

import com.demo.telenorhealthassignment.model.local.room.BookDataEntry;
import com.demo.telenorhealthassignment.model.local.room.BookDataDao;
import com.demo.telenorhealthassignment.model.local.room.BookDatabase;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

public class BookDataRepository {
    private BookDataDao bookDataDao;

    public BookDataRepository(Application application) {
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        bookDataDao = bookDatabase.bookDataDao();
    }

    public Observable<List<BookDataEntry>> getAll() {
        return bookDataDao.getAll();
    }

    public Observable<List<BookDataEntry>> getAllBooksByType(int bookType) {
        return bookDataDao.getAllBooksByType(bookType);
    }

    public List<BookDataEntry> getBookById(int id) {
        return bookDataDao.getBookById(id);
    }

    public Single<Integer> updateBookType(int id, int bookType) {
        return bookDataDao.updateBookType(id, bookType);
    }

    public void insert(BookDataEntry bookDataEntry) {
        bookDataDao.insert(bookDataEntry);
    }

    public void delete(int id) {
        bookDataDao.delete(id);
    }
}
