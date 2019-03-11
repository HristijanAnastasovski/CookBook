package com.example.mobilniproekt.adapters.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobilniproekt.R;
import com.example.mobilniproekt.model.Recipe;
import com.example.mobilniproekt.room.RecipeDatabase;
import com.example.mobilniproekt.room.RecipeModel;

public class IngredientsHolder extends RecyclerView.ViewHolder {
    //private ImageView dotImage;
    private TextView ingredientTextView;
    private View parent;


    public IngredientsHolder(@NonNull View itemView) {
        super(itemView);

        this.ingredientTextView=itemView.findViewById(R.id.ingredientTextView);

    }

    public void bind(final String s)
    {

        ingredientTextView.setText(s);

    }

    public TextView getIngredientTextView() {
        return ingredientTextView;
    }

    public void setIngredientTextView(TextView ingredientTextView) {
        this.ingredientTextView = ingredientTextView;
    }

    public View getParent() {
        return parent;
    }

    public void setParent(View parent) {
        this.parent = parent;
    }
}

