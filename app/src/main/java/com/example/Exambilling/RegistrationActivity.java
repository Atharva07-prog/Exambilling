package com.example.Exambilling;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText nameEditText, passwordEditText;
    private RadioGroup userRoleRadioGroup;
    private static final String TAG = "RegistrationActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userRoleRadioGroup = findViewById(R.id.userRoleRadioGroup);
        Button registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            int selectedRoleId = userRoleRadioGroup.getCheckedRadioButtonId();

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedRoleId == -1) {
                Toast.makeText(RegistrationActivity.this, "Please select a user role", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = findViewById(selectedRoleId);
            String userRole = selectedRadioButton.getText().toString();

            boolean isRegistrationSuccessful = registerUser(name, password, userRole);

            if (isRegistrationSuccessful) {
                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish(); // Close activity and return to login
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean registerUser(String name, String password, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            db.beginTransaction();
            long userId = -1;
            long adminId = -1;
            String hashedPassword = hashPassword(password);

            if ("Admin".equalsIgnoreCase(role)) {
                // Register Admin
                values.put("name", name);
                values.put("password", hashedPassword);
                adminId = db.insert("Admin", null, values);

                if (adminId == -1) {
                    Log.e(TAG, "Admin insert failed: " + values);
                    throw new Exception("Failed to insert into Admin table");
                }

                Log.d(TAG, "Admin registered successfully with ID: " + adminId);
            } else if ("User".equalsIgnoreCase(role)) {
                // Get an available Admin ID (assuming at least one admin exists)
                Cursor cursor = db.rawQuery("SELECT adminID FROM Admin LIMIT 1", null);
                if (cursor.moveToFirst()) {
                    adminId = cursor.getLong(0);
                }
                cursor.close();

                if (adminId == -1) {
                    Log.e(TAG, "No Admin available for User registration");
                    throw new Exception("No Admin found to associate with User");
                }

                // Register User
                values.clear();
                values.put("name", name);
                values.put("password", hashedPassword);
                values.put("adminID", adminId);
                userId = db.insert("User", null, values);

                if (userId == -1) {
                    Log.e(TAG, "User insert failed: " + values);
                    throw new Exception("Failed to insert into User table");
                }

                Log.d(TAG, "User registered successfully with ID: " + userId);

                // Insert into Teacher table (Users are Teachers)
                ContentValues teacherValues = new ContentValues();
                teacherValues.put("name", name);
                teacherValues.put("adminID", adminId);
                long teacherId = db.insert("Teacher", null, teacherValues);

                if (teacherId == -1) {
                    Log.e(TAG, "Teacher insert failed: " + teacherValues);
                    throw new Exception("Failed to insert into Teacher table");
                }
                Log.d(TAG, "Teacher registered successfully with ID: " + teacherId);
            }

            // Insert into Login Table
            values.clear();
            values.put("userID", (role.equalsIgnoreCase("Admin")) ? adminId : userId);
            values.put("username", name);
            values.put("password", hashedPassword);
            long loginResult = db.insert("Login", null, values);

            if (loginResult == -1) {
                Log.e(TAG, "Login insert failed: " + values);
                throw new Exception("Failed to insert into Login table");
            }

            Log.d(TAG, "Login credentials stored successfully for: " + name);
            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            Log.e(TAG, "Error during registration", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error hashing password", e);
            return null;
        }
    }
}
