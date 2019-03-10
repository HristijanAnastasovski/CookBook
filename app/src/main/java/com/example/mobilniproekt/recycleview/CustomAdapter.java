package com.example.mobilniproekt.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilniproekt.R;
import com.example.mobilniproekt.model.Recipe;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Recipe> dataList;
    private Context context;

    public CustomAdapter(Context context,List<Recipe> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        private TextView txtTitle;
        private TextView txtPublisher;
        private TextView txtURL;
        private TextView txtSocialRank;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            txtPublisher=mView.findViewById(R.id.textViewPublisher);
            txtURL=mView.findViewById(R.id.textViewRecipeURL);
            txtSocialRank=mView.findViewById(R.id.textViewSocialRank);
            coverImage = mView.findViewById(R.id.imageViewPoster);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_result_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getTitle());
        holder.txtPublisher.setText(dataList.get(position).getPublisher());
        holder.txtURL.setText(dataList.get(position).getSource_url());
        holder.txtSocialRank.setText(Float.toString(dataList.get(position).getSocial_rank()));

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getImage_url())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}