package com.example.budget;

import android.os.Bundle;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment implements View.OnClickListener{

    private TextView goalReplaced;

    private RequestQueue requestQueue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalsFragment newInstance(String param1, String param2) {
        GoalsFragment fragment = new GoalsFragment();
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
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(requireContext());

        //getting the buttons to nav to budget, income and expense fragments
        Button BudgetNavButton = view.findViewById(R.id.BtnBudgetNavInc);
        BudgetNavButton.setOnClickListener(this);

        Button IncomeButton = view.findViewById(R.id.BtnGoalNavInc);
        IncomeButton.setOnClickListener(this);

        Button ExpensesButton = view.findViewById(R.id.BtnGoalNavExp);
        ExpensesButton.setOnClickListener(this);

        Button AddGoalsButton = view.findViewById(R.id.BtnAddGoals);
        AddGoalsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.BtnBudgetNavInc){
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_budgetFragement);
        } else if (view.getId()==R.id.BtnGoalNavExp) {
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_expenseFragment);
        } else if (view.getId()==R.id.BtnGoalNavInc) {
            Navigation.findNavController(view).navigate(R.id.action_goalsFragment_to_incomeFragment);
        } else if (view.getId() == R.id.BtnAddGoals) {
            goalReplaced = this.getView().findViewById(R.id.NewGoalInput);
            GoalReplace(goalReplaced.getText().toString(),requireContext());
        }
    }

    public static void GoalReplace(final String newGoal,final Context context) {
        // Your Firebase Realtime Database URL
        String url = "https://weather-f9ae8-default-rtdb.firebaseio.com/Budget/0/Users/Goal.json";

        // Make a network request using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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