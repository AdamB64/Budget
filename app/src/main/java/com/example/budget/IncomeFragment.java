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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.nio.BufferUnderflowException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment implements View.OnClickListener {

    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
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
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        //getting the button that navigate to the income view fragment
        Button IncomeAddButton = view.findViewById(R.id.BtnAddIncome);
        IncomeAddButton.setOnClickListener(this);

        //getting the button that navigate to the budget fragment
        Button BudgetNavButton = view.findViewById(R.id.btnBudgetNavInc);
        BudgetNavButton.setOnClickListener(this);

        //getting the button to view all expenses
        Button IncomeViewButton = view.findViewById(R.id.BtnIncNavIncView);
        IncomeViewButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BtnAddIncome) {
            //writeToDatabase(this.getView());
            Navigation.findNavController(view).navigate(R.id.action_incomeFragment_to_viewIncomeFragment);
        } else if (view.getId() == R.id.btnBudgetNavInc) {
            Navigation.findNavController(view).navigate(R.id.action_incomeFragment_to_budgetFragement);
        } else if (view.getId() == R.id.BtnIncNavIncView) {
            Navigation.findNavController(view).navigate(R.id.action_incomeFragment_to_viewIncomeFragment);
        }
    }

    /*
    private void writeToDatabase(View view) {
        //getting the input fields
        String Amount = ((EditText) view.findViewById(R.id.InputIncome)).getText().toString();
        String Date = ((EditText) view.findViewById(R.id.InputIncDate)).getText().toString();
        String Description = ((EditText) view.findViewById(R.id.InputIncDescription)).getText().toString();
        // Assuming you have a JSONObject with the data you want to write
        JSONObject postData = new JSONObject();
        try {
            postData.put("Amount", Amount);
            postData.put("Date", Date);
            postData.put("Description",Description);
            // Add other data fields as needed
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // The URL to which you want to send the POST request
        String writeUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/Users/Income.json";

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
    }*/
}