package com.finki.mobilniproekt.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finki.mobilniproekt.R;
import com.finki.mobilniproekt.RecipeDetailsActivity;
import com.finki.mobilniproekt.adapters.viewholders.CardItemViewHolder;
import com.finki.mobilniproekt.model.Recipe;
import com.finki.mobilniproekt.room.RecipeDatabase;

import java.util.ArrayList;
import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardItemViewHolder>{
    private List<Recipe> data;
    private Context context;
    private RecipeDatabase database;

    public CardViewAdapter(Context context, RecipeDatabase database) {
        this.data = new ArrayList<>();
        this.context = context;
        this.database=database;
    }

    @NonNull
    @Override
    public CardItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_view_item,viewGroup,false);
        CardItemViewHolder holder = new CardItemViewHolder(view);
        holder.setParent(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CardItemViewHolder cardItemViewHolder, int i) {

        Recipe entity = data.get(i);
        cardItemViewHolder.bind(entity, database, data.size());
        cardItemViewHolder.getParent().setOnClickListener(v-> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(context.getString(R.string.id_details), entity.getRecipe_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        return 0;
    }

    public void updateData(List<Recipe> data) {
        this.data.addAll(data);
        notifyDataSetChanged();

    }


}
