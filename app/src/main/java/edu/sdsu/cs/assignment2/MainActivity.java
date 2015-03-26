package edu.sdsu.cs.assignment2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, ListViewFragment.OnDataPass {
    public static final String TEXT_FIELD_CONTENT = "TextFieldContent";
    public static final String LIST_VIEW_SELECTION = "ListViewSelection";
    private static final int INTENT_DATE_REQUEST_CODE = 1;
    private static final int INTENT_LIST_REQUEST_CODE = 2;
    private Button show_button;
    private Spinner spinner;
    private EditText editText;
    private int listViewSelection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.LIST_VIEW_SELECTION, listViewSelection);
            ListViewFragment fragment = new ListViewFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_holder,fragment)
                    .commit();
        }

        show_button = (Button) findViewById(R.id.show_button);
        show_button.setOnClickListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);

        editText = (EditText) findViewById(R.id.editText);
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
        } else {
            switch (id) {
                case R.id.action_activity_date:
                    showActivity(getString(R.string.set_date));
                    break;
                case R.id.action_activity_keyboard:
                    showActivity(getString(R.string.enter_personal_details));
                    break;
                case R.id.action_activity_list:
                    showActivity(getString(R.string.show_android_versions));
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String action = spinner.getSelectedItem().toString();
        //Log.i(TAG, nextActivity);
        showActivity(action);
    }

    void showActivity(String activity_title) {
        if(activity_title.equalsIgnoreCase(getString(R.string.set_date))) {
            Intent dateActivityIntent = null;
            dateActivityIntent = new Intent(this, DateActivity.class);
            startActivityForResult(dateActivityIntent, INTENT_DATE_REQUEST_CODE);
        } else if (activity_title.equalsIgnoreCase(getString(R.string.enter_personal_details))) {
            Intent keyboardActivityIntent = null;
            keyboardActivityIntent = new Intent(this, KeyboardActivity.class);
            keyboardActivityIntent.putExtra(TEXT_FIELD_CONTENT, editText.getText().toString());
            startActivity(keyboardActivityIntent);
        } else {
            Intent listIntent = new Intent(this, ListActivity.class);
            listIntent.putExtra(LIST_VIEW_SELECTION, listViewSelection);
            startActivityForResult(listIntent, INTENT_LIST_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_DATE_REQUEST_CODE) {
                String date = data.getStringExtra("Date");
                if (data != null) {
                    editText.setText(date);
                }
            } else if (requestCode == INTENT_LIST_REQUEST_CODE) {
                listViewSelection = data.getIntExtra(LIST_VIEW_SELECTION, -1);
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.LIST_VIEW_SELECTION, listViewSelection);
                ListViewFragment fragment = new ListViewFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_holder,fragment)
                        .commit();
            }
        }
    }

    @Override
    public void onDataPass(int position) {
        listViewSelection = position;
    }
}
