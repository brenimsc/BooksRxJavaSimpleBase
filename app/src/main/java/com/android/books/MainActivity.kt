package com.android.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.books.adapter.Bookdapter
import com.android.books.databinding.ActivityMainBinding
import com.android.books.network.BookInfo
import com.android.books.network.BookList
import com.android.books.network.VolumeInfo
import com.android.books.viewmodel.MainActivityViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration.VERTICAL
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var key: String
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: Bookdapter
    private lateinit var search: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MainActivityViewModel

    private val listBook by lazy {
        mutableListOf<VolumeInfo>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        initViewModel()

        setupSearch()
        setupRecyclerView()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private fun setupViews() {
        search = binding.txtBookName
        recyclerView = binding.recyclerview
    }

    private fun setupSearch() {
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                key = "$p1,$p2,$p3"
                loadApi(p0.toString())


            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun loadApi(query: String) {

        viewModel.loading.observe(this) {
            if (it) {
                Snackbar.make(binding.root, "Carregando...", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Carregado!", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.booksList.observe(this) { bookList ->
            val array = key.split(",")
            if (bookList != null) {
                binding.layoutEmpty.visibility = View.GONE
                controlLayoutEmpty(array, bookList)
            } else {
                habilitRecycler(array)
                listBook.clear()
                bookAdapter.notifyDataSetChanged()
            }
        }


        viewModel.getBooksApi(query)
    }

    private fun habilitRecycler(array: List<String>) {
        if (array[0] == "0" && array[2] != "0") {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
        }
    }

    private fun controlLayoutEmpty(
        array: List<String>,
        bookList: BookList
    ) {
        if (array[0] == "0" && array[2] == "0") {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
        } else {
            binding.recyclerview.visibility = View.VISIBLE
            listBook.clear()
            bookList.items?.let {
                listBook.addAll(it)
            }
            bookAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(dividerItemDecoration)
            bookAdapter = Bookdapter(listBook)
            adapter = bookAdapter
        }
    }
}