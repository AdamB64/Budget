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
import com.example.budget.data.Users;
import com.example.budget.data.UsersDAO;
import com.example.budget.data.UsersRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragement extends Fragment implements View.OnClickListener {


    private List<Users> Goals;

    private List<Users> Budget;

    private UsersRepo mUsersRepo;

    //declare all the request
    private RequestQueue requestQueue;

    private RequestQueue requestQueue2;

    //set the first key as public
    private String firstKey;

    //declare the inputs
    private TextView budgetInputTextView;

    private TextView GoalInputTextView;

    //get the passes username varaible
    private final static String UsernamePassed=HomeFragment.UsernamePassed;

    //the passed username
    private String mUsername;

    public BudgetFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Username the users username
     * @return A new instance of fragment BudgetFragement.
     */
    public static BudgetFragement newInstance(String Username) {
        BudgetFragement fragment = new BudgetFragement();
        Bundle args = new Bundle();
        args.putString(UsernamePassed, Username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mUsername = getArguments().getString(UsernamePassed);
        }
        //declare the new repos and arrays
        this.mUsersRepo = new UsersRepo(getContext());
        this.Goals = new ArrayList<>();
        this.Budget = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.budget_fragement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //start all the volley requests
        requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue2= Volley.newRequestQueue(requireContext());


        //declare the inputs
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

        //declare budget
        int budget=0;

        // get all the income amounts and expenses amount
        Budget.addAll(this.mUsersRepo.findBudget(this.mUsername));
        //if there is any income and expenses amount do
        if(Budget.size()>0){
            //loop through the array size
            for(int i =0;i<Budget.size();i++){

                //set up the users for getters and setters
                Users users = Budget.get(i);

                //declare placeholderInc
                int PlaceHolderExp = users.getExpAmount();

                //declare PlaceHolderExp
                int PlaceHolderInc = users.getIncamount();

                //add income to budget
                budget+=PlaceHolderInc;
                //add expenses to budget
                budget-=PlaceHolderExp;

            }
            //out budget to fragment textview
            BudgetFragement.this.budgetInputTextView.setText("Budget: "+ budget);
        }


        //get the room data of the goal of users
        Goals.addAll(this.mUsersRepo.findGoal(this.mUsername));
        //check if room has any gaols for users else use firebase database
        if(Goals.size()>0){
                //set up users with first goal
                Users users = Goals.get(0);

                //set goal to room goal
                int goal =users.getGoal();
                //get percentage
                double  percent = ((double) budget/goal)*100;
                //display goal and percentage towards goal
            BudgetFragement.this.GoalInputTextView.setText("Goal: "+ goal+" and the percentage you are to reaching it "+percent+"%");
        }else {

            // Make a request to the Firebase link
            getUserKey(this.getView());
        }
    }
    @Override
    public void onClick(View view) {
        //create the bundle to pass in navigation
        Bundle bundle = new Bundle();
        //set the bundle as the username
        bundle.putString(UsernamePassed,this.mUsername);
        //a if statement for going to the different fragment and making sure it the right fragment
        if(view.getId()==R.id.BtnIncomeNav2){
            //navigate to the income fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_incomeFragment,bundle);
        }else if(view.getId()==R.id.BtnExpensesNav2){
            //navigate to the expenses fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_expenseFragment,bundle);
        } else if (view.getId()==R.id.BtnHome) {
            //navigate to the home fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_homeFragment,bundle);
        } else if (view.getId()==R.id.BtnGoalNavBudget) {
            //navigate to the goal fragment
            Navigation.findNavController(view).navigate(R.id.action_budgetFragement_to_goalsFragment,bundle);
        }
    }

    private void getUserKey(View view) {
        // The URL to get data
        String readUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+mUsername+"/.json";

        //the request to get the jsonobject first key
        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(
                Request.Method.GET, readUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {
                        try {
                            // Get the first key in the response
                            Iterator<String> keys = response2.keys();
                            if (keys.hasNext()) {
                                firstKey = keys.next();
                                // Call the method to perform the POST request with the obtained key
                                makeRequest(firstKey);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("WriteToDatabase", "Error handling GET response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occurred during the request
                        error.printStackTrace();
                        Log.e("WriteToDatabase", "Error reading from database");
                    }
                });

        // Add the request to the RequestQueue
        requestQueue2.add(jsonRequest2);
    }
    public void makeRequest(String key) {
        //url to make the GET
        String url = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+this.mUsername+"/"+key+"/.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("BudgetFragment", "Response received: " + response.toString());
                            // Parse the JSON data

                            double budget = response.getDouble("TotalBudget");
                            double goal = response.getDouble("Goal");

                            // Update the TextView with the result
                            BudgetFragement.this.budgetInputTextView.setText(String.format("Budget: %.2f", budget));
                            BudgetFragement.this.GoalInputTextView.setText("Goal: "+ goal+" and the percentage you are to reaching it "+((budget/goal)*100)+"%");
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