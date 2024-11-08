package com.example.libraryapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import android.view.LayoutInflater;
import android.widget.NumberPicker;

public class EditLibraryActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText openTimeEditText;
    private EditText closeTimeEditText;
    private Button saveLibraryButton;

    private String libraryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);  // Reutiliza o layout de adicionar biblioteca

        nameEditText = findViewById(R.id.libraryNameEditText);
        addressEditText = findViewById(R.id.libraryAddressEditText);
        openTimeEditText = findViewById(R.id.libraryOpenTimeEditText);
        closeTimeEditText = findViewById(R.id.libraryCloseTimeEditText);
        saveLibraryButton = findViewById(R.id.saveLibraryButton);

        // Recebe o ID e detalhes da biblioteca para pré-preencher os campos
        libraryId = getIntent().getStringExtra("libraryId");
        String name = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        String openTime = getIntent().getStringExtra("openTime");
        String closeTime = getIntent().getStringExtra("closeTime");

        // Pré-preenche os campos com as informações recebidas
        if (name != null) nameEditText.setText(name);
        if (address != null) addressEditText.setText(address);
        if (openTime != null) openTimeEditText.setText(openTime);
        if (closeTime != null) closeTimeEditText.setText(closeTime);

        // Configurar os NumberPickers para os campos de horário
        openTimeEditText.setOnClickListener(v -> showNumberPickerDialog(openTimeEditText));
        closeTimeEditText.setOnClickListener(v -> showNumberPickerDialog(closeTimeEditText));

        saveLibraryButton.setOnClickListener(v -> updateLibrary());
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

    private void updateLibrary() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String openTime = openTimeEditText.getText().toString();
        String closeTime = closeTimeEditText.getText().toString();

        if (name.isEmpty() || address.isEmpty() || openTime.isEmpty() || closeTime.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um objeto Library com os dados atualizados
        Library updatedLibrary = new Library();
        updatedLibrary.setId(libraryId);
        updatedLibrary.setName(name);
        updatedLibrary.setAddress(address);
        updatedLibrary.setOpenTime(openTime);
        updatedLibrary.setCloseTime(closeTime);

        LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
        Call<Void> call = api.updateLibrary(libraryId, updatedLibrary);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditLibraryActivity.this, "Biblioteca atualizada com sucesso!", Toast.LENGTH_SHORT).show();

                    // Envia o resultado para a LibrariesActivity para recarregar a lista
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditLibraryActivity.this, "Erro ao atualizar biblioteca", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditLibraryActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
