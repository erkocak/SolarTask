package com.example.solartask;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.solartask.db.Database;
import com.example.solartask.db.Task;
import com.example.solartask.db.TaskDao;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.solartask.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PdfGeneratorMainActivity extends AppCompatActivity {

    // Variables for our buttons.
    Button generatePDFbtn;

    // Declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    String s = "";
    Database db;
    TaskDao taskDao;

    // Constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_generator_main);

        db = Database.getInstance(getApplicationContext());
        taskDao = db.taskDao();

        // Initializing our variables.
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);

        // Checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        //Setting up the button
        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePDF() {
        //Setting up the PDF document
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        //Setting up the title
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        title.setTextSize(15);

        title.setColor(ContextCompat.getColor(this, R.color.black));

        canvas.drawText("My Agenda", 300, 100, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);

        title.setTextAlign(Paint.Align.CENTER);

        //Setting up the text
        int x = 400;
        int y = 400;
        for (Task t: taskDao.getAllTasks()){
            canvas.drawText(t.toString(), x, y, title);
            y = y + 100;
        }

        pdfDocument.finishPage(myPage);

        //Setting up the file path and the file
        File file = new File(getExternalFilesDir(null), "MyAgenda.pdf");

        System.out.println("THIS CODE RAN");

        //Outputting the PDF document
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(PdfGeneratorMainActivity.this, "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    /**
     * Checks the storage permission
     * @return storage permission boolean statement
     */
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests storage permission
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
