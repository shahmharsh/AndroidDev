package edu.sdsu.cs.assignment3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InstructorsList extends ActionBarActivity implements AdapterView.OnItemClickListener{

    public static final String TAG = "InstructorsList";
    private ListView mListViewAllInstructors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors_list);

        mListViewAllInstructors = (ListView) findViewById(R.id.listViewAllInstructors);
        mListViewAllInstructors.setOnItemClickListener(this);

        if (Util.isNetworkAvailable(getApplicationContext())) {
            getAllInstructorsFromNetwork();
        } else {
            Instructors.getInstance(getApplicationContext()).getInstructorsFromDB();
            setInstructorsAdapter();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, InstructorDetails.class);
        intent.putExtra(Constants.keyPassedInstructorPosition, position);
        startActivity(intent);
    }

    private void getAllInstructorsFromNetwork() {
        Log.i(TAG, "Network Request for list");
        NetworkRequest networkRequest = NetworkRequest.getInstance(getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Constants.urlGetInstructors, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i=0; i<response.length(); i++) {
                        JSONObject instructorJSONObject = response.getJSONObject(i);
                        String firstName = instructorJSONObject.getString(Constants.keyFirstName);
                        String lastName = instructorJSONObject.getString(Constants.keyLastName);
                        int id = instructorJSONObject.getInt(Constants.keyID);

                        Instructor instructor = new Instructor(id, firstName, lastName);
                        Instructors.getInstance(getApplicationContext()).add(instructor);
                    }
                    setInstructorsAdapter();
                    Instructors.getInstance(getApplicationContext()).updateAllInstructorsInDB();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error : " + TAG, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Error Response");
            }
        });

        networkRequest.addToRequestQueue(jsonArrayRequest);
    }

    private void setInstructorsAdapter() {
        Instructor [] allInstructors = Instructors.getInstance(getApplicationContext()).getAllInstructors();
        ArrayAdapter<Instructor> adapter = new ArrayAdapter<Instructor>(getApplicationContext(), android.R.layout.simple_list_item_1, allInstructors) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        mListViewAllInstructors.setAdapter(adapter);
    }
}
