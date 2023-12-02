package com.example.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());


        //get the button for register
        Button HomeNavReg = view.findViewById(R.id.BntRegister);
        HomeNavReg.setOnClickListener(this);
    }
    @Override
    public void onClick(@NonNull View view){
        //if the register button is clicked go to home page
        if(view.getId()==R.id.BntRegister){
            boolean r =writeToDatabase(this.getView());
            if(r==true) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        }
    }

    private boolean writeToDatabase(@NonNull View view) {
        boolean r =false;
        // Retrieve data from input fields
        String userName = ((EditText) view.findViewById(R.id.InputName)).getText().toString();
        String userPassword = ((EditText) view.findViewById(R.id.InputPassword)).getText().toString();
        // Assuming you have a JSONObject with the data you want to write
        JSONObject postData = new JSONObject();
        try {
            if(userName.isEmpty()==true || userPassword.isEmpty()==true) {
                Toast.makeText(getContext(), "Must have input in all fields", Toast.LENGTH_SHORT).show();
            }else {
                r=true;
                // Create an array for expenses
                JSONObject expensesArray = new JSONObject();
                JSONObject expenseObject = new JSONObject();
                // You may want to get these values from the user input fields
                expenseObject.put("Amount", 0);
                expenseObject.put("Date", "");
                expenseObject.put("Description", "");
                expensesArray.put("Expenses",expenseObject);

                // Create an array for income
                JSONObject incomeArray = new JSONObject();
                JSONObject incomeObject = new JSONObject();
                // You may want to get these values from the user input fields
                incomeObject.put("Amount", 0);  // Replace with the actual amount value
                incomeObject.put("Date", "");  // Replace with the actual date value
                incomeObject.put("Description", "");  // Replace with the actual description value
                incomeArray.put("Income",incomeObject);

                // Put arrays into the JSON object
                postData.put("UserName", userName);
                postData.put("Password", userPassword);
                postData.put("Expenses", expensesArray);
                postData.put("Income", incomeArray);
                postData.put("Goal","");
                postData.put("TotalBudget","");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // The URL to which you want to send the POST request
        String writeUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+userName+".json";

        // Create a JsonObjectRequest with POST method
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST, writeUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the server after writing data
                        // You may want to update your UI or perform other actions
                        // based on the server's response
                        Log.d("WriteToDatabase", "Write successful");
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
        return r;
    }

}