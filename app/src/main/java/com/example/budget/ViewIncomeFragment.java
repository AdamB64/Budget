package com.example.budget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

public class ViewIncomeFragment extends Fragment implements View.OnClickListener {

    //Url for the cloud databse
    private static final String FIREBASE_DATABASE_URL = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/Users.json";

    //Array for the income
    private ArrayList<String> incomeList;

    //Array adapter
    private ArrayAdapter<String> adapter;

    //volley request queue
    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewIncomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ViewIncomeFragment newInstance(String param1, String param2) {
        ViewIncomeFragment fragment = new ViewIncomeFragment();
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
        return inflater.inflate(R.layout.fragment_view_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Initialize the income list and the adapter
        incomeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, incomeList);

        // Set up the ListView
        ListView listView = view.findViewById(R.id.LstIncome);
        listView.setAdapter(adapter);

        // Get the button to navigate to the budget fragment
        Button BudgetNavButton = view.findViewById(R.id.BtnIncBudgetNav);
        BudgetNavButton.setOnClickListener(this);

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Fetch and display income data using Volley
        fetchAndDisplayIncomeData();
    }

    // Method to fetch and display income data using Volley
    private void fetchAndDisplayIncomeData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, FIREBASE_DATABASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ViewIncomeFragment", "Response: " + response.toString());
                        try {
                            JSONObject userObject = response;

                            JSONArray ExpensesArray = userObject.getJSONArray("Income");

                            if (ExpensesArray != null) {
                                for (int i = 0; i < ExpensesArray.length(); i++) {
                                    int amount = ExpensesArray.getJSONObject(i).getInt("Amount");
                                    Log.d("ViewIncomeFragment", "Amount: " + amount);
                                    String date = ExpensesArray.getJSONObject(i).getString("Date");
                                    Log.d("ViewIncomeFragment", "Date: " + date);
                                    String description = ExpensesArray.getJSONObject(i).getString("Description");
                                    if (description.equals("")) {
                                        description = "No Description";
                                    }

                                    //Display Expenses to the list view
                                    incomeList.add("Amount: " + amount + " date: " + date + " Description: " + description);

                                    //Notify the adapter that the data set has been changed
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //handles errors
                        error.printStackTrace();
                    }
                });

        //add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        // If statement to navigate to the budget fragment when button clicked
        if (view.getId() == R.id.BtnIncBudgetNav) {
            Navigation.findNavController(view).navigate(R.id.action_viewIncomeFragment_to_budgetFragement);
        }
    }
}
