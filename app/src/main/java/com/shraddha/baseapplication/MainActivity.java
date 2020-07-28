package com.shraddha.baseapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editRollno, editName, editMarks;
    Button btnAdd, btnDelete, btnModify, btnView, btnViewAll, btnShowInfo;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editRollno = findViewById(R.id.editRollno);
        editName = findViewById(R.id.editName);
        editMarks = findViewById(R.id.editMarks);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnModify = findViewById(R.id.btnModify);
        btnView = findViewById(R.id.btnView);
        btnViewAll = findViewById(R.id.btnViewAll);
        btnShowInfo = findViewById(R.id.btnShowInfo);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                if (editRollno.getText().toString().trim().length() == 0 ||
                        editName.getText().toString().trim().length() == 0 ||
                        editMarks.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    showMessage("Error", "Please enter all values");
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + editRollno.getText() + "','" + editName.getText() +
                        "','" + editMarks.getText() + "');");
                showMessage("Success", "Record added");
                clearText();
                break;
            case R.id.btnDelete:
                if (editRollno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE rollno='"+editRollno.getText()+"'");
                    showMessage("Success", "Record Deleted");
                }
                break;
            case R.id.btnModify:
                if (editRollno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + editName.getText() + "',marks='" + editMarks.getText() +
                            "' WHERE rollno='" + editRollno.getText() + "'");
                    showMessage("Success", "Record Modified");
                }
                break;
            case R.id.btnView:
                if (editRollno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter Rollno");
                    return;
                }
                cursor = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollno.getText() + "'", null);
                if (cursor.moveToFirst()) {
                    editName.setText(cursor.getString(1));
                    editMarks.setText(cursor.getString(2));
                }
                else
                {
                    showMessage("Error", "Invalid Rollno");
                    clearText();
                }
                break;
            case R.id.btnViewAll:
                cursor = db.rawQuery("SELECT * FROM student ", null);
                if (cursor.getCount()==0)
                {
                    showMessage("Error","No Records Found");
                    return;
                }
                else
                {
                    StringBuffer buffer = new StringBuffer();
                    while (cursor.moveToNext())
                    {
                        buffer.append("Rollno : "+cursor.getString(0)+"\n");
                        buffer.append("Name   : "+cursor.getString(1)+"\n");
                        buffer.append("Marks  : "+cursor.getString(2)+"\n");
                    }
                    showMessage("All Recods !!",buffer.toString());
                }
                break;
            case R.id.btnShowInfo:
                showMessage("Student Record Application", "Developed By Chandan Prasad");
                break;
        }

    }

    private void clearText() {
        editRollno.setText("");
        editName.setText("");
        editMarks.setText("");
        editRollno.requestFocus();
    }

    private void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(true);
        builder.show();
    }
}
