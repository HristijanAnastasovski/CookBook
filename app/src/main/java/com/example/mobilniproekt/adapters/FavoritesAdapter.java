package com.example.mobilniproekt.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobilniproekt.R;
import com.example.mobilniproekt.RecipeDetailsOfflineActivity;
import com.example.mobilniproekt.room.RecipeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{

    private List<RecipeModel> list;
    private Activity context;
    private int position;

    public FavoritesAdapter(List<RecipeModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView title;
        private CardView relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.favoritesImage);
            title=(TextView) itemView.findViewById(R.id.favoritesTitle);
            relativeLayout=itemView.findViewById(R.id.favorites_layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_favorites_list_item, viewGroup, false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context)
                .asBitmap()
                .load(list.get(i).getImage_url())
                .apply(new RequestOptions().override(600, 300))
                .into(viewHolder.imageView);

        viewHolder.title.setText(list.get(i).getTitle().replaceAll("&nbsp;"," ").replaceAll("&amp;","&"));

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clicking on the item
                Intent intent=new Intent(context, RecipeDetailsOfflineActivity.class);
                intent.putExtra("Recipe ID", list.get(i).getRecipe_id());
                intent.putExtra("position", String.valueOf(viewHolder.getAdapterPosition()));
                context.startActivityForResult(intent, 999);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}
