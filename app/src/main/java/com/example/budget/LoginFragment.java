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
import android.widget.Toast;

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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    //private static final String LogUsername =RegisterFragment.RegUsername;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mUsername;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mUsername Parameter 1.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String mUsername) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        //args.putString(LogUsername,mUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //this.mUsername = getArguments().getString(LogUsername);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        Button LoginNavHomeButton = view.findViewById(R.id.BtnLogin);
        LoginNavHomeButton.setOnClickListener(this);
        Toast.makeText(getContext(), "Username"+this.mUsername, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        //going to the home fragment if button clicked
        if(view.getId()==R.id.BtnLogin){
            boolean loginTrue=login(this.getView());
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    private boolean login(View view){
        boolean loginTrue=false;

        return loginTrue;
    }

}