package com.codepath.tout_le_monde.unused;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.tout_le_monde.R;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> campaigns;
    private ArrayList<Integer> CampaignImages;

    public GridAdapter(Context context, ArrayList<String> campaigns, ArrayList<Integer> campaignImages) {
        this.context = context;
        this.campaigns = campaigns;
        CampaignImages = campaignImages;
    }

    @Override
    public int getCount() {
        return campaigns.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_campaign_image);
        TextView textView = convertView.findViewById(R.id.text_view);

        imageView.setImageResource(CampaignImages.get(position));
        textView.setText(campaigns.get(position));
        return convertView;
    }
}
