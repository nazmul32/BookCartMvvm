package com.demo.telenorhealthassignment.viewmodel;

import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.model.local.room.BookDataEntry;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<List<BookDataDetails>> cardLiveData = new MutableLiveData<>();
    public MutableLiveData<List<BookDataDetails>> getCartLiveData() {
        return cardLiveData;
    }

    private BookDataDetails getBookDataDetails(BookDataEntry bookDataEntry) {
        BookDataDetails bookDataDetails = new BookDataDetails();
        bookDataDetails.setId(bookDataEntry.getId());
        bookDataDetails.setTitle(bookDataEntry.getTitle());
        bookDataDetails.setSubTitle(bookDataEntry.getSubTitle());
        bookDataDetails.setDescription(bookDataEntry.getDescription());
        bookDataDetails.setPreview(bookDataEntry.getPreview());
        bookDataDetails.setCreatedAt(bookDataEntry.getCreatedAt());
        bookDataDetails.setUpdatedAt(bookDataEntry.getUpdatedAt());
        bookDataDetails.setType(bookDataEntry.getType());

        return bookDataDetails;
    }

    public void getAllCartListBookDataFromDatabase(BookDataRepository bookDataRepository) {
        Observable<List<BookDataEntry>> bookDataEntryObservable = bookDataRepository
                .getAllBooksByType(Constants.CART_LIST_TYPE);

        Observer<List<BookDataEntry>> observer = new Observer<List<BookDataEntry>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<BookDataEntry> bookDataEntryList) {
                List<BookDataDetails> bookDataDetailsArrayList = new ArrayList<>();
                if (bookDataEntryList != null) {
                    for (BookDataEntry bookDataEntry : bookDataEntryList) {
                        bookDataDetailsArrayList.add(getBookDataDetails(bookDataEntry));
                    }
                    cardLiveData.setValue(bookDataDetailsArrayList);
                } else {
                    cardLiveData.setValue(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                cardLiveData.setValue(null);
            }

            @Override
            public void onComplete() {

            }
        };

        bookDataEntryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
