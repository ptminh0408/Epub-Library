package com.example.epub.Display;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.epub.Adapter.UserAdapter;
import com.example.epub.R;
import com.example.epub.ReadBook.User;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class Library extends SideBar {


    private RecyclerView rcvUser;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_library, null, false);
        mDrawerLayout.addView(v, 0);

        rcvUser = findViewById(R.id.rcv_user);
        mUserAdapter = new UserAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcvUser.setLayoutManager(gridLayoutManager);

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.LayoutHeader)));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            List<User> fileList = new ArrayList<>();
            fileList = getListUser();
            mUserAdapter.setData(fileList);
        } catch (Exception e) {
             Log.v("Err", e.getMessage());
        }
        rcvUser.setAdapter(mUserAdapter);



    }

    private List<User> getListUser() throws Exception {
        List<User> list = new ArrayList<>();
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Search_Dir(dir, list);

        return list;
    }

    public void Search_Dir(File dir, List<User> list) throws Exception {
        InputStream inputStream = null;
        String epubExt = ".epub";
        File fileList[] = dir.listFiles();
        if (fileList != null) {
            for(int i = 0; i < fileList.length; i++) {
                if (fileList[i].getName().endsWith(epubExt)) {
                    inputStream = new BufferedInputStream(new FileInputStream(fileList[i].getPath()));
                    Book mybook = new EpubReader().readEpub(inputStream);
                    Bitmap cover = BitmapFactory.decodeStream(mybook.getCoverImage().getInputStream());
                    list.add(new User(cover, mybook.getTitle(), fileList[i].getPath()));
                }
            }
        }
    }

}