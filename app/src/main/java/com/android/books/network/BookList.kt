package com.android.books.network

data class BookList(val items: List<VolumeInfo>)

data class VolumeInfo(val volumeInfo: BookInfo)

data class BookInfo(val title: String?, val publishedDate: String?, val description: String?, val imageLinks: ImageLinks?)

data class ImageLinks(val smallThumbnail: String?)