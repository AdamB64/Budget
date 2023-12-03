package com.example.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    //set up the username variable for it to be used in other fragments
    public static final String UsernamePassed = "Username";


    //set up the user name passed value
    private String mUsername;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Username Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String Username) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //getting the button for going to the budget fragment and setting a on click listener
        Button BudgetButton = view.findViewById(R.id.BtnBudget);
        BudgetButton.setOnClickListener(this);

        //getting the button for going to the goal fragment and setting a on click listener
        Button GoalButton = view.findViewById(R.id.BtnGoals);
        GoalButton.setOnClickListener(this);

        //getting the button for going to the income fragment and setting a on click listener
        Button IncomeButtonNav = view.findViewById(R.id.BtnIncome);
        IncomeButtonNav.setOnClickListener(this);

        //getting the button for going to the expenses fragment and setting a on click listener
        Button ExpensesButton = view.findViewById(R.id.BtnExpenses);
        ExpensesButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        //setting the bundle to be passed in the navigation
        Bundle bundle = new Bundle();
        //adding the username to the bundle
        bundle.putString(UsernamePassed,mUsername);
        //making a if statement that check what button pressed and goes to the fragment
        if(view.getId()==R.id.BtnBudget){
            //goes to the budget fragment
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_budgetFragement,bundle);
        } else if (view.getId()==R.id.BtnGoals) {
            //goes to the Goal fragment
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_goalsFragment,bundle);
        } else if (view.getId()==R.id.BtnIncome) {
            //goes to the income fragment
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_incomeFragment,bundle);
        } else if (view.getId()==R.id.BtnExpenses) {
            //goes to the expenses fragment
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_expenseFragment,bundle);
        }
    }
}