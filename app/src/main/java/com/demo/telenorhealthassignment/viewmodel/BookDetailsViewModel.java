package com.demo.telenorhealthassignment.viewmodel;

import android.util.Log;
import com.demo.telenorhealthassignment.model.repository.BookDataRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class BookDetailsViewModel extends ViewModel {
    private final MutableLiveData<Integer> integerMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> getIntegerMutableLiveData() {
        return integerMutableLiveData;
    }

    public void updateBookType(final BookDataRepository bookDataRepository, final int id, final int type) {
        Single<Integer> integerSingle = Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(final SingleEmitter<Integer> emitter) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Single<Integer> result = bookDataRepository.updateBookType(id, type);
                        emitter.onSuccess(result.blockingGet());
                    }
                }).start();
            }
        });

        integerSingle.subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                Log.v("updateBookType", integer.toString());
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
