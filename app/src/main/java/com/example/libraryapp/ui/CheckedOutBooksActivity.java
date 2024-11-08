package com.example.libraryapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;
import com.example.libraryapp.adapter.CheckedOutBooksAdapter;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.CheckedOutBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class CheckedOutBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckedOutBooksAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_out_books);

        // Inicializar o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCheckedOutBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Receber o userId passado pela MainActivity
        String userId = getIntent().getStringExtra("userId");

        if (userId != null && !userId.isEmpty()) {
            fetchCheckedOutBooks(userId);
        } else {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchCheckedOutBooks(String userId) {
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
        Call<List<CheckedOutBook>> call = api.getCheckedOutBooks(userId);

        call.enqueue(new Callback<List<CheckedOutBook>>() {
            @Override
            public void onResponse(Call<List<CheckedOutBook>> call, Response<List<CheckedOutBook>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CheckedOutBook> checkedOutBooks = response.body();
                    adapter = new CheckedOutBooksAdapter(checkedOutBooks, CheckedOutBooksActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(CheckedOutBooksActivity.this, "No books found for this user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CheckedOutBook>> call, Throwable t) {
                Log.e("CheckedOutBooksActivity", "Error fetching data: " + t.getMessage());
                Toast.makeText(CheckedOutBooksActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
