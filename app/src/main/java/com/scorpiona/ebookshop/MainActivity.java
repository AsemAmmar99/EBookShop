package com.scorpiona.ebookshop;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpiona.ebookshop.databinding.ActivityMainBinding;
import com.scorpiona.ebookshop.model.Book;
import com.scorpiona.ebookshop.model.Category;
import com.scorpiona.ebookshop.viewmodel.MainActivityViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<Category> categoriesList;
    private ArrayList<Book> bookArrayList;
    private MainActivityViewModel mainActivityViewModel;
    private MainActivityClickHandlers handlers;
    private Category selectedCategory;
    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;
    private int selectedBookId;
    public static final int ADD_BOOK_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        handlers = new MainActivityClickHandlers();
        binding.setClickHandlers(handlers);

        mainActivityViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {

                categoriesList = (ArrayList<Category>)categories;

                for (Category c : categories) {
                    Log.i("MyTag", c.getCategoryName());
                }
                showOnSpinner();
            }
        });
    }

    private void showOnSpinner(){

        ArrayAdapter<Category> categoryArrayAdapter=new ArrayAdapter<Category>(this, R.layout.spinner_item, categoriesList);
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        binding.setSpinnerAdapter(categoryArrayAdapter);

    }

    private void loadBooksArrayList(int categoryId){
        mainActivityViewModel.getBooksOfASelectedCategory(categoryId).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookArrayList = (ArrayList<Book>) books;
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView(){
        booksRecyclerView = binding.secondaryLayout.rvBooks;
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerView.setHasFixedSize(true);
        booksAdapter = new BooksAdapter();
        booksRecyclerView.setAdapter(booksAdapter);
        booksAdapter.setBooks(bookArrayList);
        booksAdapter.setListener(new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                selectedBookId = book.getBookId();
                Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
                intent.putExtra(AddAndEditActivity.BOOK_ID, selectedBookId);
                intent.putExtra(AddAndEditActivity.BOOK_NAME, book.getBookName());
                intent.putExtra(AddAndEditActivity.UNIT_PRICE, book.getUnitPrice());
                startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Book bookToDelete=bookArrayList.get(viewHolder.getAdapterPosition());
                mainActivityViewModel.deleteBook(bookToDelete);
            }
        }).attachToRecyclerView(booksRecyclerView);

    }

    public class MainActivityClickHandlers {

        public void onFABClicked(View view) {

            // Toast.makeText(getApplicationContext(), "FAB Clicked", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, AddAndEditActivity.class);
            startActivityForResult(intent, ADD_BOOK_REQUEST_CODE);
        }

        public void onSelectItem(AdapterView<?> parent, View view, int pos, long id) {

            selectedCategory = (Category) parent.getItemAtPosition(pos);

            String message = " id is " + selectedCategory.getId() + "\n name is " + selectedCategory.getCategoryName() + "\n email is " + selectedCategory.getCategoryDescription();

            // Showing selected spinner item
//            Toast.makeText(parent.getContext(), message, Toast.LENGTH_LONG).show();

            loadBooksArrayList(selectedCategory.getId());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("BookIdTest"," at 4 top id is "+selectedBookId);
        int selectedCategoryId=selectedCategory.getId();
        if(requestCode==ADD_BOOK_REQUEST_CODE && resultCode==RESULT_OK){
            Log.i("BookIdTest"," at 4 wrong 2 id is "+selectedBookId);
            Book book=new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.UNIT_PRICE));
            mainActivityViewModel.addNewBook(book);



        } else if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == RESULT_OK) {

            Book book=new Book();
            book.setCategoryId(selectedCategoryId);
            book.setBookName(data.getStringExtra(AddAndEditActivity.BOOK_NAME));
            book.setUnitPrice(data.getStringExtra(AddAndEditActivity.UNIT_PRICE));
            Log.i("BookIdTest"," at 4 id is "+selectedBookId);
            book.setBookId(selectedBookId);
            mainActivityViewModel.updateBook(book);


        }
    }

}