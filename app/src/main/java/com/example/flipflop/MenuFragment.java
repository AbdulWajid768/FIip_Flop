package com.example.flipflop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * This class handles the fragment_menu.xml
 */
public class MenuFragment extends Fragment {
    private Button easyBtn, mediumBtn, hardBtn;
    private ImageButton exitBtn;

    /**
     * Create the View and return it.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_menu, container, false);
        setClickListenerOnEasyButton(view);
        setClickListenerOnMediumButton(view);
        setClickListenerOnHardButton(view);
        setClickListenerOnExitButton(view);
        return view;
    }

    /**
     * Set click listener on easy button
     * @param view View
     */
    private void setClickListenerOnEasyButton(View view){
        easyBtn = view.findViewById(R.id.replayBtn);
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new GameFragment(10,0,1));
            }
        });
    }

    /**
     * Set click listener on medium button
     * @param view View
     */
    private void setClickListenerOnMediumButton(View view){
        mediumBtn = view.findViewById(R.id.mediumBtn);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new GameFragment(80,0,1));
            }
        });
    }

    /**
     * Set click listener on hard button
     * @param view View
     */
    private void setClickListenerOnHardButton(View view){
        hardBtn = view.findViewById(R.id.menuBtn);
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new GameFragment(60,0,1));

            }
        });
    }

    /**
     * Set click listener on exit button
     * @param view View
     */
    private void setClickListenerOnExitButton(View view){
        exitBtn = view.findViewById(R.id.refreshBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * Replace fragment in activity_main.xml
     * @param gameFragment GameFragment
     */
    private void replaceFragment(GameFragment gameFragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, gameFragment);
        ft.commit();
    }
}
