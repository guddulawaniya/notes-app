package com.example.secure_notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.viewholder> {

    List<notes> list;

    Context context;

    public myAdapter(List<notes> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        notes module = list.get(position);
        holder.title.setText(module.getText());
        holder.descripation.setText(module.getDescri());
        holder.date.setText(module.getDate());
        holder.time.setText(module.getTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Add_notes.class);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.itemView, "fab").toBundle();
                intent.putExtra("id",module.getId());
                intent.putExtra("title",module.getText());
                intent.putExtra("ds",module.getDescri());
                intent.putExtra("date",module.getDate());
                intent.putExtra("time",module.getTime());
                context.startActivity(intent,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView title,descripation,date,time;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titletext);
            descripation = itemView.findViewById(R.id.descripation);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);

        }
    }

}
