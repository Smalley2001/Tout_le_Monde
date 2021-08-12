package com.codepath.tout_le_monde.unused;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.tout_le_monde.R;

import java.util.ArrayList;

public class CampaignGridActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<String> campaigns = new ArrayList<>();
//    String [] campaigns = {"Black Lives Matter", "LGBTQ+", "Stop Asian Hate", "MeToo", };
    EditText editText;
    Button button;

    ArrayList<Integer> campaign_images = new ArrayList<>();
//    int [] campaign_images = {R.drawable.blm, R.drawable.lgbtq, R.drawable.placeholder_image,
//    R.drawable.placeholder_image};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_grid);

        gridView = findViewById(R.id.grid_view);
        editText = findViewById(R.id.editText1);
        button = findViewById(R.id.button1);

        campaigns.add("Black Lives Matter");
        campaigns.add("LGBTQ+");
        campaigns.add("Stop Asian Hate");
        campaigns.add("MeToo");

        campaign_images.add(R.drawable.blm);
        campaign_images.add(R.drawable.lgbtq);
        campaign_images.add(R.drawable.placeholder_image);
        campaign_images.add(R.drawable.placeholder_image);


        GridAdapter adapter = new GridAdapter(CampaignGridActivity.this, campaigns, campaign_images);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You Clicked: " + campaigns.get(+position),
                        Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editText.getText().toString();
                campaigns.add(item);
                campaign_images.add(R.drawable.placeholder_image);
                adapter.notifyDataSetChanged();
            }
        });
    }
}