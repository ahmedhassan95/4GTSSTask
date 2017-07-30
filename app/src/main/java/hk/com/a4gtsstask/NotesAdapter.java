package hk.com.a4gtsstask;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
        }
        Note note = getItem(position);


        final EditText title = (EditText) view.findViewById(R.id.noteTitle);
        title.setText(note.getTitle());

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



        return view;
    }
}
