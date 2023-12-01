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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragement extends Fragment implements View.OnClickListener {

    private RequestQueue requestQueue;

    private TextView budgetInputTextView;

    private TextView GoalInputTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BudgetFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetFragement newInstance(String param1, String param2) {
        BudgetFragement fragment = new BudgetFragement();
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
        return inflater.inflate(R.layout.budget_fragement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(requireContext());

        budgetInputTextView = view.findViewById(R.id.BudgetInput);

        GoalInputTextView=view.findViewById(R.id.GoalInput);

        //getting the button to navigate to the income fragment
        Button IncomeNavButton = view.findViewById(R.id.BtnIncomeNav2);
        IncomeNavButton.setOnClickListener(this);

        //getting the button to navigate to the expenses fragment
        Button ExpensesNavButton = view.findViewById(R.id.BtnExpensesNav2);
        ExpensesNavButton.setOnClickListener(this);

        //getting the button to nav to goals and home fragments
        Button HomeNavButton = view.findViewById(R.id.BtnHome);
        HomeNavButton.setOnClickListener(this);

        Button GoalNavButton = view.findViewById(R.id.BtnGoalNavBudget);
        GoalNavButton.setOnClickListener(this);
        // Make a request to the Firebase link
        makeRequest();
    }
    @Override
    public void onClick(View view) {
        //a if statement for going to the different fragment and making sure it the right fragment
        if(view.getId()==R.id.BtnIncomeNav2){
            //navigate to the income fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_incomeFragment);
        }else if(view.getId()==R.id.BtnExpensesNav2){
            //navigate to the expenses fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_expenseFragment);
        } else if (view.getId()==R.id.BtnHome) {
            //navigate to the home fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_homeFragment);
        } else if (view.getId()==R.id.BtnGoalNavBudget) {
            //navigate to the goal fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_goalsFragment);
        }
    }

    private void makeRequest() {
        String url = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/0/.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("BudgetFragment", "Response received: " + response.toString());

                        try {
                            // Parse the JSON data
                            JSONObject placeHolder = response.getJSONObject("Users");

                            double budget = placeHolder.getDouble("TotalBudget");

                            double goal = placeHolder.getDouble("Goal");

                            // Update the TextView with the result
                            budgetInputTextView.setText(String.format("Budget: %.2f", budget));
                            GoalInputTextView.setText("Goal: "+ goal+"%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("BudgetFragment", "JSONException: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("BudgetFragment", "VolleyError: " + error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}