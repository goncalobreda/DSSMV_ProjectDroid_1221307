package com.example.libraryapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;
import com.example.libraryapp.api.LibraryApi;
import com.example.libraryapp.api.RetrofitClient;
import com.example.libraryapp.models.CheckedOutBook;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckedOutBooksAdapter extends RecyclerView.Adapter<CheckedOutBooksAdapter.ViewHolder> {

    private List<CheckedOutBook> checkedOutBooks;
    private Context context;

    public CheckedOutBooksAdapter(List<CheckedOutBook> checkedOutBooks, Context context) {
        this.checkedOutBooks = checkedOutBooks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checked_out_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckedOutBook book = checkedOutBooks.get(position);

        holder.titleTextView.setText(book.getBook().getTitle());
        holder.libraryNameTextView.setText(book.getLibraryName());
        holder.dueDateTextView.setText("Due Date: " + formatDueDate(book.getDueDate()));

        holder.checkInButton.setOnClickListener(v -> {
            String formattedLibraryId = formatLibraryId(book.getLibraryId());

            LibraryApi api = RetrofitClient.getClient("http://193.136.62.24/v1/").create(LibraryApi.class);
            Call<Void> call = api.checkInBook(formattedLibraryId, book.getBook().getIsbn(), book.getUserId());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Check-in successful!", Toast.LENGTH_SHORT).show();
                        checkedOutBooks.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), checkedOutBooks.size());
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                JSONObject jsonObject = new JSONObject(errorBody);
                                String errorMessage = jsonObject.optString("message", "Check-in failed!");

                                // Exibe a mensagem de erro detalhada
                                Toast.makeText(context, "Check-in failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                Log.e("CheckedOutBooksAdapter", "Error response: " + errorBody);
                            } else {
                                Toast.makeText(context, "Check-in failed! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "An error occurred while processing the response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CheckedOutBooksAdapter", "Error during check-in: " + t.getMessage());
                    Toast.makeText(context, "Error during check-in: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return checkedOutBooks.size();
    }

    // Método para formatar o libraryId para o formato de UUID
    private String formatLibraryId(String libraryId) {
        if (libraryId.length() != 32) {
            return libraryId; // Retorna como está se não for do tamanho esperado
        }
        return libraryId.substring(0, 8) + "-" +
                libraryId.substring(8, 12) + "-" +
                libraryId.substring(12, 16) + "-" +
                libraryId.substring(16, 20) + "-" +
                libraryId.substring(20);
    }

    // Método para formatar a data para o formato "YYYY-MM-DD"
    private String formatDueDate(String dueDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(dueDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dueDate;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView libraryNameTextView;
        TextView dueDateTextView;
        Button checkInButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitleTextView);
            libraryNameTextView = itemView.findViewById(R.id.libraryNameTextView);
            dueDateTextView = itemView.findViewById(R.id.dueDateTextView);
            checkInButton = itemView.findViewById(R.id.checkInButton);
        }
    }
}
