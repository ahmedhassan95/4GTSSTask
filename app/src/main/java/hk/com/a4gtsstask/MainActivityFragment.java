package hk.com.a4gtsstask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    DatabaseHandler db = null;
    final ArrayList<Note> notes = new ArrayList<>();
    NotesAdapter adapter;
    private static final String TAG = "GET ALL NOTES";
    public MainActivityFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new DatabaseHandler(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.fragment_main, container, false);



        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Note>> allNotes = apiService.getAllNotes();
        allNotes.enqueue(new Callback<List<Note>>() {

            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                List<Note> notesRetrieved = response.body();
                notes.clear();
                notes.addAll(notesRetrieved);
                db.deleteAllNotes();
                for (Note n: notesRetrieved)
                    db.addNote(n);
                Log.d(TAG, "Number of notes received: " + notesRetrieved.size());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable throwable) {
                notes.clear();
                notes.addAll(db.getAllNotes());
                Log.e(TAG, throwable.toString());
            }

        });

        ListView listView = (ListView) view.findViewById(R.id.notes_list);
        adapter = new NotesAdapter(getActivity(),R.layout.list_item,notes);

        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final EditText editText = new EditText(getActivity());
                editText.setBackground(null);
                builder.setView(editText);
                builder.setTitle("Add New Note");
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Note note = new Note(editText.getText().toString(),false);
                        notes.add(note);
                        adapter.notifyDataSetChanged();

                        final Call<Note>  newNote = apiService.addNote(note.getTitle(),notes.size(),false);
                        final String[] id = {"100"};
                        newNote.enqueue(new Callback<Note>() {
                            @Override
                            public void onResponse(Call<Note> call, Response<Note> response) {
                                id[0] = response.body().getId();

                            }

                            @Override
                            public void onFailure(Call<Note> call, Throwable throwable) {

                            }
                        });
                        note.setId(id[0]);
                        db.addNote(note);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.create();
                builder.show();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notes.clear();
        notes.addAll(db.getAllNotes());
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_settings)
        {
            Toast.makeText(getContext(),"Settings Pressed",Toast.LENGTH_SHORT).show();

        }
        else if (item.getItemId()==R.id.action_deleteAll)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete All Notes ?");
            builder.setMessage("Are you sure you want to delete all notes ?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Delete All",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            db.deleteAllNotes();
                            onResume();
                        }
                    });

            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }
        return true;
    }
}
