package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.contacts.MainActivity.handler;

public class ContactAdapter
        extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    public Context context;
    public List<Contact> list;

    public ContactAdapter(Context context, List<Contact> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Contact contact = list.get(position);
        holder.textPhone.setText(contact.phone);
        holder.textName.setText(contact.toString());
        holder.imageView.setImageURI(contact.photo);
        holder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra(EditActivity.SURNAME, contact.surname);
                intent.putExtra(EditActivity.NAME, contact.name);
                intent.putExtra(EditActivity.PATRONYMYC, contact.patronimyc);
                intent.putExtra(EditActivity.PHONE, contact.phone);
                intent.putExtra(EditActivity.EMAIL, contact.email);
                intent.putExtra(EditActivity.POSITION, position);

                if (contact.photo != null){
                    intent.putExtra(EditActivity.PHOTO, contact.photo.toString());
                }
                ((MainActivity) context).startActivityForResult(intent, EditActivity.EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onItemDismiss(int position) {
        final Contact contact = list.get(position);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                App.app.db.contactDao().delete(contact);
                handler.sendEmptyMessage(0);
            }
        });
        thread.start();
        list.remove(position);
//        ((MainActivity) context).adapter.notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textName, textPhone;
        LinearLayout line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textPhone = itemView.findViewById(R.id.textPhone);
            line = itemView.findViewById(R.id.line);
        }
    }
}
