package org.raguenets.festimot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

public class NewParty extends AppCompatActivity {
    private String clientId;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://www.raguenets.org/festimot");
        } catch (URISyntaxException e) {
            Log.e("NewParty", Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_party);

        mSocket.on("ready", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] instanceof JSONObject) {
                    JSONObject object = (JSONObject) args[0];

                    try {
                        clientId = (String) object.get("clientId");
                    } catch (JSONException e) {
                        Log.e("mSocket", object.toString() + "\\\\" + e.getLocalizedMessage());
                    }
                }

                NewParty.this.log("ready", args);
            }
        });

        mSocket.on("startgame", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("startgame", Arrays.toString(args));


                NewParty.this.log("startgame", args);
            }
        });

        mSocket.on("definition", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d("definition", Arrays.toString(args));
                NewParty.this.log("definition", args);
                final Object[] arg = args;
                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.newDefinition(arg);
                    }
                });
            }
        });

        mSocket.on("indice", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("indice", Arrays.toString(args));
                NewParty.this.log("indice", args);
                final Object[] arg = args;
                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.newIndice(arg);
                    }
                });
            }
        });

        mSocket.connect();

        mSocket.emit("clientconnect", "");
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject msg = new JSONObject();

                try {
                    msg.put("clientId", clientId);
                } catch (JSONException e) {
                    Log.e("mSocket", e.getLocalizedMessage());
                }

                mSocket.emit("startgame", msg);
            }});

        final Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                JSONObject msg = new JSONObject();
                EditText eT = (EditText) findViewById(R.id.reponse);
                try {
                    msg.put("answer", eT.getText());
                } catch (JSONException e) {
                    Log.e("mSocket", e.getLocalizedMessage());
                }

                mSocket.emit("answer", msg);
                send.setEnabled(false);
            }
        });
    }

    private void newIndice(Object[] args) {
        TextView textView = (TextView) findViewById(R.id.indice);
        textView.append(Arrays.toString(args));
    }

    private void newDefinition(Object[] args) {
        TextView textView = (TextView) findViewById(R.id.definition);
        textView.append(Arrays.toString(args));

        Button send = (Button) findViewById(R.id.send);
        send.setEnabled(true);
    }

    private void log(final String message, final Object[] args) {
        final TextView textView = (TextView) findViewById(R.id.logs);
        textView.post(new Runnable() {
                          @Override
                          public void run() {
                              textView.append(">>>" + message + ":" + Arrays.toString(args) + System.getProperty("line.separator"));
                          }
                      }
        );
    }
}
