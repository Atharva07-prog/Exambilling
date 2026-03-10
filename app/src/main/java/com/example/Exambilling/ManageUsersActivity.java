package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView assignedBundlesRecyclerView;
    private DatabaseHelper databaseHelper; // Assuming you have a DatabaseHelper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        databaseHelper = new DatabaseHelper(this);
        assignedBundlesRecyclerView = findViewById(R.id.assignedBundlesRecyclerView);
        assignedBundlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAssignedBundles();
    }

    private void loadAssignedBundles() {
        List<AssignedBundle> AssignedBundles = databaseHelper.getAssignedBundles(); // ✅ Fetching List<AssignedBundle>

        if (assignedBundlesRecyclerView != null) {
            AssignedBundleAdapter adapter = new AssignedBundleAdapter(AssignedBundles); // ✅ Now matches expected type
            assignedBundlesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            assignedBundlesRecyclerView.setAdapter(adapter);
        } else {
            Log.e("ManageUserActivity", "RecyclerView is null. Check activity_manage_user.xml");
        }
    }

}

