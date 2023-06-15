package com.example.tekscore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<Announcement> announcements;

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the ViewHolder
        Announcement announcement = announcements.get(position);
        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the clicked announcement
                    Announcement clickedAnnouncement = announcements.get(getAdapterPosition());

                    // Show the modal with the announcement details
                    // You can implement your own logic here
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    String emoji = clickedAnnouncement.getContent() == "info"? "ℹ️" :clickedAnnouncement.getContent() == "important"? "❗" : "⚠️";
                    builder.setTitle(emoji+ " "+clickedAnnouncement.getTitle() + " " + emoji);
                    Log.d("TAG", "onClick: " + clickedAnnouncement.getContent());
                    builder.setMessage(clickedAnnouncement.getContent());
                    builder.setPositiveButton("OK", null);
                    builder.show();
                    //showAnnouncementModal(clickedAnnouncement);
                }
            });
        }

        public void bind(Announcement announcement) {
            // Set the data to the views
            titleTextView.setText(announcement.getEmoji()+" "+announcement.getTitle());
            dateTextView.setText(announcement.getDate());
        }
    }
}
