package edu.sdsu.cs.assignment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;


public class DateActivity extends ActionBarActivity implements View.OnClickListener{

    private DatePicker datePicker;
    private Button setDateButton;
    private String dayKey = "day";
    private String monthKey = "month";
    private String yearKey = "year";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = getSharedPreferences("edu.sdsu.cs.assignment2", Context.MODE_PRIVATE);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        int day = prefs.getInt(dayKey, datePicker.getDayOfMonth());
        int month = prefs.getInt(monthKey, datePicker.getMonth());
        int year = prefs.getInt(yearKey, datePicker.getYear());

        datePicker.updateDate(year, month, day);

        setDateButton = (Button) findViewById(R.id.buttonSetDate);
        setDateButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int day = datePicker.getDayOfMonth();
        final int month = datePicker.getMonth();
        final int year =  datePicker.getYear();
        final String date = String.format("%02d", month + 1) + "/" + day + "/" + year;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences prefs = getSharedPreferences("edu.sdsu.cs.assignment2", Context.MODE_PRIVATE);
                prefs.edit().putInt(dayKey, day).apply();
                prefs.edit().putInt(monthKey, month).apply();
                prefs.edit().putInt(yearKey, year).apply();

                Intent data = getIntent();
                data.putExtra("Date", date);
                setResult(RESULT_OK, data);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setResult(RESULT_CANCELED);
            }
        });

        builder.setMessage("Are you sure you want to set date as " + date);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
