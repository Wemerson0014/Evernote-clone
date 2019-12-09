package br.com.estudo.evernote;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.estudo.evernote.ui.EvernoteApi;
import br.com.estudo.evernote.ui.Note;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        List<Note> notes = new ArrayList<>();

        adapter = new NoteAdapter(notes);
        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(divider);
        
        request();
    }

    private void request() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://myevernote.glitch.me")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EvernoteApi api = retrofit.create(EvernoteApi.class);
        api.listNotes().enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.isSuccessful()){
                    List<Note> notes = response.body();

                    if (notes != null){
                        adapter.notes.clear();
                        adapter.notes.addAll(notes);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Log.i("t", t.getMessage());
            }
        });
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteView> {

        private final List<Note> notes;

        private NoteAdapter(List<Note> notes) {
            this.notes = notes;
        }

        @NonNull
        @Override
        public NoteView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item_note, parent, false);
            return new NoteView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteView holder, int position) {
            Note note = notes.get(position);
            holder.bind(note.getTitle(), note.getDesc(), note.getDate());
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }
    }

    private static class NoteView extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDesc, noteDate;

        public NoteView(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDesc = itemView.findViewById(R.id.note_desc);
            noteDate = itemView.findViewById(R.id.note_date);
        }

        void bind(String title, String desc, String date) {
            noteTitle.setText(title);
            noteDesc.setText(desc);
            noteDate.setText(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
