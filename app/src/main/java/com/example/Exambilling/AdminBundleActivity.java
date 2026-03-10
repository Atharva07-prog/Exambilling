package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminBundleActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText bundleNameEditText, paperIdEditText,subjectIdEditText,courseIdEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bundle);

        databaseHelper = new DatabaseHelper(this);
        bundleNameEditText = findViewById(R.id.bundleNameEditText);
        paperIdEditText = findViewById(R.id.paperIdEditText);

        ImageButton addBundleButton = findViewById(R.id.addBundleButton);
        addBundleButton.setOnClickListener(v -> {
            String bundleName = bundleNameEditText.getText().toString().trim();
            int subjectId = Integer.parseInt(subjectIdEditText.getText().toString()); // Get subject ID
            int courseId = Integer.parseInt(courseIdEditText.getText().toString());   // Get course ID
            int paperId = Integer.parseInt(paperIdEditText.getText().toString());     // Get paper ID

            long result = databaseHelper.addBundle(bundleName, subjectId, courseId, paperId);

            if (result != -1) {
                Toast.makeText(AdminBundleActivity.this, "Bundle added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AdminBundleActivity.this, "Failed to add bundle", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
