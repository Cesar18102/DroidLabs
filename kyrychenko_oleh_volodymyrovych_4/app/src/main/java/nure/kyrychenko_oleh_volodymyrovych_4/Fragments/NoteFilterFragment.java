package nure.kyrychenko_oleh_volodymyrovych_4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnFiltersAppliedListener;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.FilterSettings;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.Importance;
import nure.kyrychenko_oleh_volodymyrovych_4.R;

public class NoteFilterFragment extends Fragment {
    public static final String NOTE_FILTER_FRAGMENT_KEY = "NOTE_FILTER_FRAGMENT";

    private FilterSettings filterSettings = new FilterSettings();

    private EditText contentFilterEditor;

    private CheckBox lowImportanceCheckBox;
    private CheckBox mediumImportanceCheckBox;
    private CheckBox highImportanceCheckBox;

    public FilterSettings getFilterSettings() {
        return filterSettings;
    }

    public NoteFilterFragment() {

    }

    public static NoteFilterFragment createFilterFragment() {
        return new NoteFilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_filter, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FilterSettings.ARG_FILTER_SETTINGS, filterSettings);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            filterSettings = (FilterSettings) savedInstanceState.getParcelable(FilterSettings.ARG_FILTER_SETTINGS);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentFilterEditor = view.findViewById(R.id.content_filter_entry);
        lowImportanceCheckBox = view.findViewById(R.id.importance_low);
        mediumImportanceCheckBox = view.findViewById(R.id.importance_medium);
        highImportanceCheckBox = view.findViewById(R.id.importance_high);

        Button applyFiltersButton = (Button)view.findViewById(R.id.apply_filters_button);
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilters();
            }
        });
    }

    public void applyFilters() {
        filterSettings.setContentFilter(contentFilterEditor.getText().toString());

        ArrayList<Importance> importanceFilters = new ArrayList<Importance>();

        if(lowImportanceCheckBox.isChecked()) {
            importanceFilters.add(Importance.LOW);
        }

        if(mediumImportanceCheckBox.isChecked()) {
            importanceFilters.add(Importance.MEDIUM);
        }

        if(highImportanceCheckBox.isChecked()) {
            importanceFilters.add(Importance.HIGH);
        }

        filterSettings.setImportanceFilter(importanceFilters.toArray(new Importance[0]));
        ((OnFiltersAppliedListener)getActivity()).onFiltersApplied();
    }
}