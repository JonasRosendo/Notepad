package br.com.jonasrosendo.notepad.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.jonasrosendo.notepad.R;
import br.com.jonasrosendo.notepad.adapters.RecyclerAdapter;
import br.com.jonasrosendo.notepad.models.Note;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference noteDB;
    private List<Note> noteList = new ArrayList<>();
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteDB = FirebaseDatabase.getInstance().getReference("note");

        recyclerView = findViewById(R.id.noteList);
        actionButton = findViewById(R.id.fab_add_note);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveNotes();
    }

    private void retrieveNotes(){

        noteList.clear();
        noteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot notes : dataSnapshot.getChildren()){
                    Note note = notes.getValue(Note.class);
                    noteList.add(note);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), noteList);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.notes_download_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
