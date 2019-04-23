package com.demo.telenorhealthassignment.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.demo.telenorhealthassignment.model.local.book.BookDataDetails;
import com.demo.telenorhealthassignment.model.local.room.BookDataEntry;
import com.demo.telenorhealthassignment.model.remote.BookDataResponse;
import com.demo.telenorhealthassignment.model.remote.LoginResponse;
import com.demo.telenorhealthassignment.model.remote.retrofit.APIInterface;
import com.demo.telenorhealthassignment.model.remote.retrofit.RetrofitClient;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<BookDataDetails>> homeLiveData = new MutableLiveData<>();
    private volatile List<BookDataDetails> bookDataDetailsArrayList;
    public MutableLiveData<List<BookDataDetails>> getHomeLiveData() {
        return homeLiveData;
    }

    public void sendAllBookDetailsRequest(final BookDataRepository bookDataRepository) {
        final APIInterface apiInterface = RetrofitClient.getRetrofitClient().create(APIInterface.class);
        final Call<BookDataResponse> bookDataResponseCall = apiInterface.getBookDataListResponse();
        bookDataResponseCall.enqueue(new Callback<BookDataResponse>() {
            @Override
            public void onResponse(Call<BookDataResponse> call, final Response<BookDataResponse> response) {
                Completable.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        bookDataDetailsArrayList = insertOrUpdateIntoDatabase(response, bookDataRepository);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                homeLiveData.setValue(bookDataDetailsArrayList);
                            }

                            @Override
                            public void onError(Throwable e) {
                                homeLiveData.setValue(null);
                            }
                        });
            }

            @Override
            public void onFailure(Call<BookDataResponse> call, Throwable t) {
                homeLiveData.setValue(null);
            }
        });
    }

    private List<BookDataDetails> insertOrUpdateIntoDatabase(Response<BookDataResponse> response, BookDataRepository bookDataRepository) {
        List<BookDataDetails> bookDataDetailsArrayList = new ArrayList<>();
        for (BookDataResponse.BookData bookData : response.body().getBookDataList()) {
            int id = bookData.getId();
            if (bookDataRepository.getBookById(id).size() == 1) {
                bookDataRepository.delete(id);
            }

            String title = bookData.getTitle();
            String subTitle = bookData.getSubTitle();
            String description = bookData.getDescription();
            String preview = bookData.getPreview();
            String createdAt = bookData.getCreatedAt();
            String updatedAt = bookData.getUpdatedAt();
            int type = Constants.DEFAULT_TYPE;
            BookDataEntry bookDataEntry = new BookDataEntry(id, title, subTitle,
                    description, preview, createdAt, updatedAt, type);
            bookDataRepository.insert(bookDataEntry);
            bookDataDetailsArrayList.add(getBookDataDetails(bookDataEntry));

        }
        return bookDataDetailsArrayList;
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

    public void getAllBookDataFromDatabase(BookDataRepository bookDataRepository) {
        Observable<List<BookDataEntry>> bookDataEntryObservable = bookDataRepository.getAll();

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
                    homeLiveData.setValue(bookDataDetailsArrayList);
                } else {
                    homeLiveData.setValue(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                homeLiveData.setValue(null);
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
