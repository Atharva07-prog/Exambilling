package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ManageTeachersActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView teachersListView;
    private ArrayList<Integer> teacherIds; // To store teacher IDs for deletion

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers); // Make sure this XML exists

        databaseHelper = new DatabaseHelper(this);
        teachersListView = findViewById(R.id.teachersListView);

        loadTeachers(); // Load teachers into the ListView

        // Set up click listener to delete a teacher on long press
        teachersListView.setOnItemLongClickListener((parent, view, position, id) -> {
            int teacherId = teacherIds.get(position); // Get the ID of the teacher
            boolean deleted = databaseHelper.deleteTeacher(teacherId);

            if (deleted) {
                Toast.makeText(this, "Teacher deleted", Toast.LENGTH_SHORT).show();
                loadTeachers(); // Refresh list after deletion
            } else {
                Toast.makeText(this, "Failed to delete teacher", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Load teachers from database
    private void loadTeachers() {
        ArrayList<String> teachersList = new ArrayList<>();
        teacherIds = new ArrayList<>();
        Cursor cursor; // Fetch teachers from DB
        cursor = databaseHelper.getTeachers();

        if (cursor.getCount() == 0) {
            teachersList.add("No teachers available.");
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                teacherIds.add(id);
                teachersList.add(name);
            }
        }
        cursor.close();

        // Set up the ListView adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teachersList);
        teachersListView.setAdapter(adapter);
    }
}
