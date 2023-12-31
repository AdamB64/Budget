package com.example.budget;

import android.os.Bundle;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.budget.data.Users;
import com.example.budget.data.UsersRepo;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment implements View.OnClickListener{

    private UsersRepo mUsersRepo;

    //set a textview as public for the goal replaced
    private TextView goalReplaced;

    //set the requestqueues
    private RequestQueue requestQueue;

    private RequestQueue requestQueue2;
    //set the first key as public
    private String firstKey;

    //set the username variable name
    private static final String UsernamePassed = HomeFragment.UsernamePassed;

    //set the username variable value
    private String mUsername;

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Username to get the right user
     * @return A new instance of fragment GoalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalsFragment newInstance(String Username) {
        GoalsFragment fragment = new GoalsFragment();
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
        this.mUsersRepo = new UsersRepo(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //set up the new requestqueues
        requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue2 = Volley.newRequestQueue(requireContext());

        //getting the buttons to nav to budget, income and expense fragments
        Button BudgetNavButton = view.findViewById(R.id.BtnBudgetNavInc);
        BudgetNavButton.setOnClickListener(this);

        Button IncomeButton = view.findViewById(R.id.BtnGoalNavInc);
        IncomeButton.setOnClickListener(this);

        Button ExpensesButton = view.findViewById(R.id.BtnGoalNavExp);
        ExpensesButton.setOnClickListener(this);

        //getting button to add the goal
        Button AddGoalsButton = view.findViewById(R.id.BtnAddGoals);
        AddGoalsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //seting up the bundle to be passed to other fragments
        Bundle bundle = new Bundle();
        //giving the bundle the username
        bundle.putString(UsernamePassed,this.mUsername);
        //if button pressed do
        if(view.getId()==R.id.BtnBudgetNavInc){
            //go to budget fragment
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_budgetFragement,bundle);
        } else if (view.getId()==R.id.BtnGoalNavExp) {
            //go to expenses fragment
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_expenseFragment,bundle);
        } else if (view.getId()==R.id.BtnGoalNavInc) {
            //go to income fragment
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_incomeFragment,bundle);
        } else if (view.getId() == R.id.BtnAddGoals) {
            //set goalreplaced as inputted value
            goalReplaced = this.getView().findViewById(R.id.NewGoalInput);
            //call write to database and pass the view
            writeToDatabase(this.getView());
        }
    }

    private void writeToDatabase(View view) {
        // The URL to get data
        String readUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+mUsername+"/.json";

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
                                GoalReplace(goalReplaced.getText().toString(),requireContext(),firstKey);
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

    public void GoalReplace(final String newGoal,final Context context,String key) {
        // Your Firebase Realtime Database URL
        String url = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+this.mUsername+"/"+key+"/.json";

        // Make a network request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //add the goal and the username to a users field
        Users users = new Users();
        users.setGoal(Integer.parseInt(newGoal));
        users.setUserName(this.mUsername);
        users.setTotalBudget(0);
        users.setDescription("");
        users.setIncamount(0);
        users.setExpAmount(0);
        users.setDate("");
        users.setPassword("");

        //add the user to the database
        this.mUsersRepo.addUser(users);

        // Create the JSONObject for the update
        try {
            JSONObject updatedData = new JSONObject();
            updatedData.put("Goal", Integer.parseInt(newGoal));

            // Create the request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PATCH,  // Use PATCH for partial updates
                    url,
                    updatedData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the successful response (if needed)
                            Toast.makeText(context, "Goal updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            String errorMessage;
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                errorMessage = new String(error.networkResponse.data);
                            } else {
                                errorMessage = "Unknown error";
                            }

                            Toast.makeText(context, "Error updating goal: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the queue
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}