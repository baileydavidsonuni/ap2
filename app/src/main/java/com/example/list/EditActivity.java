package com.example.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class EditActivity extends Activity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private ArrayList<String> items;
    private ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initDatePicker();
        items = new ArrayList<String>();
        dateButton = findViewById(R.id.datePickerButton);
        read();
    }


    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + "/" + day + "/" + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }


    private void read() {
        File filesDir = getFilesDir();
        String title = getIntent().getStringExtra("title");
        File editToDoFile = new File(filesDir, title);
        try {
            items = new ArrayList<String>(FileUtils.readLines(editToDoFile));
            EditText titleText = (EditText) findViewById(R.id.toDoTitle);
            titleText.setText(items.get(0));
            dateButton.setText(items.get(1));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }


    private void write() {
        File filesDir = getFilesDir();
        EditText toDoTitle = (EditText) findViewById(R.id.toDoTitle);
        String itemText = toDoTitle.getText().toString();
        String dbDate = dateButton.getText().toString();
        File toDoFile = new File(filesDir, itemText);
        try {
            FileUtils.writeLines(toDoFile, Arrays.asList(itemText, dbDate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEditItem(View v) {

        write();
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

}


