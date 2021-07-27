package com.codepath.tout_le_monde;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>  {

    private static final String TAG = "EventsAdapter";
    private Context context;
    private List<Event> events;
    // Set to default position < 0 to call in onbindViewHolder method
    // This is used to apply animations to viewholders when onbindViewHolder method is called
    int last_position = -1;

    public EventsAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        if(holder.getAdapterPosition() > last_position) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            ((ViewHolder) holder).itemView.startAnimation(animation);
            Event event = events.get(position);
            try {
                holder.bind(event);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            last_position = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvEventName;
        TextView tvDate;
        TextView tvStart;
        TextView tvEnd;
        TextView tvCampaign;
        TextView tvEventLocation;
        TextView tvHost;
        TextView tvDescriptionTitle;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStart = itemView.findViewById(R.id.tvStart);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvCampaign = itemView.findViewById(R.id.gotCampaign);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvHost = itemView.findViewById(R.id.tvHost);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener(this);
        }

        public void bind(Event event) throws ParseException {
            // Bind the post data to the view elements
            String campaign = "Campaign: " + event.getCampaign();
            String date = "Date: " + event.getDate();
            String description = "Description: " + event.getDescription();
            String location = "Location: " + event.getLocation();
            String host = "Host: " + event.getHostUsername();
            String start = "Start Time: " + event.getStartTime();
            String end = "End Time: " + event.getEndTime();

            tvEventName.setText(event.getName());
            tvDate.setText(date);
            tvStart.setText(start);
            tvEnd.setText(end);
            tvCampaign.setText(campaign);
            tvEventLocation.setText(location);
            tvHost.setText(host);
            tvDescriptionTitle.setText(R.string.Description);
            tvDescription.setText(description);
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            if(position != RecyclerView.NO_POSITION) {

                Event event = events.get(position);

                Log.i(TAG, event.getName());

                Log.d(TAG, context.getClass().toString());

                String host = event.getHost().getUsername();
                Log.i(TAG, "Host is : " + host);

                Log.i(TAG, context.getClass().toString());

                if (context.getClass().toString().equals("class com.codepath.tout_le_monde.EventTimelineActivity")) {
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("Context_Name", context.getClass().toString());
                    intent.putExtra("X", Parcels.wrap(event));
                    intent.putExtra("U", Parcels.wrap(event.getHost()));

                    context.startActivity(intent);
                } else if (context.getClass().toString().equals("class com.codepath.tout_le_monde.MyCreatedEventsActivity")) {
                    Intent intent = new Intent(context, CreatedEventDetailsActivity.class);
                    intent.putExtra("Context_Name", context.getClass().toString());
                    intent.putExtra("X", Parcels.wrap(event));
                    intent.putExtra("U", Parcels.wrap(event.getHost()));

                    context.startActivity(intent);
                } else if (context.getClass().toString().equals("class com.codepath.tout_le_monde.MySignedUpEventsActivity")) {
                    Intent intent = new Intent(context, SignedUpEventDetailsActivity.class);
                    intent.putExtra("Context_Name", context.getClass().toString());
                    intent.putExtra("X", Parcels.wrap(event));
                    intent.putExtra("U", Parcels.wrap(event.getHost()));
                    context.startActivity(intent);
                }


                else {

                    Intent intent = new Intent(context, EventDetailsActivity.class);
//                intent.putExtra("Host", host);
                    intent.putExtra("Context_Name", context.getClass().toString());
                    intent.putExtra("X", Parcels.wrap(event));
                    intent.putExtra("U", Parcels.wrap(event.getHost()));

                    context.startActivity(intent);
                }
            }
        }
    }
}
