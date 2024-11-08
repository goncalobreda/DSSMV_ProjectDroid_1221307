package com.example.libraryapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.BookRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {

    private EditText isbnEditText;
    private EditText stockEditText;

    private Button addBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        isbnEditText = findViewById(R.id.isbnEditText);
        stockEditText = findViewById(R.id.stockEditText);
        addBookButton = findViewById(R.id.addBookButton);

        // Receber o libraryId da BooksActivity
        String libraryId = getIntent().getStringExtra("libraryId");

        addBookButton.setOnClickListener(v -> {
            String isbn = isbnEditText.getText().toString();
            String stockText = stockEditText.getText().toString();
            int stock = stockText.isEmpty() ? 0 : Integer.parseInt(stockText);

            if (!isbn.isEmpty()) {
                addBookToLibrary(libraryId, isbn, stock);
            } else {
                Toast.makeText(this, "Please enter an ISBN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookToLibrary(String libraryId, String isbn, int stock) {
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);

        // Enviar request para adicionar o livro à biblioteca
        Call<Void> call = api.createBook(libraryId, isbn, new BookRequest(stock));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddBookActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a Activity após adicionar
                } else {
                    Toast.makeText(AddBookActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddBookActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
