package com.codepath.tout_le_monde;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CampaignRecyclerActivity extends AppCompatActivity {

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    public static final String TAG = "CampaignRecycler";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_recycler);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        loadItems();
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                String location = getIntent().getStringExtra("Location");
                double latitude = getIntent().getDoubleExtra("Latitude", 0);
                double longitude = getIntent().getDoubleExtra("Longitude", 0);
                String val = items.get(position);
                Intent i = new Intent(CampaignRecyclerActivity.this, CreateEventActivity.class);
                i.putExtra("Campaign", val);
                i.putExtra("MapsLocation", location);
                i.putExtra("MapsLatitude", latitude);
                i.putExtra("MapsLongitude", longitude);
                startActivity(i);

            }
        };

        final ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                // Add item to model
                items.add(todoItem);
                // Notify the adapter that an item has been inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // Reset the edit Text to empty
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });

    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {

        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("CampaignRecyclerAct", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {

        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("CampaignRecyclerAct", "Error writing items", e);
        }
    }

}