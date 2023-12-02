package com.example.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewIncomeFragment extends Fragment implements View.OnClickListener{

    private static final String UsernamePassed = HomeFragment.UsernamePassed;

    private String firstKey;

    //Array for the expenses
    private ArrayList<String> incomeList;

    //array adapter
    private ArrayAdapter<String> adapter;

    //Volley request queue
    private RequestQueue requestQueue;

    private RequestQueue requestQueue2;

    // TODO: Rename and change types of parameters
    private String mUsername;

    public ViewIncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Username username of user
     * @return A new instance of fragment ViewExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewExpensesFragment newInstance(String Username) {
        ViewExpensesFragment fragment = new ViewExpensesFragment();
        Bundle args = new Bundle();
        args.putString(Username, Username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mUsername = getArguments().getString(UsernamePassed);
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
        //Initialise the expenses list and the adapter
        incomeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(),android.R.layout.simple_list_item_1,incomeList);

        //set up the list view
        ListView listView = view.findViewById(R.id.LstIncome);
        listView.setAdapter(adapter);

        //get the button to navigate to the budget page
        Button BudgetNavButton = view.findViewById(R.id.BtnIncBudgetNav);
        BudgetNavButton.setOnClickListener(this);

        //Initialise the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        requestQueue2=Volley.newRequestQueue(requireContext());

        //fetch and display expense data using Volloy
        writeToDatabase(this.getView());
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
                            // Handle the response

                            // Get the first key in the response
                            Iterator<String> keys = response2.keys();
                            if (keys.hasNext()) {
                                firstKey = keys.next();
                                // Call the method to perform the POST request with the obtained key
                                fetchAndDisplayExpensesData(firstKey);
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

    private void fetchAndDisplayExpensesData(String key){
        incomeList.clear();
        String Url = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/"+mUsername+"/"+key+"/Income.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ViewIncomeFragment", "Response: " + response.toString());
                        try {
                            String userObject = response.toString();

                            // Create an ObjectMapper
                            ObjectMapper objectMapper = new ObjectMapper();

                            JsonNode jsonNode = objectMapper.readTree(userObject);

                            for (JsonNode entry : jsonNode) {
                                int amount = entry.get("Amount").asInt();
                                String date = entry.get("Date").asText();
                                String description = entry.get("Description").asText();

                                Log.d("ViewIncomeFragment", "Response: " + amount+date+description);
                                if (amount!=0) {
                                    // Display Expenses to the list view
                                    incomeList.add("Amount: " + amount + ", Date: " + date + ", Description: " + description);
                                    // Notify the adapter that the data set has been changed
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
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
        //if statement to navigate to the budget page
        if(view.getId()==R.id.BtnIncBudgetNav){
            Navigation.findNavController(view).navigate(R.id.action_viewIncomeFragment_to_budgetFragement);
        }
    }
}