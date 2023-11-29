package com.example.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragement extends Fragment implements View.OnClickListener{

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
}