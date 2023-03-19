package com.example.epub.Display;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.ReadBook.BookModel;
import com.example.epub.R;
import com.squareup.picasso.Picasso;

public class Download extends Activity {

    TextView bookAuthor, bookTitle, bookLanguage, bookGenre;
    ImageView bookCover;
    Button btnDown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_information);

        bookAuthor = findViewById(R.id.txtAuthor);
        bookTitle = findViewById(R.id.txtBookName);
        bookLanguage = findViewById(R.id.txtLanguage);
        bookGenre = findViewById(R.id.txtType);
        bookCover = findViewById(R.id.imageView);
        btnDown = findViewById(R.id.btnDownload);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Intent it = getIntent();
        BookModel book = (BookModel) it.getSerializableExtra("BOOK");
        Picasso.get().load(book.getBookCover()).into(bookCover);
        bookGenre.setText(book.getBookGenre());
        bookLanguage.setText(book.getBookLanguage());
        bookTitle.setText(book.getBookTitle());
        bookAuthor.setText(book.getBookAuthor());
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(book.getBookURL()));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download");
                request.setDescription("Downloading book.......");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, book.getBookTitle() + ".epub");
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(Download.this, "Downloading....", Toast.LENGTH_SHORT).show();
                Download.this.finish();
            }
        });
    }
}
