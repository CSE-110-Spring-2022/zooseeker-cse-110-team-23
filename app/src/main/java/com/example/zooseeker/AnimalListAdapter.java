package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.ViewHolder> {
    private List<AnimalListItem> animalItems = Collections.emptyList();
    private BiConsumer<AnimalListItem, String> onTextEditedHandler;

    public void setAnimalListItems(List<AnimalListItem> newTodoItems) {
        this.animalItems.clear();
        this.animalItems = newTodoItems;
        notifyDataSetChanged();
    }

    public void setOnTextEditedHandler(BiConsumer<AnimalListItem, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.animal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setAnimalItem(animalItems.get(position));
    }

    @Override
    public int getItemCount() {
        return animalItems.size();
    }

    @Override
    public long getItemId(int position) { return animalItems.get(position).id;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private AnimalListItem animalItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.animal_item_text);

            this.textView.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    onTextEditedHandler.accept(animalItem, textView.getText().toString());
                }
            });
        }

        public AnimalListItem getAnimalItem() {
            return animalItem;
        }

        public void setAnimalItem(AnimalListItem todoItem) {
            this.animalItem = todoItem;
            this.textView.setText(todoItem.text);
        }
    }
}
