package com.example.solartask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Objects;

public class Settings extends Fragment {

    // Properties
    private boolean darkModeEnabled;
    private boolean notificationsEnabled;
    public static int notificationFrequency;
    public Button notButton;
    public Button aboutUsButton;
    public Button privacyButton;
    public Button termsButton;
    public Switch darkSwitch;

    /**
     * Empty Settings Constructor
     */
    public Settings() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Settings.
     */
    public static Settings newInstance() {
        Settings fragment = new Settings();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        //Setting up the dark switch and dark mode
        darkSwitch = (Switch) v.findViewById( R.id.darkSwitch) ;
        darkModeEnabled = darkSwitch.isChecked();

        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            darkSwitch.setChecked(true);
        }

        darkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      if (darkSwitch.isChecked()) {
                                                          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                                      } else {
                                                          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                                      }
                                                  }
                                              }
        );

        //Initializing the buttons
        this.notButton = (Button) v.findViewById(R.id.notificationButton); //This not a "Notifications" button. It has changed to the "Share Button".
        this.aboutUsButton = (Button) v.findViewById(R.id.aboutUsButton);
        this.privacyButton = (Button) v.findViewById(R.id.privacyButton);

        //Setting up the navigation
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragNavHost);
        NavController navController = navHostFragment.getNavController();

        //Setting up the functions of the buttons and the navigation
        notButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PdfGeneratorMainActivity.class);
                startActivity(i);
                ((Activity) requireActivity()).overridePendingTransition(0, 0);
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navController.navigate(R.id.action_settings_to_aboutUs);
            }
        });
        privacyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                navController.navigate(R.id.action_settings_to_privacyPolicy);
            }
        });

        return v;
    }
}