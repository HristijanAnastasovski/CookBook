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

public class CardItemViewHolder extends RecyclerView.ViewHolder{
    private ImageView imageView;
    private TextView  titleTextView;
    private View parent;
    private Button AddToList;

    public CardItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView=itemView.findViewById(R.id.image);
        this.titleTextView=itemView.findViewById(R.id.title);
        //this.AddToList=itemView.findViewById(R.id.buttonAddToList);
    }

    public void bind(final Recipe entity, RecipeDatabase database, int size)
    {
        Glide.with(itemView.getContext())
                .load(entity.getImage_url())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(imageView.getContext())
                        .load(R.drawable.ic_launcher_background))
                .apply(new RequestOptions().override(600, 300))
                .into(imageView);
    /*
        Picasso.with(itemView.getContext())
                .load(entity.getImage_url())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
                */
        titleTextView.setText(entity.getTitle().replaceAll("&nbsp;"," ").replaceAll("&amp;","&"));

        // ova da bide vo RecipeDetailsActivity
      /*  AddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RecipeModel recipe=new RecipeModel(entity.getRecipe_id(), entity.getPublisher(),entity.getF2f_url(), entity.getTitle(), entity.getSource_url(), entity.getImage_url(), entity.getSocial_rank(), entity.getPublisher_url());
                        database.daoAccess () . insertOnlySingleMovie (recipe);
                    }
                }) .start();

            }
        });*/
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public View getParent() {
        return parent;
    }

    public void setParent(View parent) {
        this.parent = parent;
    }
}
