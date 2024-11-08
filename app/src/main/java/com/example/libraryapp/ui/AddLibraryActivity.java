package com.example.libraryapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app.R;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.Library;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLibraryActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText openTimeEditText;
    private EditText closeTimeEditText;
    private Button saveLibraryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);

        // Inicializar os views
        nameEditText = findViewById(R.id.libraryNameEditText);
        addressEditText = findViewById(R.id.libraryAddressEditText);
        openTimeEditText = findViewById(R.id.libraryOpenTimeEditText);
        closeTimeEditText = findViewById(R.id.libraryCloseTimeEditText);
        saveLibraryButton = findViewById(R.id.saveLibraryButton);

        // Configurar os NumberPickers para os campos de horário
        openTimeEditText.setOnClickListener(v -> showNumberPickerDialog(openTimeEditText));
        closeTimeEditText.setOnClickListener(v -> showNumberPickerDialog(closeTimeEditText));

        saveLibraryButton.setOnClickListener(v -> saveLibrary());
    }

    private void showNumberPickerDialog(EditText timeEditText) {
        // Cria o layout do diálogo personalizado
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_number_picker, null);

        // Inicializa o NumberPicker
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);  // Define o valor mínimo como 0
        numberPicker.setMaxValue(23); // Define o valor máximo como 23

        // Cria o diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Selecionar Hora")
                .setPositiveButton("OK", (dialog, which) -> {
                    int hour = numberPicker.getValue();
                    String formattedTime = String.format("%02d:00", hour);
                    timeEditText.setText(formattedTime);
                })
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    private void saveLibrary() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String openTime = openTimeEditText.getText().toString();
        String closeTime = closeTimeEditText.getText().toString();

        if (name.isEmpty() || address.isEmpty() || openTime.isEmpty() || closeTime.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar um objeto de biblioteca
        Library newLibrary = new Library();
        newLibrary.setName(name);
        newLibrary.setAddress(address);
        newLibrary.setOpenTime(openTime);
        newLibrary.setCloseTime(closeTime);

        // Enviar a nova biblioteca para a API
        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
        Call<Library> call = api.createLibrary(newLibrary);

        call.enqueue(new Callback<Library>() {
            @Override
            public void onResponse(Call<Library> call, Response<Library> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddLibraryActivity.this, "Library created successfully!", Toast.LENGTH_SHORT).show();

                    // Retorna o resultado à LibrariesActivity
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();  // Fecha a activity após adicionar a biblioteca
                } else {
                    Toast.makeText(AddLibraryActivity.this, "Failed to create library", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Library> call, Throwable t) {
                Toast.makeText(AddLibraryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
