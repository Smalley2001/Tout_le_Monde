package com.codepath.tout_le_monde;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.transition.Hold;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAp extends SliderViewAdapter<SliderAp.Holder> {

    int [] images;

    public SliderAp(int [] images) {
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent,
                false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAp.Holder viewHolder, int position) {

        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
