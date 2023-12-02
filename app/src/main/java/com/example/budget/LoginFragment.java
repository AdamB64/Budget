package com.example.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.budget.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private RequestQueue requestQueue;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mUsername;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mUsername Parameter 1.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String mUsername) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        //args.putString(LogUsername,mUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //this.mUsername = getArguments().getString(LogUsername);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        Button LoginNavHomeButton = view.findViewById(R.id.BtnLogin);
        LoginNavHomeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //going to the home fragment if button clicked
        if(view.getId()== R.id.BtnLogin){
            login(this.getView());
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void login(View view){

        //get the user inputted username
        EditText userNameEditText = view.findViewById(R.id.Input_name);
        String UserName = userNameEditText.getText().toString();

        EditText passwordEditText = view.findViewById(R.id.PasswordInput);
        String PasswordInput = passwordEditText.getText().toString();

        // The URL to which you want to send the POST request
        String writeUrl = String.format("https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/%s/.json",UserName);

        // Create a JsonObjectRequest with POST method
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, writeUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 0) {
                                // Handle the response

                                // Get the first key in the response
                                Iterator<String> keys = response.keys();
                                if (keys.hasNext()) {
                                    String firstKey = keys.next();
                                    JSONObject firstObject = response.getJSONObject(firstKey);
                                    String Username = firstObject.getString("UserName");
                                    String Password = firstObject.getString("Password");

                                    //Toast.makeText(getContext(), "pass and username: "+Username+" "+ Password, Toast.LENGTH_SHORT).show();
                                    if(Username.equals(UserName) && Password.equals(PasswordInput)){
                                        Bundle bundle = new Bundle();
                                        bundle.putString(HomeFragment.UsernamePassed,Username);
                                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment,bundle);
                                    }else{
                                        Toast.makeText(getContext(), "User doesn't exist or password wrong", Toast.LENGTH_SHORT).show();
                                    }
                                // Do something with the first object
                                Log.d("WriteToDatabase", "First object found: " + firstObject.toString());
                                }
                            } else {
                                Log.e("WriteToDatabase", "Empty JSON response");
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("WriteToDatabase", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occurred during the request
                        error.printStackTrace();
                        Log.e("WriteToDatabase", "Error writing to database");
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonRequest);
    }
}