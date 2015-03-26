package edu.sdsu.cs.assignment2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


public class KeyboardActivity extends ActionBarActivity implements View.OnClickListener {

    private Button backButton;
    private Button hideButton;
    private EditText topEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String passedData = getIntent().getExtras().getString(MainActivity.TEXT_FIELD_CONTENT);

        topEditText = (EditText) findViewById(R.id.editTextTop);
        topEditText.setText(passedData);

        backButton = (Button) findViewById(R.id.keyboardButtonBack);
        backButton.setOnClickListener(this);
        hideButton = (Button) findViewById(R.id.buttonHide);
        hideButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_keyboard, menu);
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
        if (v.getId() == backButton.getId()) {
            finish();
        } else { // hide button
            InputMethodManager manager;
            manager =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            if (manager!=null) {

                if (this == null)
                    return;
                if (this.getCurrentFocus() == null)
                    return;
                if (this.getCurrentFocus().getWindowToken() == null)
                    return;
                manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }

        }
    }
}
