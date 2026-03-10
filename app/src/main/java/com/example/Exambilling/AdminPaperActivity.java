package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPaperActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText paperNameEditText, courseIdEditText,subjectIdEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paper);

        databaseHelper = new DatabaseHelper(this);
        paperNameEditText = findViewById(R.id.paperNameEditText);
        courseIdEditText = findViewById(R.id.courseIdEditText);
        subjectIdEditText = findViewById(R.id.subjectIdEditText);
        ImageButton addPaperButton = findViewById(R.id.addPaperButton);

    }

    private void onClick(View v) {
        String papername = paperNameEditText.getText().toString(); // Get paper name as a string
        int subjectId = Integer.parseInt(subjectIdEditText.getText().toString()); // Get subject ID
        int courseId = Integer.parseInt(courseIdEditText.getText().toString()); // Get course ID

        long result = databaseHelper.addPaper(papername, subjectId, courseId); // Pass all values

        if (result != -1) {
            Toast.makeText(AdminPaperActivity.this, "Paper added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AdminPaperActivity.this, "Failed to add paper", Toast.LENGTH_SHORT).show();
        }
    }
}