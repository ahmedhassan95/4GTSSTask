package hk.com.a4gtsstask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    DatabaseHandler db = null;
    final ArrayList<Note> notes = new ArrayList<>();
    NotesAdapter adapter;

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
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        notes.clear();
        notes.addAll(db.getAllNotes());

        ListView listView = (ListView) view.findViewById(R.id.notes_list);
        adapter = new NotesAdapter(getActivity(),R.layout.list_item,notes);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra("Note", notes.get(i));
                startActivity(intent);

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
