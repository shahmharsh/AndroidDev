package edu.sdsu.cs.assignment2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ListActivity extends ActionBarActivity implements ListViewFragment.OnDataPass{

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int passedData = getIntent().getExtras().getInt(MainActivity.LIST_VIEW_SELECTION);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.LIST_VIEW_SELECTION, passedData);
            ListViewFragment fragment = new ListViewFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_fragment_holder,fragment)
                    .commit();
        }

        backButton = (Button) findViewById(R.id.listButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
    public void onDataPass(int position) {
        Intent data = getIntent();
        data.putExtra(MainActivity.LIST_VIEW_SELECTION, position);
        setResult(RESULT_OK, data);
    }
}
