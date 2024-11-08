package com.example.libraryapp.adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.app.R;
import com.example.libraryapp.models.Library;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    public List<Library> libraryList;
    private OnLibraryClickListener listener;

    public interface OnLibraryClickListener {
        void onLibraryClick(String libraryId);
        void onLibraryEdit(String libraryId);  // Para edição
        void onLibraryDelete(String libraryId); // Para exclusão
    }

    public LibraryAdapter(List<Library> libraryList, OnLibraryClickListener listener) {
        this.libraryList = libraryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        Library library = libraryList.get(position);

        holder.libraryNameTextView.setText(library.getName());
        holder.libraryAddressTextView.setText(library.getAddress());
        String openHours = "Open: " + library.getOpenTime() + " - " + library.getCloseTime();
        holder.libraryHoursTextView.setText(openHours);

        holder.itemView.setOnClickListener(v -> listener.onLibraryClick(library.getId()));

        // Menu de contexto ao fazer long click
        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.itemView);
            popup.inflate(R.menu.context_menu_library);
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit_library) {
                    listener.onLibraryEdit(library.getId());
                    return true;
                } else if (item.getItemId() == R.id.action_delete_library) {
                    listener.onLibraryDelete(library.getId());
                    return true;
                }
                return false;
            });
            popup.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        TextView libraryNameTextView;
        TextView libraryAddressTextView;
        TextView libraryHoursTextView;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            libraryNameTextView = itemView.findViewById(R.id.libraryName);
            libraryAddressTextView = itemView.findViewById(R.id.libraryAddress);
            libraryHoursTextView = itemView.findViewById(R.id.libraryHours);
        }
    }
}
