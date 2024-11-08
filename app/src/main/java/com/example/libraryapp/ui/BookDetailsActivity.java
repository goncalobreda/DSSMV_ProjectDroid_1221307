package com.example.libraryapp.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.app.R;

public class BookDetailsActivity extends AppCompatActivity {

    private ImageView bookCoverImageView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView descriptionTextView;
    private TextView stockTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Inicializar os views
        bookCoverImageView = findViewById(R.id.bookCoverImageView);
        titleTextView = findViewById(R.id.bookTitleTextView);
        authorTextView = findViewById(R.id.bookAuthorTextView);
        descriptionTextView = findViewById(R.id.bookDescriptionTextView);
        stockTextView = findViewById(R.id.bookStockTextView);

        // Receber os dados passados pelo Intent
        String coverUrl = getIntent().getStringExtra("coverUrl");
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String description = getIntent().getStringExtra("description");
        int stock = getIntent().getIntExtra("stock", 0);

        // Definir os valores nos views
        titleTextView.setText(title);
        authorTextView.setText(author);
        descriptionTextView.setText(description);
        stockTextView.setText("Stock: " + stock);

        // Carregar a imagem do cover do livro
        Glide.with(this)
                .load(coverUrl)
                .into(bookCoverImageView);
    }
}
