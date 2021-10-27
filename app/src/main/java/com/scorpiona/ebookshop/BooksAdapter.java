package com.scorpiona.ebookshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpiona.ebookshop.databinding.BookListItemBinding;
import com.scorpiona.ebookshop.model.Book;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.bookViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<Book> books = new ArrayList<>();

    @NonNull
    @Override
    public bookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookListItemBinding bookListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.book_list_item, parent, false);

        return new bookViewHolder(bookListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull bookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookListItemBinding.setBook(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(ArrayList<Book> newBooks) {
//        this.books = books;
//        notifyDataSetChanged();
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new BooksDiffCallback(books, newBooks), false);
        books = newBooks;
        result.dispatchUpdatesTo(BooksAdapter.this);

    }

    class bookViewHolder extends RecyclerView.ViewHolder {
        private BookListItemBinding bookListItemBinding;

        public bookViewHolder(@NonNull BookListItemBinding bookListItemBinding) {
            super(bookListItemBinding.getRoot());
            this.bookListItemBinding = bookListItemBinding;
            bookListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    if (listener != null && clickedPosition!=RecyclerView.NO_POSITION) {
                        listener.onItemClick(books.get(clickedPosition));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
