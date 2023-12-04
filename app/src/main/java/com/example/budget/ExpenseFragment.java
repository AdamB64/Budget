package com.example.budget;

import android.content.Context;
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
import com.example.budget.data.Users;
import com.example.budget.data.UsersRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.nio.BufferUnderflowException;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment implements View.OnClickListener{


    private UsersRepo mUsersRepo;

    //get the string for username
    private static final String UsernamePassed = HomeFragment.UsernamePassed;

    //set the first key as public
    private String firstKey;

    //set the expenses as public
    public int expenses;

    //set the requestqueues
    private RequestQueue requestQueue;

    private RequestQueue requestQueue2;

    private RequestQueue requestQueue3;

    //set the username parameter
    private String mUsername;


    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Username username of the user
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String Username) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //set the new requests
        requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue2 = Volley.newRequestQueue(requireContext());

        requestQueue3 = Volley.newRequestQueue(requireContext());

        //get the button to navigate to the expenses view page
        Button ExpensesAddButton = view.findViewById(R.id.BtnAddExpense);
        ExpensesAddButton.setOnClickListener(this);

        //getting the button to navigate to the budget page
        Button BudgetNavButton = view.findViewById(R.id.BtnBudgetNavExp);
        BudgetNavButton.setOnClickListener(this);

        //getting button to go to expenses view
        Button ExpensesViewButton = view.findViewById(R.id.BtnExpNavExpView);
        ExpensesViewButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        //set bundler to send to other fragments
        Bundle bundle = new Bundle();
        //add the username to the bundle
        bundle.putString(UsernamePassed,this.mUsername);
        //if button pressed do
        if(view.getId()==R.id.BtnAddExpense){
            //call this function
            writeToDatabase();
        } else if (view.getId()==R.id.BtnBudgetNavExp) {
            //go to the budget fragment
            Navigation.findNavController(view).navigate(R.id.action_expenseFragment_to_budgetFragement,bundle);
        } else if (view.getId()==R.id.BtnExpNavExpView) {
            //go to the view fragment
            Navigation.findNavController(view).navigate(R.id.action_expenseFragment_to_viewExpensesFragment,bundle);
        }
    }

    private void writeToDatabase() {
        // The URL to get data
        String readUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+this.mUsername+"/.json";

        JsonObjectRequest jsonRequest2 = new JsonObjectRequest(
                Request.Method.GET, readUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response2) {
                        try {
                            // Handle the response

                            // Get the first key in the response
                            Iterator<String> keys = response2.keys();
                            if (keys.hasNext()) {

                                //get the firstKey from response
                                firstKey = keys.next();
                                //placeholder jsonobject
                                JSONObject placeHolder = response2.getJSONObject(firstKey);
                                //get the budget data
                                int Budget = placeHolder.getInt("TotalBudget");
                                // Call the method to perform the POST request with the obtained key
                                performPostRequest(firstKey,ExpenseFragment.this.getView());
                                //set budget as budget - expenses
                                int TotalBudget = Budget -expenses;
                                //call function to update the budget and pass firstkey and totalbudget
                                postBudget(firstKey,requireContext(),TotalBudget);

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

    private void performPostRequest(String key,View view) {
        //getting the input fields
        String Amount = ((EditText) view.findViewById(R.id.InputExpense)).getText().toString();
        String date = ((EditText) view.findViewById(R.id.InputExpDate)).getText().toString();
        String Description = ((EditText) view.findViewById(R.id.InputExpDescription)).getText().toString();
        //if user didn't change description make it blank
        if(Description.matches("Type here a description for the expense if wanted")){
            Description="";
        }
        //check that date is right format
        if(isValidDate(date)) {
            //get that amount is a number
            if(Amount.matches("[0-9]+")) {
                // Assuming you have a JSONObject with the data you want to write
                //make a new object for the new income
                JSONObject postData = new JSONObject();
                try {
                    //adding to the room-sql
                    Users users = new Users();
                    users.setPassword("");
                    users.setUserName(this.mUsername);
                    users.setGoal(0);
                    users.setDescription(Description);
                    users.setDate(date);
                    users.setIncamount(0);
                    users.setExpAmount(Integer.parseInt(Amount));
                    users.setTotalBudget(0);

                    mUsersRepo.addUser(users);
                    //put the inputted variable in to the jsonobject
                    postData.put("Amount", Amount);
                    postData.put("Date", date);
                    postData.put("Description", Description);
                    //set expenses for other method
                    expenses=Integer.parseInt(Amount);
                    // Add other data fields as needed
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // The URL to post data
                String writeUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/" + this.mUsername + "/" + key + "/Expenses.json";

                // Create a JsonObjectRequest with POST method
                JsonObjectRequest jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, writeUrl, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //success call
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
                Bundle bundle=new Bundle();
                bundle.putString(UsernamePassed,this.mUsername);
                Navigation.findNavController(this.getView()).navigate(R.id.action_expenseFragment_to_viewExpensesFragment, bundle);
            }else {
                Toast.makeText(getContext(), R.string.tvErrorAmount, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //a method to see if a date is in the right format
    private static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);  // This ensures strict validation

        try {
            // Try parsing the date
            Date date = sdf.parse(dateStr);

            // If parsing succeeds, it's a valid date
            return true;
        } catch (ParseException e) {
            // Parsing failed, not a valid date
            return false;
        }
    }

    private void postBudget(String key, Context context,int budget){
        //url to PATCH to
        String writeUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/" + this.mUsername + "/" + key + "/.json";

        //request
        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        try {
            //set up newData with budget
            JSONObject updateData = new JSONObject();
            updateData.put("TotalBudget", budget);

            //post the updatedata to the url
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PATCH, writeUrl, updateData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //add a success caller
                            Toast.makeText(context, "Budget updated as well", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage;
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                errorMessage = new String(error.networkResponse.data);
                            } else {
                                errorMessage = "Unknown error";
                            }

                            Toast.makeText(context, "Error updating budget: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue3.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}