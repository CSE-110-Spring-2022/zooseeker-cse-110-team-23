package com.example.zooseeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.Collections;
import java.util.List;

public class GraphListAdapter extends RecyclerView.Adapter<GraphListAdapter.ViewHolder> {
    private List<GraphListItem> graphItems = Collections.emptyList();


    public void setNewGraphListItems(List<GraphListItem> newGraphListItems) {
        this.graphItems.clear();
        this.graphItems = newGraphListItems;
        notifyDataSetChanged();
//        this.animalListItem = animalListItem;
//        //this.id.setText(Long.toString(animalListItem.id));
//        this.kind.setText(animalListItem.kind);
//        this.name.setText(animalListItem.name);
//        this.tag.setText(animalListItem.tags.toString());
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
        holder.setGraphListItem(graphItems.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //private final TextView id;
        private final TextView kind;
        private final TextView name;
        private final TextView tag;
        private GraphListItem graphListItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.id = itemView.findViewById(R.id.test_animal_id);
            this.kind = itemView.findViewById(R.id.test_animal_kind);
            this.name = itemView.findViewById(R.id.test_animal_name);
            this.tag = itemView.findViewById(R.id.test_animal_tag);
        }

        public void setGraphListItem(GraphListItem graphListItem) {
            this.graphListItem = graphListItem;
            this.kind.setText(graphListItem.kind);
            this.name.setText(graphListItem.name);
            this.tag.setText(graphListItem.tags.toString());
        }
    }


}