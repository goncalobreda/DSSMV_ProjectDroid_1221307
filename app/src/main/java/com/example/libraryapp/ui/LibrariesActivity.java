package com.example.libraryapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;
import com.example.libraryapp.adapter.LibraryAdapter;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.Library;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibrariesActivity extends AppCompatActivity implements LibraryAdapter.OnLibraryClickListener {

    private RecyclerView libraryRecyclerView;
    private LibraryAdapter libraryAdapter;
    private List<Library> libraries = new ArrayList<>();

    private static final int ADD_LIBRARY_REQUEST_CODE = 1;
    private static final int EDIT_LIBRARY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryRecyclerView = findViewById(R.id.libraryRecyclerView);
        libraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar o adaptador com lista vazia e anexá-lo à RecyclerView
        libraryAdapter = new LibraryAdapter(libraries, this);
        libraryRecyclerView.setAdapter(libraryAdapter);

        fetchLibraries();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_library_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_library) {
            Intent intent = new Intent(LibrariesActivity.this, AddLibraryActivity.class);
            startActivityForResult(intent, ADD_LIBRARY_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_LIBRARY_REQUEST_CODE || requestCode == EDIT_LIBRARY_REQUEST_CODE) && resultCode == RESULT_OK) {
            fetchLibraries();
        }
    }

    private void fetchLibraries() {
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);

        Call<List<Library>> call = api.getLibraries();
        call.enqueue(new Callback<List<Library>>() {
            @Override
            public void onResponse(Call<List<Library>> call, Response<List<Library>> response) {
                if (response.isSuccessful()) {
                    libraries.clear();
                    libraries.addAll(response.body());
                    libraryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LibrariesActivity.this, "Erro ao encontrar bibliotecas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Library>> call, Throwable t) {
                Toast.makeText(LibrariesActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLibraryClick(String libraryId) {
        Intent intent = new Intent(LibrariesActivity.this, BooksActivity.class);
        intent.putExtra("libraryId", libraryId);
        startActivity(intent);
    }

    @Override
    public void onLibraryEdit(String libraryId) {
        Intent intent = new Intent(LibrariesActivity.this, EditLibraryActivity.class);
        intent.putExtra("libraryId", libraryId); // Passa o ID da biblioteca para edição
        startActivityForResult(intent, EDIT_LIBRARY_REQUEST_CODE);
    }


    @Override
    public void onLibraryDelete(String libraryId) {
        new AlertDialog.Builder(this)
                .setMessage("Tem certeza de que deseja excluir esta biblioteca?")
                .setPositiveButton("Sim", (dialog, which) -> deleteLibrary(libraryId))
                .setNegativeButton("Não", null)
                .show();
    }

    private void deleteLibrary(String libraryId) {
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
        Call<Void> call = api.deleteLibrary(libraryId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LibrariesActivity.this, "Biblioteca excluída com sucesso!", Toast.LENGTH_SHORT).show();
                    fetchLibraries();
                } else {
                    Toast.makeText(LibrariesActivity.this, "Erro ao excluir a biblioteca", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LibrariesActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
