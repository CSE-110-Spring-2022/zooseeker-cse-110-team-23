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
    private Consumer<AnimalListItem> onDeleteButtonClicked;

    public void setAnimalListItems(List<AnimalListItem> newTodoItems) {
        this.animalItems.clear();
        this.animalItems = newTodoItems;
        notifyDataSetChanged();
    }

    public void setOnTextEditedHandler(BiConsumer<AnimalListItem, String> onTextEdited) {
        this.onTextEditedHandler = onTextEdited;
    }

    public void setOnDeleteButtonClicked(Consumer<AnimalListItem> onDeleteButtonClicked) {
        this.onDeleteButtonClicked = onDeleteButtonClicked;
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
        private Button deleteButton;
        private TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.animal_item_text);
            this.deleteButton = itemView.findViewById(R.id.delete_btn);
            this.distance = itemView.findViewById(R.id.distance);

            this.textView.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    onTextEditedHandler.accept(animalItem, textView.getText().toString());
                }
            });

            this.deleteButton.setOnClickListener(view -> {
                if (onDeleteButtonClicked == null) return;
                onDeleteButtonClicked.accept(animalItem);
            });
        }

        public AnimalListItem getAnimalItem() {
            return animalItem;
        }

        public void setAnimalItem(AnimalListItem animalItem) {
            this.animalItem = animalItem;
            this.textView.setText(animalItem.text);
            this.distance.setText(animalItem.order+"ft");
        }

    }
}