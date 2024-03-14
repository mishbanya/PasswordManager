package com.example.passwordmanager.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;

import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordmanager.R;
import com.example.passwordmanager.passwords.Password;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private static OnViewButtonClickListener viewButtonClickListener;
    private static OnEditButtonClickListener editButtonClickListener;
    private static List<Password> dataList;

    public interface OnViewButtonClickListener {
        void onViewButtonClick(Password password);
    }

    public interface OnEditButtonClickListener {
        void onEditButtonClick(Password password);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.itempassword, parent, false);
        return new ViewHolder(view);
    }

    public RecyclerAdapter(Context context, List<Password> dataList) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Password data = dataList.get(position);
        holder.textViewHost.setText(data.getHost());
        String icon = data.getIconBase64();
        if ((icon != null ) && (!icon.isEmpty())){
            byte[] decodedString = Base64.decode(icon, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imageIcon.setImageBitmap(decodedBitmap);
        } else {
            holder.imageIcon.setImageResource(R.drawable.error);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<Password> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    public void setOnViewButtonClickListener(OnViewButtonClickListener listener) {
        viewButtonClickListener = listener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        editButtonClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHost;
        public TextView textViewPassword;
        ImageView imageIcon;
        Button buttonEdit;
        Button buttonView;

        ViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.imageIcon);
            textViewHost = itemView.findViewById(R.id.textViewHost);
            textViewPassword = itemView.findViewById(R.id.textViewPassword);
            buttonView = itemView.findViewById(R.id.buttonView);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);

            buttonView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && viewButtonClickListener != null) {
                    viewButtonClickListener.onViewButtonClick(dataList.get(position));
                }
            });

            buttonEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(dataList.get(position));
                }
            });
        }
    }
}