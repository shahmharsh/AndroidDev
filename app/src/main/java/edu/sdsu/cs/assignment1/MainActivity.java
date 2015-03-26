package edu.sdsu.cs.assignment1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String TAG = "Assignment1";
    private String keyTextViewData = "TextViewData";
    private TextView lifeCycleInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lifeCycleInfoTextView = (TextView) findViewById(R.id.life_cycle_info);

        Button clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        String onCreate = getString(R.string.on_create);
        Log.i(TAG, onCreate);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onCreate);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String onPause = getString(R.string.on_pause);
        Log.i(TAG, onPause);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onPause);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String onResume = getString(R.string.on_resume);
        Log.i(TAG, onResume);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onResume);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String onStart = getString(R.string.on_start);
        Log.i(TAG, onStart);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onStart);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String onRestart = getString(R.string.on_restart);
        Log.i(TAG, onRestart);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onRestart);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String onSaveInstanceState = getString(R.string.on_save_instance_state);
        Log.i(TAG, onSaveInstanceState);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onSaveInstanceState);
        outState.putString(keyTextViewData, lifeCycleInfoTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String onRestoreInstanceState = getString(R.string.on_restore_instance_state);
        lifeCycleInfoTextView.setText(savedInstanceState.getString(keyTextViewData) + lifeCycleInfoTextView.getText());
        Log.i(TAG, onRestoreInstanceState);
        lifeCycleInfoTextView.setText(lifeCycleInfoTextView.getText() + "\n" + onRestoreInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        lifeCycleInfoTextView.setText("");
    }
}
