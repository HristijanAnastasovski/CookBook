package com.example.mobilniproekt.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilniproekt.R;
import com.example.mobilniproekt.RecipeDetailsActivity;
import com.example.mobilniproekt.adapters.viewholders.CardItemViewHolder;
import com.example.mobilniproekt.adapters.viewholders.IngredientsHolder;
import com.example.mobilniproekt.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder> {
    private List<String> data;
    private Context context;

    public IngredientsAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.ingredients_item,viewGroup,false);
        IngredientsHolder holder = new IngredientsHolder(view);
        holder.setParent(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsHolder ingredientsHolder, int i) {

        String ingredient = data.get(i);
        ingredientsHolder.bind(ingredient);

    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        return 0;
    }

    public void updateData(List <String> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        notifyDataSetChanged();

    }

}

