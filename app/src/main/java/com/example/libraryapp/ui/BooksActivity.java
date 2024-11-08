package com.example.libraryapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;
import com.example.libraryapp.adapter.BooksAdapter;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.BookWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private BooksAdapter booksAdapter;


    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar a lista de livros sempre que a activity volta a estar ativa
        String libraryId = getIntent().getStringExtra("libraryId");
        if (libraryId != null) {
            fetchBooks(libraryId);  // Buscar novamente os livros da biblioteca
        } else {
            Toast.makeText(this, "Error: Library not found.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        booksRecyclerView = findViewById(R.id.booksRecyclerView);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String libraryId = getIntent().getStringExtra("libraryId");
        if (libraryId != null) {
            fetchBooks(libraryId);
        } else {
            Toast.makeText(this, "Error: Library not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_book) {
            // Abrir a activity para adicionar um novo livro
            Intent intent = new Intent(BooksActivity.this, AddBookActivity.class);
            intent.putExtra("libraryId", getIntent().getStringExtra("libraryId")); // Passar o libraryId
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchBooks(String libraryId) {
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
        Call<List<BookWrapper>> call = api.getBooksByLibrary(libraryId);

        call.enqueue(new Callback<List<BookWrapper>>() {
            @Override
            public void onResponse(Call<List<BookWrapper>> call, Response<List<BookWrapper>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookWrapper> books = response.body();
                    Log.d("BooksActivity", "Livros recebidos: " + books.toString());
                    displayBooks(books);
                } else {
                    Log.e("BooksActivity", "Erro na resposta: " + response.code() + " - " + response.message());
                    Toast.makeText(BooksActivity.this, "Erro ao buscar livros.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BookWrapper>> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBooks(List<BookWrapper> books) {
        booksAdapter = new BooksAdapter(books, this);
        booksRecyclerView.setAdapter(booksAdapter);
    }
}
