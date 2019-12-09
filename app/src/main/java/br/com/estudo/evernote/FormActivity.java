package br.com.estudo.evernote;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.estudo.evernote.ui.EvernoteApi;
import br.com.estudo.evernote.ui.Note;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormActivity extends AppCompatActivity implements TextWatcher {

    private EditText noteTitle, noteBody;
    boolean toSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        noteTitle = findViewById(R.id.note_title);
        noteBody = findViewById(R.id.note_editor);

        noteTitle.addTextChangedListener(this);
        noteBody.addTextChangedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggleToolbar(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (toSave) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://myevernote.glitch.me")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Note note = new Note();
                note.setTitle(noteTitle.getText().toString());
                note.setBody(noteBody.getText().toString());

                EvernoteApi api = retrofit.create(EvernoteApi.class);
                api.createNote(note).enqueue(new Callback<Note>() {
                    @Override
                    public void onResponse(Call<Note> call, Response<Note> response) {
                        if (response.isSuccessful()) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Note> call, Throwable t) {

                    }
                });


            } else {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleToolbar(@DrawableRes int icon) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(null);
            final Drawable upArrow = getResources().getDrawable(icon);
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            upArrow.setColorFilter(colorFilter);
            supportActionBar.setHomeAsUpIndicator(upArrow);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (noteBody.getText().toString().isEmpty() &&
                noteTitle.getText().toString().isEmpty()){
            toggleToolbar(R.drawable.ic_arrow_back_black_24dp);
            toSave = false;
        } else {
            toggleToolbar(R.drawable.ic_done_black_24dp);
            toSave = true;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
