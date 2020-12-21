package nure.kyrychenko_oleh_volodymyrovych_5.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.UUID;

import nure.kyrychenko_oleh_volodymyrovych_5.Controls.NoteView;
import nure.kyrychenko_oleh_volodymyrovych_5.DataSource.NoteSqlDataSource;
import nure.kyrychenko_oleh_volodymyrovych_5.DataSource.NoteSqlHelper;
import nure.kyrychenko_oleh_volodymyrovych_5.Listeners.OnAsyncNoteQueryDoneListener;
import nure.kyrychenko_oleh_volodymyrovych_5.Listeners.OnTaskCompletedListener;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncNoteQueryResult;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.INote;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.Importance;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.Note;
import nure.kyrychenko_oleh_volodymyrovych_5.R;
import nure.kyrychenko_oleh_volodymyrovych_5.Util.ImageSelector;

public class AddNoteActivity extends Activity implements OnTaskCompletedListener<Bitmap>, OnAsyncNoteQueryDoneListener {
    public static final String MODE = "nure.kyrychenko_oleh_volodymyrovych_5.MODE";
    public static final String NOTE_ID = "nure.kyrychenko_oleh_volodymyrovych_5.NOTE_ID";

    public static final String MODE_EDIT = "EDIT";
    public static final String MODE_ADD = "ADD";

    private INote note = new Note();

    private EditText title;
    private EditText description;

    private RadioButton lowImportance;
    private RadioButton mediumImportance;
    private RadioButton highImportance;

    private EditText imageUrl;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private boolean isAdd;

    private NoteSqlHelper helper;
    private NoteSqlDataSource dataSource;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        helper = new NoteSqlHelper(this);
        dataSource = new NoteSqlDataSource(helper);

        title = (EditText)findViewById(R.id.title);
        description = (EditText)findViewById(R.id.description);

        lowImportance = (RadioButton)findViewById(R.id.importance_low);
        mediumImportance = (RadioButton)findViewById(R.id.importance_medium);
        highImportance = (RadioButton)findViewById(R.id.importance_high);

        imageUrl = (EditText)findViewById(R.id.img_url_editor);
        datePicker = (DatePicker)findViewById(R.id.date_picker);
        timePicker = (TimePicker)findViewById(R.id.time_picker);

        Intent intent = getIntent();
        isAdd = intent.getStringExtra(MODE).equals(MODE_ADD);

        connectListeners();

        if(isAdd) {
            note.setDestinationDate(new Date(
                    datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth()
            ));

            note.setDestinationTime(
                    new Date(
                            0, 0, 0,
                            timePicker.getHour(),
                            timePicker.getMinute(), 0
                    )
            );
        } else {
            ((Button)findViewById(R.id.add_note)).setText(R.string.save_note);
            String editedId = intent.getStringExtra(NOTE_ID);
            dataSource.getByIdAsync(editedId, this);
        }

        updateNotePreview();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void connectListeners() {
        title.addTextChangedListener(new NoteTextFieldsWatcher());
        description.addTextChangedListener(new NoteTextFieldsWatcher());

        lowImportance.setOnCheckedChangeListener(new OnImportanceChangedListener());
        mediumImportance.setOnCheckedChangeListener(new OnImportanceChangedListener());
        highImportance.setOnCheckedChangeListener(new OnImportanceChangedListener());

        imageUrl.addTextChangedListener(new NoteUrlWatcher());
        datePicker.setOnDateChangedListener(new DatePickerValueChangedListener());
        timePicker.setOnTimeChangedListener(new TimePickerValueChangedListener());
    }

    public void onSelectImageFromGalleryButtonClicked(View v) {
        ImageSelector.selectImage(this);
    }

    public void onAddNoteButtonClicked(View v) {
        if(isAdd) {
            Date creationDateTime = new Date();
            creationDateTime.setYear(creationDateTime.getYear() + 1900);
            note.setCreationDateTime(creationDateTime);
            note.setId(UUID.randomUUID().toString());

            dataSource.addNote(note);
        } else {
            dataSource.updateNote(note);
        }

        Intent mainActivity = new Intent(this, MainActivity.class);
        navigateUpTo(mainActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri localImageUri = ImageSelector.checkResult(requestCode, resultCode, data);

        if(localImageUri != null) {
            note.setImageUri(localImageUri.toString());
            updateNotePreview();
        }
    }

    @Override
    public void onTaskCompleted(Bitmap data) {
        if(data == null)
            return;

        EditText webUrlEdit = (EditText)findViewById(R.id.img_url_editor);
        note.setImageUri(webUrlEdit.getText().toString());
        updateNotePreview();
    }

    private void updateNotePreview() {
        NoteView noteView = (NoteView) findViewById(R.id.note_preview);
        noteView.setNote(note);
    }

    private Importance getImportance() {
        if(highImportance.isChecked())
            return Importance.HIGH;
        else if(mediumImportance.isChecked())
            return Importance.MEDIUM;
        else if(lowImportance.isChecked())
            return Importance.LOW;
        else
            return null;
    }

    @Override
    public void handleNoteQueryFinish(AsyncNoteQueryResult queryResult) {

        switch (queryResult.getQueryType()) {
            case GET_BY_ID:
                note = queryResult.getGetByIdResult();

                title.setText(note.getTitle());
                description.setText(note.getDescription());

                if(note.getImportance() != null) {
                    switch (note.getImportance()) {
                        case LOW: lowImportance.setChecked(true); break;
                        case MEDIUM: mediumImportance.setChecked(true); break;
                        case HIGH: highImportance.setChecked(true); break;
                    }
                }

                String imageUri = note.getImageUri();
                if(imageUri != null) {
                    if (imageUri.startsWith("http://") || imageUri.startsWith("https://")) {
                        imageUrl.setText(imageUri);
                    }
                }

                Date destDate = note.getDestinationDate();
                datePicker.updateDate(
                        destDate.getYear(),
                        destDate.getMonth(),
                        destDate.getDate()
                );

                Date destTime = note.getDestinationTime();
                timePicker.setHour(destTime.getHours());
                timePicker.setMinute(destTime.getMinutes());
                break;
        }

        findViewById(R.id.progress_wrapper).setVisibility(View.INVISIBLE);
    }

    @Override
    public void handleNoteQueryStart() {
        findViewById(R.id.progress_wrapper).setVisibility(View.VISIBLE);
    }

    private class NoteTextFieldsWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

        @Override
        public void afterTextChanged(Editable s) {  }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            note.setTitle(title.getText().toString());
            note.setDescription(description.getText().toString());
            updateNotePreview();
        }
    }

    private class OnImportanceChangedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            note.setImportance(getImportance());
            updateNotePreview();
        }
    }

    private class NoteUrlWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                Uri uri = Uri.parse(s.toString());
                note.setImageUri(uri.toString());
                updateNotePreview();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class DatePickerValueChangedListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            note.setDestinationDate(
                    new Date(year, monthOfYear, dayOfMonth)
            );
            updateNotePreview();
        }
    }

    private class TimePickerValueChangedListener implements TimePicker.OnTimeChangedListener {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            note.setDestinationTime(
                    new Date(
                            0, 0, 0, hourOfDay, minute, 0
                    )
            );
            updateNotePreview();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}