package nure.kyrychenko_oleh_volodymyrovych_4.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import nure.kyrychenko_oleh_volodymyrovych_4.Controls.NoteView;
import nure.kyrychenko_oleh_volodymyrovych_4.DataSource.INoteDataSource;
import nure.kyrychenko_oleh_volodymyrovych_4.Fragments.NoteFilterFragment;

import nure.kyrychenko_oleh_volodymyrovych_4.Fragments.NoteSearchFragment;
import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnFiltersAppliedListener;
import nure.kyrychenko_oleh_volodymyrovych_4.Listeners.OnSearchChangedListener;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.FilterSettings;
import nure.kyrychenko_oleh_volodymyrovych_4.Model.INote;
import nure.kyrychenko_oleh_volodymyrovych_4.R;
import nure.kyrychenko_oleh_volodymyrovych_4.Util.Util;

public class MainActivity extends AppCompatActivity implements OnFiltersAppliedListener, OnSearchChangedListener {
    private NoteFilterFragment filterFragment;
    private NoteSearchFragment searchFragment;

    private ArrayList<NoteView> noteViews = new ArrayList<NoteView>();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null && filterFragment == null) {
            filterFragment = NoteFilterFragment.createFilterFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.filter_settings_dialog_container, filterFragment)
                    .hide(filterFragment)
                    .commit();
        }

        if(savedInstanceState == null && searchFragment == null) {
            searchFragment = NoteSearchFragment.createSearchFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.search_settings_dialog_container, searchFragment)
                    .hide(searchFragment)
                    .commit();
        }

        ImageButton filter = (ImageButton)findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().show(filterFragment).commit();
            }
        });

        ImageButton search = (ImageButton)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().show(searchFragment).commit();
            }
        });

        final Context context = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNoteIntent = new Intent(context, AddNoteActivity.class);
                addNoteIntent.putExtra(AddNoteActivity.MODE, AddNoteActivity.MODE_ADD);
                startActivity(addNoteIntent);
            }
        });

        INoteDataSource noteDataSource = Util.getNoteDataSource(this);

        if(noteDataSource != null) {
            buildList(noteDataSource.getAllNotes());
        }
    }

    private void buildList(INote[] notes) {
        if(notes == null)
            return;

        LinearLayout content = (LinearLayout)findViewById(R.id.content);

        for(INote note : notes) {
            NoteView noteView = new NoteView(this);
            noteView.setNote(note);

            noteViews.add(noteView);
            content.addView(noteView);
            registerForContextMenu(noteView);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v instanceof NoteView) {
            onCreateNoteViewContextMenu(menu, (NoteView)v);
        }
    }

    private void onCreateNoteViewContextMenu(ContextMenu menu, final NoteView noteView) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_view_context_menu, menu);

        MenuItem editItem = (MenuItem)menu.findItem(R.id.edit);
        MenuItem deleteItem = (MenuItem)menu.findItem(R.id.delete);

        final LinearLayout content = (LinearLayout)findViewById(R.id.content);
        final Activity context = this;

        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent editNoteIntent = new Intent(context, AddNoteActivity.class);

                editNoteIntent.putExtra(AddNoteActivity.MODE, AddNoteActivity.MODE_EDIT);
                editNoteIntent.putExtra(AddNoteActivity.NOTE_ID, noteView.getNote().getId());

                startActivity(editNoteIntent);
                return true;
            }
        });

        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                content.removeView(noteView);
                noteViews.remove(noteView);

                INoteDataSource noteDataSource = Util.getNoteDataSource(context);

                INote[] notes = noteDataSource.getAllNotes();
                INote[] updated = new INote[notes.length - 1];

                String noteId = noteView.getNote().getId();
                int offset = 0;

                for(int i = 0; i < notes.length; ++i) {
                    if (notes[i].getId().equals(noteId)) {
                        offset = -1;
                        continue;
                    }
                    updated[i + offset] = notes[i];
                }

                try {
                    noteDataSource.saveAllNotes(updated);
                } catch (IOException e) {
                    Toast.makeText(context, "Failed to save new node", Toast.LENGTH_LONG);
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, NoteFilterFragment.NOTE_FILTER_FRAGMENT_KEY, filterFragment);
        getSupportFragmentManager().putFragment(outState, NoteSearchFragment.NOTE_SEARCH_FRAGMENT_KEY, searchFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        filterFragment = (NoteFilterFragment)getSupportFragmentManager().getFragment(savedInstanceState, NoteFilterFragment.NOTE_FILTER_FRAGMENT_KEY);
        searchFragment = (NoteSearchFragment)getSupportFragmentManager().getFragment(savedInstanceState, NoteSearchFragment.NOTE_SEARCH_FRAGMENT_KEY);

        filterFragment.applyFilters();
        onSearchChanged();
    }

    @Override
    public void onFiltersApplied() {
        FilterSettings filterSettings = filterFragment.getFilterSettings();

        for(NoteView noteView : noteViews) {
            INote note = noteView.getNote();
            noteView.setVisibility(
                    filterSettings.isNoteMatches(note) ? View.VISIBLE : View.GONE
            );
        }

        getSupportFragmentManager().beginTransaction().hide(filterFragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onSearchChanged() {
        if(searchFragment == null) {
            return;
        }

        String titleSearch = searchFragment.getTitleSearch();
        String descriptionSearch = searchFragment.getDescriptionSearch();

        for(NoteView noteView : noteViews) {
            if(noteView.getVisibility() != View.VISIBLE) {
                continue;
            }

            INote note = noteView.getNote();

            String title = note.getTitle();
            String description = note.getDescription();

            boolean titleMatch = title != null && !title.isEmpty() && titleSearch != null && !titleSearch.isEmpty() && title.contains(titleSearch);
            boolean descriptionMatch = description != null && !description.isEmpty() && descriptionSearch != null && !descriptionSearch.isEmpty() && description.contains(descriptionSearch);

            boolean match = titleMatch || descriptionMatch;
            int color = match ? Color.argb(50,255, 0,0) : Color.argb(255, 255, 255, 255);

            noteView.setBackgroundColor(color);
        }
    }

    @Override
    public void onBackPressed() {
        if(filterFragment != null && filterFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(filterFragment).commit();
        } else if(searchFragment != null && searchFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
        } else {
            super.onBackPressed();
        }
    }
}