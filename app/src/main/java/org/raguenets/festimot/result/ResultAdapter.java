package org.raguenets.festimot.result;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.raguenets.festimot.R;

import java.util.List;
import java.util.zip.Inflater;

public class ResultAdapter extends ArrayAdapter<Result> {

    private LayoutInflater mInflater;

    public ResultAdapter(Context context, List<Result> objects) {
        super(context, R.layout.row_result, 0, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.row_result, parent, false);
        } else {
            view = convertView;
        }

        try {
            Result result = getItem(position);

            text = (TextView) view.findViewById(R.id.question);
            text.setText(result.getDefinition());

            text = (TextView) view.findViewById(R.id.points);
            text.setText(String.valueOf(result.getPoints()));

            text = (TextView) view.findViewById(R.id.correct_answer);
            text.setText(result.getCorrectAnswer());

            text = (TextView) view.findViewById(R.id.user_answer);
            text.setText(result.getUserAnswer());


        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }


        return view;
    }
}
