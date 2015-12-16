package org.raguenets.festimot;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import org.json.JSONObject;
import org.raguenets.festimot.result.Result;
import org.raguenets.festimot.result.ResultAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView) findViewById(R.id.resultsList);

        Object[] resultsObject = getIntent().getParcelableArrayExtra("Results");
        Result[] results = new Result[resultsObject.length];
        System.arraycopy(resultsObject, 0, results, 0, results.length);
        listView.setAdapter(new ResultAdapter(getBaseContext(), Arrays.asList(results)));
    }

}
