package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnClickListener {




  public  Button forward_button;
    public  Button backward_button;
    public  Button left_button;
    public  Button right_button;
    private TextView headingTextView;

    public String vehicle = "";

    private Spinner planeSelectorSpinner;
    private String[] planes = new String[]{
            "Select a plane",
            "RedPlane",
            "BluePlane",
            "OrangePlane",
            "YellowPlane",
            "GreenPlane"

    };

    RequestQueue queue;
    String baseURL = "https://cambrianopenhouse.azurewebsites.net/api/race/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forward_button = (Button)findViewById(R.id.forward_button);
        backward_button = (Button)findViewById(R.id.backward_button);
        left_button = (Button)findViewById(R.id.left_button );
        right_button = (Button)findViewById(R.id.right_button);
        planeSelectorSpinner = findViewById(R.id.plane_selector_spinner);
        headingTextView = findViewById(R.id.plane_heading_tv);

        disableButtons();

        forward_button.setOnClickListener(this);
       backward_button.setOnClickListener(this);
       left_button.setOnClickListener(this);
        right_button.setOnClickListener(this);


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        List<String> planeList = new ArrayList<>(Arrays.asList(planes));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, planeList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                   textView.setTextColor(Color.BLACK);
                }

                return view;
            }
        };

        planeSelectorSpinner.setAdapter(spinnerArrayAdapter);
        planeSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position> 0) {

                    headingTextView.setText(parent.getItemAtPosition(position).toString());
                    vehicle = parent.getItemAtPosition(position).toString();
                    apiCall(vehicle);
                    enableButtons();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void enableButtons() {
        forward_button.setEnabled(true);
        backward_button.setEnabled(true);
        right_button.setEnabled(true);
        left_button.setEnabled(true);
    }

    private void disableButtons() {
        forward_button.setEnabled(false);
        backward_button.setEnabled(false);
        right_button.setEnabled(false);
        left_button.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        if( v.getId() == R.id.forward_button){
            apiCall(vehicle + "/Forward");

        }
        if( v.getId() == R.id.backward_button){
            apiCall(vehicle + "/Backward");

        }
        if( v.getId() == R.id.right_button){
            apiCall(vehicle + "/Right");


        }
        if( v.getId() == R.id.left_button){
            apiCall(vehicle + "/Left");

        }

    }

    private void apiCall(String command) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseURL + command,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(getApplicationContext(), response,
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
