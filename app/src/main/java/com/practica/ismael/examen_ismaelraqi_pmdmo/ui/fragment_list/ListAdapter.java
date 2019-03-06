package com.practica.ismael.examen_ismaelraqi_pmdmo.ui.fragment_list;

import android.os.LocaleList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.practica.ismael.examen_ismaelraqi_pmdmo.R;
import com.practica.ismael.examen_ismaelraqi_pmdmo.data.local.model.Book;
import com.practica.ismael.examen_ismaelraqi_pmdmo.ui.activity.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends androidx.recyclerview.widget.ListAdapter<Book, ListAdapter.ViewHolder> {

    private final ListViewModel vm;
    private final MainViewModel mainVM;
    private Book data;

    public ListAdapter(ListViewModel vm, MainViewModel mainVM) {
        super(new DiffUtil.ItemCallback<Book>() {
            @Override
            public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem.getIdBook() == newItem.getIdBook();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return TextUtils.equals(oldItem.getNameBook(), newItem.getNameBook()) &&
                        TextUtils.equals(oldItem.getAuthor(), newItem.getAuthor()) &&
                        oldItem.getDate() == newItem.getDate() &&
                        TextUtils.equals(oldItem.getCover(), newItem.getCover());
            }
        });
        this.vm = vm;
        this.mainVM = mainVM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_adapter, parent, false), mainVM);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdBook();
    }

    @Override
    public Book getItem(int position) {
        return super.getItem(position);
    }

    public void removeItem(int position) {
        data = getItem(position);
        vm.deleteBook(getItem(position));
    }

    public void restoreItem(Book item, int position) {
        vm.insertBook(data);
        data = null;
    }

    public Book getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblNameBook;
        private final TextView lblAuthor;
        private final TextView lblDate;
        private final ImageView imgCover;
        private final MainViewModel mainVM;
        private final Picasso picasso = Picasso.get();

        public ViewHolder(View itemView, MainViewModel mainVM) {
            super(itemView);
            lblNameBook = ViewCompat.requireViewById(itemView, R.id.lblNameBook);
            lblAuthor = ViewCompat.requireViewById(itemView, R.id.lblAuthor);
            lblDate = ViewCompat.requireViewById(itemView, R.id.lblDate);
            imgCover = ViewCompat.requireViewById(itemView, R.id.imgCover);
            this.mainVM = mainVM;
            itemView.setOnClickListener(v -> showInfo());
        }

        private void showInfo() {
            mainVM.setShowBookLiveData(getItem(getAdapterPosition()));
        }


        public void bind(Book book) {
            lblNameBook.setText(book.getNameBook());
            lblAuthor.setText(book.getAuthor());
            if (!book.getCover().isEmpty()){
                picasso.load(book.getCover())
                        .placeholder(R.drawable.ic_empty_logo_black_24dp)
                        .error(R.drawable.ic_empty_logo_black_24dp)
                        .into(imgCover);
            }else{
                picasso.load("a")
                        .placeholder(R.drawable.ic_empty_logo_black_24dp)
                        .error(R.drawable.ic_empty_logo_black_24dp)
                        .into(imgCover);
            }
            lblDate.setText(String.valueOf(book.getDate()));
        }

    }
}
