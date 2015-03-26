package edu.sdsu.cs.assignment3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InstructorDetails extends ActionBarActivity implements View.OnClickListener{
    public static final String TAG = "InstructorDetails";
    private int mInstructorPosition = -1;
    private Instructor mInstructor = null;
    private ArrayAdapter<Comment> mListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_details);

        mInstructorPosition = getIntent().getIntExtra(Constants.keyPassedInstructorPosition, -1);

        if (mInstructorPosition != -1) {
            mInstructor = Instructors.getInstance(getApplicationContext()).getAllInstructors()[mInstructorPosition];

            if (mInstructor.isDataCached()) {
                updateViews();
                updateComments();
            } else {
                if (Util.isNetworkAvailable(getApplicationContext())) {
                    getInstructorDetailsFromNetwork();
                } else {
                    mInstructor.getDetailsFromDB(getApplicationContext());
                    updateViews();
                    updateComments();
                }
            }

            //set click listeners for the buttons
            Button buttonRate = (Button) findViewById(R.id.buttonRate);
            Button buttonComment = (Button) findViewById(R.id.buttonComment);

            buttonRate.setOnClickListener(this);
            buttonComment.setOnClickListener(this);
        }
    }

    private void getInstructorDetailsFromNetwork() {
        Log.i(TAG, "Network Request for details");
        String url = Constants.urlGetInstructorDetails + mInstructor.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String email = response.getString(Constants.keyEmail);
                    String phone = response.getString(Constants.keyPhone);
                    String office = response.getString(Constants.keyOffice);
                    JSONObject jsonRating = response.getJSONObject(Constants.keyRating);
                    Double averageRating = jsonRating.getDouble(Constants.keyAverageRatings);
                    int totalRatings = jsonRating.getInt(Constants.keyTotalRating);
                    mInstructor.setRating(averageRating);
                    mInstructor.setNumberOfRatings(totalRatings);
                    mInstructor.setEmail(email);
                    mInstructor.setOffice(office);
                    mInstructor.setPhone(phone);
                    mInstructor.updateInDB(getApplicationContext());
                    updateViews();
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

        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        getCommentsFromNetwork();
    }

    private void getCommentsFromNetwork() {
        Log.i(TAG, "Network Request for Comments");
        String url = Constants.urlGetInstructorComment + mInstructor.getId();
        JsonArrayRequest commentsArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    mInstructor.clearComments();
                    for (int i=0; i<response.length(); i++) {
                        JSONObject commentJSONObject = response.getJSONObject(i);
                        String date = commentJSONObject.getString(Constants.keyCommentDate);
                        String text = commentJSONObject.getString(Constants.keyCommentText);

                        Comment comment = new Comment(date, text);
                        mInstructor.addComment(comment);
                    }
                    updateComments();
                    mInstructor.updateCommentsInDB(getApplicationContext());
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

        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(commentsArrayRequest);
    }

    private void updateViews() {
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        TextView textViewOffice = (TextView) findViewById(R.id.textViewOffice);
        TextView textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewName.setText(mInstructor.getName());
        textViewEmail.setText(getString(R.string.email) + mInstructor.getEmail());
        textViewOffice.setText(getString(R.string.office) + mInstructor.getOffice());
        textViewPhone.setText(getString(R.string.phone) + mInstructor.getPhone());

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(mInstructor.getRating().floatValue());
    }

    private void updateComments() {
        ListView listViewComments = (ListView) findViewById(R.id.listViewComments);
        Comment [] allComments = mInstructor.getAllComments();
        mListViewAdapter = new ArrayAdapter<Comment>(getApplicationContext(), android.R.layout.simple_list_item_1, allComments) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        listViewComments.setAdapter(mListViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (Util.isNetworkAvailable(getApplicationContext())) {
            if (v.getId() == R.id.buttonRate) {
                final Dialog ratingDialog = new Dialog(this, R.style.FullHeightDialog);
                ratingDialog.setContentView(R.layout.rating_dialog);
                ratingDialog.setCancelable(true);
                ratingDialog.show();

                Button submitButton = (Button) ratingDialog.findViewById(R.id.dialogRatingSubmitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RatingBar ratingBar = (RatingBar) ratingDialog.findViewById(R.id.dialogRatingBar);
                        float rating = ratingBar.getRating();
                        postInstructorRating(rating);
                        ratingDialog.dismiss();
                    }
                });
            } else { //comment button
                final Dialog commentDialog = new Dialog(this, R.style.FullHeightDialog);
                commentDialog.setContentView(R.layout.comment_dialog);
                commentDialog.setCancelable(true);
                commentDialog.show();

                Button submitButton = (Button) commentDialog.findViewById(R.id.dialogCommentSubmitButton);
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) commentDialog.findViewById(R.id.editTextComment);
                        String comment = editText.getText().toString();
                        postInstructorComment(comment);
                        commentDialog.dismiss();
                    }
                });
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Network Unavailable")
                    .setMessage("Please check network connection and try again.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void postInstructorRating(float rating) {
        String url = Constants.urlPostInstructorRating + mInstructor.getId() + "/" + rating;
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Double averageRating = response.getDouble(Constants.keyAverageRatings);
                    int totalRatings = response.getInt(Constants.keyTotalRating);
                    mInstructor.setRating(averageRating);
                    mInstructor.setNumberOfRatings(totalRatings);
                    mInstructor.updateInDB(getApplicationContext());
                    updateViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });
        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }

    private void postInstructorComment(final String comment) {
        String url = Constants.urlPostInstructorComment + mInstructor.getId();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase(Constants.okay)) {
                    getCommentsFromNetwork();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return comment.getBytes();
            }
        };
        NetworkRequest.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
    }
}

//Reference : 1) RatingBar ->  http://monzurulislam.blogspot.in/2011/12/android-custom-dialog-box-with-ratting.html
