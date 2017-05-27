package hk.com.a4gtsstask;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        TextView title = (TextView) view.findViewById(R.id.list_item_title);
        title.setText(note.getTitle());

        TextView details = (TextView) view.findViewById(R.id.list_item_details);
        details.setText(note.getBody());


        return view;
    }
}
