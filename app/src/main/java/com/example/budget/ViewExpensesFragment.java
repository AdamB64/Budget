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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewExpensesFragment extends Fragment implements View.OnClickListener{



    //url for the cloud database
    private static final String FIREBASE_DATABASE_URL = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/Users.json";

    //Array for the expenses
    private ArrayList<String> expensesList;

    //array adapter
    private ArrayAdapter<String> adapter;

    //Volley request queue
    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewExpensesFragment newInstance(String param1, String param2) {
        ViewExpensesFragment fragment = new ViewExpensesFragment();
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
        return inflater.inflate(R.layout.fragment_view_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Initialise the expenses list and the adapter
        expensesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(),android.R.layout.simple_list_item_1,expensesList);

        //set up the list view
        ListView listView = view.findViewById(R.id.LstExpenses);
        listView.setAdapter(adapter);

        //get the button to navigate to the budget page
        Button BudgetNavButton = view.findViewById(R.id.BtnIncBudgetNav);
        BudgetNavButton.setOnClickListener(this);

        //Initialise the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        //fetch and display expense data using Volloy
        fetchAndDisplayExpensesData();
    }


    private void fetchAndDisplayExpensesData(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, FIREBASE_DATABASE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ViewIncomeFragment", "Response: " + response.toString());
                        try {
                            JSONObject userObject = response;

                            JSONArray ExpensesArray = userObject.getJSONArray("Expenses");

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
                                    expensesList.add("Amount: " + amount + " date: " + date + " Description: " + description);

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
        //if statement to navigate to the budget page
        if(view.getId()==R.id.BtnIncBudgetNav){
            Navigation.findNavController(view).navigate(R.id.action_viewExpensesFragment_to_budgetFragement);
        }
    }
}