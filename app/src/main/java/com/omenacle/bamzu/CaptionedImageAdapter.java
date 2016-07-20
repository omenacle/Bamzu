package com.omenacle.bamzu;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by omegareloaded on 7/19/16.
 */
public class CaptionedImageAdapter extends RecyclerView.Adapter<CaptionedImageAdapter.ViewHolder> {

    int[] mCaptions;
    int[] mImages;

    public CaptionedImageAdapter(int[] mCaptions, int[] mImages) {
        this.mCaptions = mCaptions;
        this.mImages = mImages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;

        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }
    }

    @Override
    public CaptionedImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView mCardView;
        mCardView = (CardView) LayoutInflater.from(parent.getContext()).inflate
                (R.layout.card_caption_image, parent, false);
        return new ViewHolder(mCardView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        CardView cardView = holder.mCardView;
        ImageView imageView_display_image = (ImageView)cardView.findViewById(R.id.info_image);
        Drawable display_img = cardView.getResources().getDrawable(mImages[position]);
        //attaching views to data
        imageView_display_image.setImageDrawable(display_img);
        TextView textView_category = (TextView)cardView.findViewById(R.id.info_text);
        textView_category.setText(cardView.getResources().getString(mCaptions[position]));


    }

    @Override
    public int getItemCount() {
        return mCaptions.length;
    }
}
