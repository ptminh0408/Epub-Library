package com.example.epub.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epub.ReadBook.BookModel;
import com.example.epub.R;
import com.example.epub.Display.Download;
import com.squareup.picasso.Picasso;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {

    private List<BookModel> categories, categoriesList;
    private Context context;

    public CategoryAdapter(List<BookModel> categories, Context context) {
        this.categories = categories;
        this.categoriesList = categories;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null | constraint.length() == 0) {
                    filterResults.count = 0;
                }
                else{
                    String search = constraint.toString().toLowerCase();
                    List<BookModel> result = new ArrayList<>();
                    for (BookModel item : categoriesList) {
                        if (item.getBookTitle().toLowerCase().contains(search)) {
                            result.add(item);
                        }
                    }
                    filterResults.count = result.size();
                    filterResults.values = result;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //categories.clear();
                categories = (List<BookModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nTitle;
        ImageView nImage;
        TextView nGenre;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nTitle = itemView.findViewById(R.id.title_book_2);
            nImage = itemView.findViewById(R.id.img_category);
            nGenre = itemView.findViewById(R.id.genre);
            cardView = itemView.findViewById(R.id.verticalList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel category = categories.get(position);

        holder.nTitle.setText(category.getBookTitle());
        Picasso.get().load(category.getBookCover()).into(holder.nImage);
        holder.nGenre.setText(category.getBookGenre());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, Download.class);
                it.putExtra("BOOK", category);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }
}