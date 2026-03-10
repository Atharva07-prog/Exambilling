package com.example.Exambilling;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminCourseActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText courseNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course); // Make sure the correct layout file is used

        databaseHelper = new DatabaseHelper(this);
        courseNameEditText = findViewById(R.id.courseNameEditText); // Ensure this ID matches in XML

        Button addCourseButton = findViewById(R.id.addCourseButton); // Ensure this ID matches in XML

        // ✅ Using a Lambda Expression for the Click Listener
        addCourseButton.setOnClickListener(v -> {
            String courseName = courseNameEditText.getText().toString().trim();

            if (courseName.isEmpty()) {
                Toast.makeText(AdminCourseActivity.this, "Please enter a course name", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = databaseHelper.addCourse(courseName, 1); // Assuming adminId is 1

            if (result != -1) {
                Toast.makeText(AdminCourseActivity.this, "Course added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after adding
            } else {
                Toast.makeText(AdminCourseActivity.this, "Failed to add course", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
