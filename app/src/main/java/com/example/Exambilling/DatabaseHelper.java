package com.example.Exambilling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "examination.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        try {
            // Create Login table
            db.execSQL("CREATE TABLE Login (loginID INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, username VARCHAR(255) UNIQUE, password VARCHAR(255), FOREIGN KEY (userID) REFERENCES User(userID));");
            Log.d(TAG, "Login table created.");

            // Create Admin table
            db.execSQL("CREATE TABLE Admin (adminID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), password VARCHAR(255));");
            Log.d(TAG, "Admin table created.");

            // Create User table
            db.execSQL("CREATE TABLE User (userID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), password VARCHAR(255), adminID INTEGER, FOREIGN KEY (adminID) REFERENCES Admin(adminID));");
            Log.d(TAG, "User table created.");

            // Create Examiner table
            db.execSQL("CREATE TABLE examiner (" +
                    "examinerID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "examinerName TEXT NOT NULL, " +
                    "paperID INTEGER NOT NULL, " +
                    "bundleID INTEGER NOT NULL, " +
                    "paperName TEXT NOT NULL, " +
                    "bundleName TEXT NOT NULL, " +
                    "FOREIGN KEY (paperID) REFERENCES paper(paperID), " +
                    "FOREIGN KEY (bundleID) REFERENCES bundle(bundleID))");
            Log.d(TAG, "Examiner table created.");

            // Create Teacher table
            db.execSQL("CREATE TABLE Teacher (teacherID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), adminID INTEGER, FOREIGN KEY (adminID) REFERENCES Admin(adminID));");
            Log.d(TAG, "Teacher table created.");

            // Create Course table
            db.execSQL("CREATE TABLE Course (courseID INTEGER PRIMARY KEY AUTOINCREMENT, adminID INTEGER, name VARCHAR(255), FOREIGN KEY (adminID) REFERENCES Admin(adminID));");
            Log.d(TAG, "Course table created.");

            // Create Subject table
            db.execSQL("CREATE TABLE Subject (subjectID INTEGER PRIMARY KEY AUTOINCREMENT, courseID INTEGER, subjectName VARCHAR(255), FOREIGN KEY (courseID) REFERENCES Course(courseID));");
            Log.d(TAG, "Subject table created.");

            // Create Paper table
            db.execSQL("CREATE TABLE Paper ("
                    + "paperID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "papername TEXT NOT NULL, "  // Added paperName column
                    + "subjectID INTEGER, "
                    + "courseID INTEGER, "
                    + "FOREIGN KEY (subjectID) REFERENCES Subject(subjectID), "
                    + "FOREIGN KEY (courseID) REFERENCES Course(courseID)"
                    + ");");

            Log.d(TAG, "Paper table created ");


            db.execSQL("CREATE TABLE Bundle ("
                    + "bundleID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "bundleName TEXT NOT NULL, " // Added bundleName column
                    + "subjectID INTEGER, "
                    + "courseID INTEGER, "
                    + "paperID INTEGER, "
                    + "FOREIGN KEY (subjectID) REFERENCES Subject(subjectID), "
                    + "FOREIGN KEY (courseID) REFERENCES Course(courseID), "
                    + "FOREIGN KEY (paperID) REFERENCES Paper(paperID)"
                    + ");");

            Log.d(TAG, "Bundle table created ");
            // Create Notification table
            db.execSQL("CREATE TABLE Notification (notificationID INTEGER PRIMARY KEY AUTOINCREMENT, userID INTEGER, message TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (userID) REFERENCES User(userID));");
            Log.d(TAG, "Notification table created.");

            db.execSQL("CREATE TABLE AssignedBundles ("
                    + "bundleID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "bundle_name TEXT, "
                    + "subject_name TEXT, "
                    + "course_name TEXT);");
            Log.d(TAG, "Notification table created.");

        } catch (Exception e) {
            Log.e(TAG, "Error creating tables: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: oldVersion=" + oldVersion + ", newVersion=" + newVersion);

        if (oldVersion < newVersion) {
            dropAllTables(db);
            createTables(db);
        }
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Login");
        db.execSQL("DROP TABLE IF EXISTS Admin");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Examiner");
        db.execSQL("DROP TABLE IF EXISTS Teacher");
        db.execSQL("DROP TABLE IF EXISTS Course");
        db.execSQL("DROP TABLE IF EXISTS Subject");
        db.execSQL("DROP TABLE IF EXISTS Paper");
        db.execSQL("DROP TABLE IF EXISTS AssignedBundles");
        db.execSQL("DROP TABLE IF EXISTS Bundle");
        db.execSQL("DROP TABLE IF EXISTS Notification");
        onCreate(db);
    }

    public long registerAdmin(String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("password", password);

        long result = db.insert("Admin", null, values);

        if (result == -1) {
            Log.e(TAG, "Error inserting admin!");
        } else {
            Log.d(TAG, "Admin registered successfully with ID: " + result);
        }

        return result;
    }

    // ✅ Register User (Fixed)
    public long registerUser(String username, String password, String email, int adminID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Insert into Users Table
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long userId = db.insert("Users", null, values);

        if (userId != -1) {
            // Insert into Teacher Table (Assigning adminID)
            ContentValues teacherValues = new ContentValues();
            teacherValues.put("name", username);  // Store the teacher's name
            teacherValues.put("adminID", adminID); // Assign the admin ID

            long teacherId = db.insert("Teacher", null, teacherValues);
            if (teacherId == -1) {
                Log.e("DB_ERROR", "Failed to insert into Teacher table");
            }
        } else {
            Log.e("DB_ERROR", "Failed to insert into Users table");
        }

        return userId;
    }


    // Adding Course
    public long addCourse(String name, int adminId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("adminID", adminId);
        return db.insert("Course", null, values);
    }

    // Adding Subject
    public long addSubject(String name, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subjectName", name);
        values.put("courseID", courseId);
        return db.insert("Subject", null, values);
    }
    // Add these methods in DatabaseHelper.java

    public long addPaper(String papername, int subjectId, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("papername", papername); // Store paper name as text
        values.put("subjectID", subjectId); // Store subject ID
        values.put("courseID", courseId); // Store course ID

        return db.insert("Paper", null, values);
    }


    public long addBundle(String bundleName, int subjectId, int courseId, int paperId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bundleName", bundleName);  // Correct column name
        values.put("subjectID", subjectId);    // Added subjectID
        values.put("courseID", courseId);      // Added courseID
        values.put("paperID", paperId);        // Correct column name

        return db.insert("Bundle", null, values);
    }



    public boolean generateNotification(String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", message);
        long result = db.insert("Notification", null, values);
        long result2 = db.insert("User", null, values);
        return result != -1;
    }

    public boolean generatePaymentNotification(String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", message);
        long result = db.insert("Notification", null, values);
        return result != -1;
    }


    // Assign Examiner
    public boolean assignExaminer(String examinerName, String paperName, String bundleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            db.beginTransaction(); // Start transaction

            // 1️⃣ Get paperID
            Cursor paperCursor = db.rawQuery("SELECT paperID FROM paper WHERE papername = ?", new String[]{paperName});
            int paperId = -1;
            if (paperCursor.moveToFirst()) {
                paperId = paperCursor.getInt(0);
            }
            paperCursor.close();

            // 2️⃣ Get bundleID
            Cursor bundleCursor = db.rawQuery("SELECT bundleID FROM bundle WHERE bundleName = ?", new String[]{bundleName});
            int bundleId = -1;
            if (bundleCursor.moveToFirst()) {
                bundleId = bundleCursor.getInt(0);
            }
            bundleCursor.close();

            // 3️⃣ Insert into examiner table
            if (paperId != -1 && bundleId != -1) {
                ContentValues values = new ContentValues();
                values.put("examinerName", examinerName);
                values.put("paperID", paperId);
                values.put("bundleID", bundleId);
                values.put("paperName", paperName);
                values.put("bundleName", bundleName);

                long result = db.insert("examiner", null, values);
                success = (result != -1);
            } else {
                Log.e("assignExaminer", "Error: Paper ID or Bundle ID not found.");
            }

            db.setTransactionSuccessful(); // Commit transaction
        } catch (Exception e) {
            Log.e("assignExaminer", "Error assigning examiner: " + e.getMessage());
        } finally {
            db.endTransaction(); // End transaction
        }

        return success;
    }



    // Change Password
    public boolean changePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int rowsAffected = db.update("User", values, "userID = ?", new String[]{String.valueOf(userId)});
        int adminRowsAffected = db.update("Admin", values, "adminID = ?", new String[]{String.valueOf(userId)});
        int loginRowsAffected = db.update("Login", values, "userID = ?", new String[]{String.valueOf(userId)});

        return rowsAffected > 0 || adminRowsAffected > 0 || loginRowsAffected > 0;
    }

    public List<AssignedBundle> getAssignedBundles() {
        List<AssignedBundle> assignedBundles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT bundleID, bundleName, paperID, paperName FROM examiner";
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

    // Get all teachers
    public Cursor getTeachers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Teacher", null);
    }

    // Delete a teacher by ID
    public boolean deleteTeacher(int teacherId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Teacher", "id = ?", new String[]{String.valueOf(teacherId)});
        db.close();
        return result > 0;
    }
    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Admin WHERE name = ? AND password = ?",
                new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }



}

