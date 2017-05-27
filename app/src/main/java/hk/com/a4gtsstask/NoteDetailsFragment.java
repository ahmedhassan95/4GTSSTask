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
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoteDetailsFragment extends Fragment {

    DatabaseHandler db = null;
    EditText title,body;

    Note note = null;
    public NoteDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getActivity().getTitle().equals("Note Details"))
            note = (Note) getActivity().getIntent().getSerializableExtra("Note");
        db = new DatabaseHandler(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_note_details, container, false);

        title = (EditText) view.findViewById(R.id.note_title);
        body = (EditText) view.findViewById(R.id.note_body);

        if (note!=null)
        {
            title.setText(note.getTitle());
            body.setText(note.getBody());
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        String nTitle = title.getText().toString();
        String nBody = body.getText().toString();

        //INSERT into Database
        if (id==R.id.action_create)
        {
            if (nTitle.length()==0)
                Toast.makeText(getActivity(),"Title Cannot be Empty",Toast.LENGTH_SHORT).show();
            else
            {
                Note note = new Note(nTitle, nBody);
                db.addNote(note);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }


        //UPDATE in Database
        else if (id == R.id.action_update)
        {
            if (nTitle.length()==0)
                Toast.makeText(getActivity(),"Title Cannot be Empty",Toast.LENGTH_SHORT).show();
            else
            {
                note.setTitle(nTitle);
                note.setBody(nBody);
                db.updateNote(note);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

        //DELETE From Database
        else if (id == R.id.action_delete)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Note ?");
            builder.setMessage("Are you sure you want to delete this note ?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            db.deleteNote(note);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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
