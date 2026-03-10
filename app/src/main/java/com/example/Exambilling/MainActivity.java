package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout contentContainer;
    private DatabaseHelper databaseHelper;

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        contentContainer = findViewById(R.id.contentContainer);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        findViewById(R.id.assignedBundlesRecyclerView);

        boolean isAdmin ;
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        settingsIcon.setOnClickListener(v -> {
            // Admin settings
        });
        // Load Initial Content (Admin or User)
        if (isAdmin) {
            loadAdminContent();
        } else {
            loadUserContent();
        }

        // Bottom Navigation Handling
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                if (isAdmin) {
                    loadAdminContent();
                } else {
                    loadUserContent();
                }
                return true;
            } else if (itemId == R.id.navigation_chat) {
                startChatActivity();
                return true;
            } else if (itemId == R.id.navigation_list) {
                loadPaymentContent();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                loadProfileContent();
                return true;
            }

            return false;
        });

    }


    @SuppressLint({"WrongViewCast", "MissingInflatedId", "SetTextI18n"})
    private void loadAdminContent() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View adminView = inflater.inflate(R.layout.activity_admin, contentContainer, false);
        contentContainer.removeAllViews();
        contentContainer.addView(adminView);

        // Admin UI elements
        ImageButton addSubjectButton = adminView.findViewById(R.id.addSubjectButton);
        ImageButton addCourseButton = adminView.findViewById(R.id.addCourseButton);
        ImageButton addPaperButton = adminView.findViewById(R.id.addPaperButton);
        ImageButton addBundleButton = adminView.findViewById(R.id.addBundleButton);
        ImageButton manageUsersButton = adminView.findViewById(R.id.manageUsersButton);
        ImageButton manageTeachersButton = adminView.findViewById(R.id.manageTeachersButton);
        ImageButton generateNotificationButton = adminView.findViewById(R.id.generateNotificationButton);
        ImageButton generatePaymentNotificationButton = adminView.findViewById(R.id.generatePaymentNotificationButton);
        ImageButton assignExaminerButton = adminView.findViewById(R.id.assignExaminerButton);

        TextView bundleProgressPercentage = adminView.findViewById(R.id.bundleProgressPercentage);
        TextView examinerProgressPercentage = adminView.findViewById(R.id.examinerProgressPercentage);
        EditText subjectNameEditText = adminView.findViewById(R.id.subjectNameEditText);
        EditText courseNameEditText = adminView.findViewById(R.id.courseNameEditText);
        EditText paperNameEditText = adminView.findViewById(R.id.paperNameEditText);
        EditText bundleNameEditText = adminView.findViewById(R.id.bundleNameEditText);
        EditText notificationEditText = adminView.findViewById(R.id.notificationEditText);
        EditText paymentNotificationEditText = adminView.findViewById(R.id.paymentNotificationEditText);
        EditText examinerEditText = adminView.findViewById(R.id.examinerEditText);


        addSubjectButton.setOnClickListener(v -> {
            String subjectName = subjectNameEditText.getText().toString();
            long result = databaseHelper.addSubject(subjectName, 1);
            showToast(result != -1, "subject added", "Failed to add subject");
        });
        addCourseButton.setOnClickListener(v -> {
            String courseName = courseNameEditText.getText().toString();
            long result = databaseHelper.addCourse(courseName, 1);
            showToast(result != -1, "Course added", "Failed to add course");
        });


        addPaperButton.setOnClickListener(v -> {
            String paperName = paperNameEditText.getText().toString();
            long result = databaseHelper.addPaper(String.valueOf(paperName),3,5);
            showToast(result != -1, "Paper added", "Failed to add paper");
        });

        addBundleButton.setOnClickListener(v -> {
            String bundleName = bundleNameEditText.getText().toString().trim(); // Get bundle name
            long result = databaseHelper.addBundle(String.valueOf(bundleName), 3, 5, 2);
            showToast(result != -1, "Bundle added", "Failed to add Bundle");
        });



        manageUsersButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ManageUsersActivity.class)));

        manageTeachersButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ManageTeachersActivity.class)));

        generateNotificationButton.setOnClickListener(v -> {
            String notificationMessage = notificationEditText.getText().toString();
            boolean result = databaseHelper.generateNotification(notificationMessage);
            showToast(result, "Notification generated", "Failed to generate notification");
        });

        generatePaymentNotificationButton.setOnClickListener(v -> {
            String paymentMessage = paymentNotificationEditText.getText().toString();
            boolean result = databaseHelper.generatePaymentNotification(paymentMessage);
            showToast(result, "Payment notification generated", "Failed to generate payment notification");
        });

        assignExaminerButton.setOnClickListener(v -> {
            String examinerName = examinerEditText.getText().toString().trim();
            String paperName = paperNameEditText.getText().toString().trim();
            String bundleName = bundleNameEditText.getText().toString().trim();

            boolean result = databaseHelper.assignExaminer(examinerName, paperName, bundleName);
            showToast(result, "Examiner assigned successfully", "Failed to assign examiner");
        });




        bundleProgressPercentage.setText("Bundle: 80%");
        examinerProgressPercentage.setText("Examiner: 60%");
    }

    private void showToast(boolean success, String successMsg, String errorMsg) {
        Toast.makeText(this, success ? successMsg : errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void loadUserContent() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View adminView = inflater.inflate(R.layout.activity_user, contentContainer, false);
        contentContainer.removeAllViews();
        contentContainer.addView(adminView);
        RecyclerView assignedBundlesRecyclerView = findViewById(R.id.assignedBundlesRecyclerView);  // Use RecyclerView, not View
        List<AssignedBundle> assignedBundles = getAssignedBundlesFromDatabase();
        AssignedBundleAdapter adapter = new AssignedBundleAdapter(assignedBundles);

        assignedBundlesRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Required for RecyclerView
        assignedBundlesRecyclerView.setAdapter(adapter); // ✅ Correct way to set adapter

    }


    private List<AssignedBundle> getAssignedBundlesFromDatabase() {
        List<AssignedBundle> assignedBundles = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT bundleID, bundleName, paperID, paperName FROM examiner"; // ✅ Fetching from examiner table
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int bundleId = cursor.getInt(0);
                String bundleName = cursor.getString(1);
                int paperId = cursor.getInt(2);
                String paperName = cursor.getString(3);

                AssignedBundle bundle = new AssignedBundle(bundleId, bundleName, paperId, paperName);
                assignedBundles.add(bundle);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return assignedBundles;
    }

    private void startChatActivity() {
        startActivity(new Intent(this, ChatActivity.class));
    }
    @SuppressLint("SetTextI18n")
    private void loadPaymentContent() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View paymentView = inflater.inflate(R.layout.content_payment, contentContainer, false);
        contentContainer.removeAllViews();
        contentContainer.addView(paymentView);

        TextView paymentDetails = paymentView.findViewById(R.id.paymentDetails);
        paymentDetails.setText("Payment Details: ... (fetch from database)");
    }

    @SuppressLint("SetTextI18n")
    private void loadProfileContent() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View profileView = inflater.inflate(R.layout.activity_profile, contentContainer, false);
        contentContainer.removeAllViews();
        contentContainer.addView(profileView);

        TextView profileName = profileView.findViewById(R.id.profileName);
        profileName.setText("User Name: ... (fetch from database)");
    }

}

