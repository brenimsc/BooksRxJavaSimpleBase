package com.android.books.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.books.network.BookList
import com.android.books.network.BookService
import com.android.books.network.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel: ViewModel() {

    var booksList : MutableLiveData<BookList> = MutableLiveData()
    var loading : MutableLiveData<Boolean> = MutableLiveData(false)

    fun getBooksApi(query: String) {

        val service  = RetrofitInstance.getRetrofitInstance().create(BookService::class.java)
        service.getBooks(query)
            .subscribeOn(Schedulers.io()) //assinar em Thread paralela para io chamadas api
            .observeOn(AndroidSchedulers.mainThread()) //observar na Thread principal
            .subscribe(getBooksListObserverRx()) //assinar para de fato receber

    }

    private fun getBooksListObserverRx() : Observer<BookList> { //precisamos continuar observando
        return object : Observer<BookList> {

            override fun onComplete() {
                //hideProgress
                loading.value = false
            }

            override fun onError(e: Throwable) {
                booksList.postValue(null)
            }

            override fun onNext(book: BookList) {
                booksList.postValue(book)
            }

            override fun onSubscribe(d: Disposable) {
                loading.value = true
            }


        }
    }

}