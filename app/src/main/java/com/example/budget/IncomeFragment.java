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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment implements View.OnClickListener {

    //set up the requestqueue
    private RequestQueue requestQueue;

    private  RequestQueue requestQueue2;

    private RequestQueue requestQueue3;

    // the fragment initialization parameters
    private static final String UsernamePassed = HomeFragment.UsernamePassed;

    private String firstKey;

    public boolean login;

    public int income;
    //parameters
    private String mUsername;

    private UsersRepo mUserRepo;



    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param Username User username
     * @return A new instance of fragment IncomeFragment.
     */
    public static IncomeFragment newInstance(String Username) {
        IncomeFragment fragment = new IncomeFragment();
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
        this.mUserRepo = new UsersRepo(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Initialize the RequestQueues
        requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue2=Volley.newRequestQueue(requireContext());

        requestQueue3=Volley.newRequestQueue(requireContext());

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
        //set the bundle to be passed on navigation
        Bundle bundle=new Bundle();
        //add the username in the bundle
        bundle.putString(UsernamePassed,this.mUsername);
        //if button pressed do
        if (view.getId() == R.id.BtnAddIncome) {
            //this method
            writeToDatabase();
        } else if (view.getId() == R.id.btnBudgetNavInc) {
            //go to budget fragment
            Navigation.findNavController(view).navigate(R.id.action_incomeFragment_to_budgetFragement,bundle);
        } else if (view.getId() == R.id.BtnIncNavIncView) {
            //go to view income fragment
            Navigation.findNavController(view).navigate(R.id.action_incomeFragment_to_viewIncomeFragment,bundle);
        }
    }


    private void writeToDatabase() {
        // The URL to get data
        String readUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+mUsername+"/.json";

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
                                //get the firstkey from response
                                firstKey = keys.next();
                                //set placeholder for first key of response
                                JSONObject placeholder = response2.getJSONObject(firstKey);
                                //get the budget from user
                                int Budget = placeholder.getInt("TotalBudget");
                                // Call the method to perform the POST request with the obtained key
                                performPostRequest(firstKey,IncomeFragment.this.getView());
                                //add the income to the budget
                                int TotalBudget = Budget+income;
                                //call method and pass first and totalbudget
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
        String Amount = ((EditText) view.findViewById(R.id.InputIncome)).getText().toString();
        String date = ((EditText) view.findViewById(R.id.InputIncDate)).getText().toString();
        String Description = ((EditText) view.findViewById(R.id.InputIncDescription)).getText().toString();
        if(Description.matches("Type here a description for the income if wanted")){
            Description="";
        }
        if(isValidDate(date)) {
            if (Amount.matches("[0-9]+")) {
                login = true;
                income=Integer.parseInt(Amount);
                //set up jsonobject
                JSONObject postData = new JSONObject();
                try {
                    Users users = new Users();
                    users.setPassword("");
                    users.setUserName(this.mUsername);
                    users.setGoal(0);
                    users.setDescription(Description);
                    users.setDate(date);
                    users.setIncamount(Integer.parseInt(Amount));
                    users.setExpAmount(0);
                    users.setTotalBudget(0);

                    mUserRepo.addUser(users);
                    //add the user input to the json object
                    postData.put("Amount", Amount);
                    postData.put("Date", date);
                    postData.put("Description", Description);
                    // Add other data fields as needed
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // The URL to post data
                String writeUrl = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/" + this.mUsername + "/" + key + "/Income.json";

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
                //set up bundle with username and pass it in the nav
                Bundle bundle=new Bundle();
                bundle.putString(UsernamePassed,this.mUsername);
                Navigation.findNavController(this.getView()).navigate(R.id.action_incomeFragment_to_viewIncomeFragment, bundle);
            }else {
                Toast.makeText(getContext(), R.string.tvErrorAmount, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), R.string.tvErrorDate, Toast.LENGTH_SHORT).show();
        }

    }


    //method to see if date is in right format
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

        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        try {
            //set up new jsonobject
            JSONObject updateData = new JSONObject();
            //add the new budget to it to be PATCHed to the url
            updateData.put("TotalBudget",budget);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PATCH, writeUrl, updateData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //success call
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