package br.com.estudo.evernote;

import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActioBar = getSupportActionBar();
        if (supportActioBar != null) {
            supportActioBar.setTitle(null);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
            PorterDuffColorFilter filter = new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), SRC_ATOP);

            upArrow.setColorFilter(filter);
            supportActioBar.setHomeAsUpIndicator(upArrow);
            supportActioBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
