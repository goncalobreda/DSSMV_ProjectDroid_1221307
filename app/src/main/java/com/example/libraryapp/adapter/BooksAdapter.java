package com.example.libraryapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.libraryapp.models.Book;
import com.example.libraryapp.models.BookWrapper;
import com.example.libraryapp.ui.BookDetailsActivity;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private List<BookWrapper> bookList;
    private Context context;

    public BooksAdapter(List<BookWrapper> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookWrapper bookWrapper = bookList.get(position);
        Book book = bookWrapper.getBook();

        if (book != null) {
            Log.d("BooksAdapter", "Binding book: " + book.getTitle() + " by " + book.getAuthors().get(0).getName());

            holder.titleTextView.setText(book.getTitle());
            holder.authorTextView.setText(book.getAuthors().get(0).getName());

            // Verificar se o ISBN está presente
            if (book.getIsbn() != null) {
                // Construir o URL da capa com base no ISBN
                String coverUrl = "http://193.136.62.24/v1/assets/cover/" + book.getIsbn() + "-S.jpg";
                Log.d("BooksAdapter", "Cover URL for book: " + book.getTitle() + " - " + coverUrl);

                Glide.with(context)
                        .load(coverUrl)
                        .into(holder.coverImageView);
            } else {
                // Se não houver ISBN ou URL, deixar o ImageView vazio
                holder.coverImageView.setImageDrawable(null);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("coverUrl", "http://193.136.62.24/v1/assets/cover/" + book.getIsbn() + "-M.jpg");
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthors().get(0).getName());
                intent.putExtra("description", book.getDescription());
                intent.putExtra("stock", bookWrapper.getStock());
                context.startActivity(intent);
            });
        } else {
            Log.d("BooksAdapter", "Book is null in BookWrapper");
        }
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        ImageView coverImageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitle);
            authorTextView = itemView.findViewById(R.id.bookAuthor);
            coverImageView = itemView.findViewById(R.id.bookCover);
        }
    }
}
