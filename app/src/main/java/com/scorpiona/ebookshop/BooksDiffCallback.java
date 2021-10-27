package com.scorpiona.ebookshop;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.scorpiona.ebookshop.model.Book;

import java.util.ArrayList;

public class BooksDiffCallback extends DiffUtil.Callback {
    ArrayList<Book> oldBooksList;
    ArrayList<Book> newBooksList;

    public BooksDiffCallback(ArrayList<Book> oldBooksList, ArrayList<Book> newBooksList) {
        this.oldBooksList = oldBooksList;
        this.newBooksList = newBooksList;
    }

    @Override
    public int getOldListSize() {
        return oldBooksList==null ? 0:oldBooksList.size();
    }

    @Override
    public int getNewListSize() {
        return newBooksList==null ? 0:newBooksList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBooksList.get(oldItemPosition).getBookId()==newBooksList.get(newItemPosition).getBookId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBooksList.get(oldItemPosition).equals(newBooksList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
