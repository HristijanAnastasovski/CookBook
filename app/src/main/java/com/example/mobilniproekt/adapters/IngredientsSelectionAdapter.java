package com.example.mobilniproekt.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilniproekt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IngredientsSelectionAdapter extends RecyclerView.Adapter<IngredientsSelectionAdapter.ViewHolder> {

    private List<String> ingredients;
    private Context context;
    public IngredientsSelectionAdapter (Context context) {
        this.context=context;
        this.ingredients = new ArrayList<>();
    }

    public List<String> getIngredients() {
        return ingredients;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_ingredients_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bind(ingredients.get(position));
    }


    @Override
    public int getItemCount() {
        if(ingredients!=null)
            return ingredients.size();
        return 0;
    }

    public void delete(int position) { //removes the row
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    public void updateData(String data) {
        this.ingredients.add(data);
        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ingredientsTextView;
        Button deleteButton;


        public ViewHolder(View itemView) {
            super(itemView);
            ingredientsTextView = (TextView) itemView.findViewById(R.id.selectedIngredientTextView);

            deleteButton = (Button) itemView.findViewById(R.id.deleteSelectedIngredientButton);

            deleteButton.setOnClickListener(this); //button onclick listener



        }

        public void bind(String s) {
            ingredientsTextView.setText(s);
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition()); //calls the method above to delete
        }
    }
}