package com.codepath.tout_le_monde;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>  {

    private Context context;
    private List<Event> events;
    //Set to default position < 0 to call in onbindViewHolder method
    //This is used to apply animations to viewholders when onbindViewHolder method is called
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
            holder.bind(event);
            last_position = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
            tvCampaign = itemView.findViewById(R.id.tvCampaign);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvHost = itemView.findViewById(R.id.tvHost);
            tvDescriptionTitle = itemView.findViewById(R.id.tvDescriptionTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Event event) {
            // Bind the post data to the view elements
            tvEventName.setText(event.getName());
            tvDate.setText(event.getDate());
            tvStart.setText(event.getStartTime());
            tvEnd.setText(event.getEndTime());
            tvCampaign.setText(event.getCampaign());
            tvEventLocation.setText(event.getLocation());
            tvHost.setText(event.getHost().getUsername());
            tvDescriptionTitle.setText(R.string.Description);
            tvDescription.setText(event.getDescription());
//            ParseFile image = post.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }
        }
    }
}
