package com.android.books.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("volumes")
    fun getBooks(@Query("q") query: String) : Observable<BookList>
}