package hk.com.a4gtsstask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ahmed on 26 May 2017.
 */

public class NotesAdapter extends ArrayAdapter<Note> {
    @Nullable
    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    Context context ;
    List<Note> notes;
    DatabaseHandler db = null;
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public NotesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Note> notes) {
        super(context, resource, notes);
        this.context = context;
        this.notes = notes;
    }


    @Override
    public int getCount() {
        return notes.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        db = new DatabaseHandler(getContext());
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
        }
        final Note note = getItem(position);


        final EditText title = (EditText) view.findViewById(R.id.noteTitle);
        title.setText(note.getTitle());
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                {
                    Call<Note>  apiCall =apiService.updateNote(getItem(position).getId(),title.getText().toString());
                    apiCall.enqueue(new Callback<Note>() {
                        @Override
                        public void onResponse(Call<Note> call, Response<Note> response) {
                            Log.d("Update Note",response.body().getTitle());
                        }

                        @Override
                        public void onFailure(Call<Note> call, Throwable throwable) {

                        }
                    });
                }
            }
        });

        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    if(checkBox.isChecked() && title.getText().toString().length()!= 0) {

                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    }
                    else
                        title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

            }
        });


        final ImageView imageButton = (ImageView) view.findViewById(R.id.delete_note);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Current Note ?");
                builder.setMessage("Are you sure you want to delete note ?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Call<Note> apiCall = apiService.deleteNote(note.getId());
                                Log.d("Note Id",note.getId());

                                apiCall.enqueue(new Callback<Note>() {
                                    @Override
                                    public void onResponse(Call<Note> call, Response<Note> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Note> call, Throwable throwable) {

                                    }
                                });

                                Log.d("Database Before" , String.valueOf(db.getNotesCount())) ;

                                db.deleteNote(note.getId());
                                Log.d("Database After" , String.valueOf(db.getNotesCount())) ;

                                notes.clear();
                                notes.addAll(db.getAllNotes());
                                notifyDataSetChanged();


                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        return view;
    }
}
