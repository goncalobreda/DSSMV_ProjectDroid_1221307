package com.example.libraryapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carregar o layout do menu
        setContentView(R.layout.activity_main_menu);

        // Configurar o botão para ver as bibliotecas
        MaterialButton viewLibrariesButton = findViewById(R.id.buttonViewLibraries);
        viewLibrariesButton.setOnClickListener(v -> {
            // Navegar para a LibrariesActivity
            Intent intent = new Intent(MainActivity.this, LibrariesActivity.class);
            startActivity(intent);
        });

        // Configurar o botão para "HANDLING CHECKOUTS"
        MaterialButton handlingCheckoutsButton = findViewById(R.id.buttonHandlingCheckouts);
        handlingCheckoutsButton.setOnClickListener(v -> {
            // Criar um AlertDialog para inserir o userId
            EditText userIdInput = new EditText(this);
            userIdInput.setHint("Enter User ID");

            new AlertDialog.Builder(this)
                    .setTitle("User ID")
                    .setMessage("Enter the User ID to check checked-out books:")
                    .setView(userIdInput)
                    .setPositiveButton("Submit", (dialog, which) -> {
                        String userId = userIdInput.getText().toString().trim();
                        if (!userId.isEmpty()) {
                            // Passar o userId para a CheckedOutBooksActivity
                            Intent intent = new Intent(MainActivity.this, CheckedOutBooksActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        } else {
                            userIdInput.setError("User ID is required");
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
