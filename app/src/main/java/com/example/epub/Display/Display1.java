package com.example.epub.Display;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.Adapter.CategoryAdapter;
import com.example.epub.Adapter.MainAdapter;
import com.example.epub.ReadBook.BookModel;
import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.epub.EpubReader;

public class Display1 extends SideBar {

    private List<BookModel> bookList;
    private SearchView searchView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ImageButton imageButton;

    ProgressDialog progressDialog;
    EditText bookAuthor;
//    EditText bookGenre;
//    EditText bookLanguage;
    Spinner bookLanguage, bookGenre;
    ImageView bookCover;
    TextView bookTitle;
    RecyclerView recyclerView1, recyclerView2,  recyclerView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display1);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_display1, null, false);
        mDrawerLayout.addView(v, 0);

        bookList = new ArrayList<>();
        List<BookModel> VNBook = new ArrayList<>();
        List<BookModel> ENBook = new ArrayList<>();
        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView3 = findViewById(R.id.recycler_view3);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView3.setLayoutManager(layoutManager3);

        MainAdapter mainAdapter1 = new MainAdapter(VNBook, this);
        recyclerView1.setAdapter(mainAdapter1);

        MainAdapter mainAdapter2 = new MainAdapter(ENBook, this);
        recyclerView2.setAdapter(mainAdapter2);

        CategoryAdapter categoryAdapter = new CategoryAdapter(bookList, this);
        recyclerView3.setAdapter(categoryAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookModel book = new BookModel();
                VNBook.clear();
                ENBook.clear();
                bookList.clear();
                for (DataSnapshot temp : snapshot.getChildren()) {
                    book = temp.getValue(BookModel.class);
                    bookList.add(book);
                    if (book.getBookLanguage().equals("Vietnamese"))
                        VNBook.add(book);
                    if (book.getBookLanguage().equals("English"))
                        ENBook.add(book);
                }
                mainAdapter1.notifyDataSetChanged();
                mainAdapter2.notifyDataSetChanged();
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        FloatingActionButton fab = findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile = new Intent();
//                chooseFile.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(chooseFile, 2);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent = Intent.createChooser(intent, "Choose Books");
                startActivityForResult(intent, 2);

            }
        });

    }

    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            try {
                openUpLoad(Gravity.CENTER, data.getData().getPath(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //Show popup UpLoad
    private void openUpLoad(int gravity, String bookDir, Uri uri) throws Exception {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_tesstaddd);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        bookCover = dialog.findViewById(R.id.imgCover);
        bookTitle = dialog.findViewById(R.id.txtTitleUp);
        bookAuthor = dialog.findViewById(R.id.edtAuthorUp);
        bookGenre = dialog.findViewById(R.id.spinner2);
        bookLanguage = dialog.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.language, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookLanguage.setAdapter(adapter1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.genre, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookGenre.setAdapter(adapter2);

        InputStream bookStream = new BufferedInputStream(new FileInputStream(bookDir));
        nl.siegmann.epublib.domain.Book epub = new EpubReader().readEpub(bookStream);
        bookTitle.setText(epub.getTitle());
        Bitmap coverImage = BitmapFactory.decodeStream(epub.getCoverImage()
                .getInputStream());
        bookCover.setImageBitmap(coverImage);
        Button btnUpload = dialog.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    uploadToFirebase(uri);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    //Upload to FireBase

    private void uploadToFirebase(Uri uri) throws Exception {
        progressDialog= new ProgressDialog(Display1.this);
        progressDialog.setTitle("Upload book");
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);

        BookModel book = new BookModel();
        String[] bookDir = uri.getPath().split("/");
        StorageReference imgRef = storageReference.child(bookDir[bookDir.length - 1] + ".jpg");
        InputStream inputStream = new BufferedInputStream(new FileInputStream(uri.getPath()));
        nl.siegmann.epublib.domain.Book temp = new EpubReader().readEpub(inputStream);
        imgRef.putBytes(temp.getCoverImage().getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookCover(uri.toString());
                        Log.w("success", "upload img success");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("fail", "load img fail");
            }
        });

        StorageReference fileRef = storageReference.child(bookDir[bookDir.length - 1]);
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        book.setBookAuthor(bookAuthor.getText().toString());
                        book.setBookTitle(bookTitle.getText().toString());
                        book.setBookGenre(bookGenre.getSelectedItem().toString());
                        book.setBookLanguage(bookLanguage.getSelectedItem().toString());
                        book.setBookURL(uri.toString());
                        String bookID = databaseReference.push().getKey();
                        databaseReference.child(bookID).setValue(book);
                        Toast.makeText(Display1.this, "Upload Successfully!", Toast.LENGTH_SHORT).show();
                        Log.w("success", "upload success!");
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress  = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       progressDialog.setProgress((int)progress);
                    }
                }).start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Display1.this, "Uploading faile!", Toast.LENGTH_SHORT).show();
                Log.w("fail", "upload failed!");
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search_action){
            Intent intent = new Intent(Display1.this, SearchDisplay.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}