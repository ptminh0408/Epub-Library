package com.example.epub.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epub.R;
import com.example.epub.ReadBook.Reader;
import com.example.epub.ReadBook.User;

import java.io.File;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private Context mContext;
    private List<User> mListUser;

    public UserAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public UserAdapter() {
    }

    public  void setData(List<User> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mListUser.get(position);
        if (user == null){
            return;
        }
        holder.imgUser.setImageBitmap(user.getResouceImage());
        holder.tvName.setText(user.getName());
        holder.bookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickReadBook(user);
            }
        });
        holder.bookCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Delete book")
                        .setMessage("Do you want delete this book?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(user.getBookDir());
                        boolean deleted = file.delete();
                        mListUser.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        Toast.makeText(mContext, "Delete success", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                return false;
            }
        });
    }
    private void onClickReadBook(User user) {
        Intent I = new Intent(mContext, Reader.class);
        I.putExtra("epub_location", user.getBookDir());
        mContext.startActivity(I);
    }

    @Override
    public int getItemCount() {
        if (mListUser != null){
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgUser;
        private TextView tvName;
        private CardView bookCard;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.img_user);
            tvName = itemView.findViewById(R.id.tv_name);
            bookCard = itemView.findViewById(R.id.bookCard);


        }
    }

}
