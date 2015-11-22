package org.raguenets.festimot;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class NewParty extends AppCompatActivity implements View.OnClickListener {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://www.raguenets.org/festimot");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_party);
        mSocket.connect();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        Button button = (Button) parent.findViewById(R.id.button);

        button.setOnClickListener(this);

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onClick(View v) {

        mSocket.emit("startgame", "");
    }
}
