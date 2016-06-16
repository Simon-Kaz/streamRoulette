package org.simonsays.strimroulette.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.simonsays.strimroulette.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    TextView seekbar_textView;
    SeekBar seekBar;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setupLanguagesSpinner(rootView);
        setupSeekBar(rootView);

        return rootView;
    }

    private void setupLanguagesSpinner(View rootView) {
        // Set the language array for the spinner/dropdown
        Spinner spinner = (Spinner) rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.languages_array, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(langAdapter);
        // using index in the array, set default to English
        spinner.setSelection(2, true);
    }

    public void setupSeekBar(View rootView) {
        seekBar = (SeekBar) rootView.findViewById(R.id.stream_count_seek_bar);
        seekbar_textView = (TextView) rootView.findViewById(R.id.Seekbar_value_textview);

        seekBar.setMax(8);
        // set default value to 2000
        seekBar.setProgress(7);
        seekbar_textView.setText(String.valueOf(2000));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        seekbar_textView.setText(String.valueOf(10));
                        break;
                    case 1:
                        seekbar_textView.setText(String.valueOf(25));
                        break;
                    case 2:
                        seekbar_textView.setText(String.valueOf(50));
                        break;
                    case 3:
                        seekbar_textView.setText(String.valueOf(100));
                        break;
                    case 4:
                        seekbar_textView.setText(String.valueOf(200));
                        break;
                    case 5:
                        seekbar_textView.setText(String.valueOf(500));
                        break;
                    case 6:
                        seekbar_textView.setText(String.valueOf(1000));
                        break;
                    case 7:
                        seekbar_textView.setText(String.valueOf(2000));
                        break;
                    case 8:
                        seekbar_textView.setText("MAX");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
