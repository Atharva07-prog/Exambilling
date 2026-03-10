package com.example.Exambilling;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminSubjectActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText subjectNameEditText, courseIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subject);

        databaseHelper = new DatabaseHelper(this);
        subjectNameEditText = findViewById(R.id.subjectNameEditText);
        courseIdEditText = findViewById(R.id.courseIdEditText);

        Button addSubjectButton = findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(v -> {
            String subjectName = subjectNameEditText.getText().toString();
            int courseId = Integer.parseInt(courseIdEditText.getText().toString());
            long result = databaseHelper.addSubject(subjectName, courseId);

            if (result != -1) {
                Toast.makeText(AdminSubjectActivity.this, "Subject added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AdminSubjectActivity.this, "Failed to add subject", Toast.LENGTH_SHORT).show();
            }
        });
    }
}