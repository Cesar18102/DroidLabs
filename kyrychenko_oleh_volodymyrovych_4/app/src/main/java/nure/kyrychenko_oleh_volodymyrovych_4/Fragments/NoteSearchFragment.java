package nure.kyrychenko_oleh_volodymyrovych_4.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnSearchChangedListener;
import nure.kyrychenko_oleh_volodymyrovych_4.R;

public class NoteSearchFragment extends Fragment {
    public final static String NOTE_SEARCH_FRAGMENT_KEY = "NOTE_SEARCH_FRAGMENT";

    private String titleSearch;
    private String descriptionSearch;

    private final static String TITLE_SEARCH_KEY = "TITLE_SEARCH";
    private final static String DESCRIPTION_SEARCH_KEY = "DESCRIPTION_SEARCH";

    public String getTitleSearch() {
        return titleSearch;
    }

    public String getDescriptionSearch() {
        return descriptionSearch;
    }

    public NoteSearchFragment() {

    }

    public static NoteSearchFragment createSearchFragment() {
        return new NoteSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_search, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE_SEARCH_KEY, titleSearch);
        outState.putString(DESCRIPTION_SEARCH_KEY, descriptionSearch);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            titleSearch = savedInstanceState.getString(TITLE_SEARCH_KEY);
            descriptionSearch = savedInstanceState.getString(DESCRIPTION_SEARCH_KEY);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Fragment fragment = this;

        EditText titleSearchEntry = view.findViewById(R.id.title_search_entry);
        EditText descriptionSearchEntry = view.findViewById(R.id.description_search_entry);
        Button doneButton = view.findViewById(R.id.search_done);

        OnSearchChangedListener listener = (OnSearchChangedListener)getActivity();

        titleSearchEntry.addTextChangedListener(new TitleSearchChangedNotifier(listener));
        descriptionSearchEntry.addTextChangedListener(new DescriptionSearchChangedNotifier(listener));

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
        });
    }

    private class DescriptionSearchChangedNotifier extends SearchChangedNotifier {
        public DescriptionSearchChangedNotifier(OnSearchChangedListener listener) {
            super(listener);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            descriptionSearch = s.toString();
            super.onTextChanged(s, start, before, count);
        }
    }

    private class TitleSearchChangedNotifier extends SearchChangedNotifier {
        public TitleSearchChangedNotifier(OnSearchChangedListener listener) {
            super(listener);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            titleSearch = s.toString();
            super.onTextChanged(s, start, before, count);
        }
    }

    private class SearchChangedNotifier implements TextWatcher {
        private OnSearchChangedListener listener;
        public SearchChangedNotifier(OnSearchChangedListener listener) {
            this.listener = listener;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            listener.onSearchChanged();
        }
    }
}