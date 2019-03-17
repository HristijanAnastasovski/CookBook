package com.example.mobilniproekt.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobilniproekt.R;
import com.example.mobilniproekt.RecipeDetailsOfflineActivity;
import com.example.mobilniproekt.room.RecipeModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{

    private List<RecipeModel> list;
    private Context context;

    public FavoritesAdapter(List<RecipeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView title;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView) itemView.findViewById(R.id.favoritesCircleImage);
            title=(TextView) itemView.findViewById(R.id.favoritesTitle);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.favorites_layout);
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
                .into(viewHolder.circleImageView);

        viewHolder.title.setText(list.get(i).getTitle());

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clicking on the item
                Intent intent=new Intent(context, RecipeDetailsOfflineActivity.class);
                intent.putExtra("Recipe ID", list.get(i).getRecipe_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
