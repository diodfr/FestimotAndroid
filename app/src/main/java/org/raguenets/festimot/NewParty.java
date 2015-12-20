package org.raguenets.festimot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.raguenets.festimot.result.Result;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class NewParty extends AppCompatActivity {
    private String clientId;

    private Socket mSocket;

    {
        reset(new Handler() {
            @Override
            public void close() {

            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord record) {
                Log.i("SocketIO", record.getMessage());
            }
        });

        Logger.getLogger(Socket.class.getName()).setLevel(Level.FINEST);
        try {
            mSocket = IO.socket("https://www.raguenets.org/festimot");
        } catch (URISyntaxException e) {
            Log.e("NewParty", Log.getStackTraceString(e));
        }
    }

    public static void reset(Handler rootHandler) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        LogManager.getLogManager().getLogger("").addHandler(rootHandler);
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

        mSocket.on("solution", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("indice", Arrays.toString(args));
                NewParty.this.log("indice", args);
                final Object[] arg = args;
                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.newSolution(arg);
                    }
                });
            }
        });

        mSocket.on("points", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("points", Arrays.toString(args));
                NewParty.this.log("points", args);
                final Object[] arg = args;
                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.updatePoints(arg);
                    }
                });
            }
        });

        mSocket.on("scores", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("scores", Arrays.toString(args));
                NewParty.this.log("scores", args);
                final Object[] arg = args;
                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.updateScore(arg);
                    }
                });
            }
        });

        mSocket.on("gameended", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("end", Arrays.toString(args));
                NewParty.this.log("end", args);

                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.displayScores(args);
                    }
                });
            }
        });

        mSocket.on("time", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("time", Arrays.toString(args));
                NewParty.this.log("time", args);

                NewParty.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewParty.this.updateChrono(args);
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

        final Button next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {

                JSONObject msg = new JSONObject();

                mSocket.emit("nextindice", msg);
            }
        });
    }

    private void updateChrono(Object[] args) {
        TextView chronoTextView = (TextView) findViewById(R.id.time);
        JSONObject msg = (JSONObject) args[0];
        try {
            chronoTextView.setText(String.valueOf(msg.get("chrono")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePoints(Object[] arg) {
        TextView scoreTextView = (TextView) findViewById(R.id.score);
        TextView bestScoreTextView = (TextView) findViewById(R.id.bestScore);
        JSONObject msg = (JSONObject) arg[0];
        try {
            scoreTextView.setText(String.valueOf(msg.get("score")));
            bestScoreTextView.setText(String.valueOf(msg.get("best")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayScores(Object[] args) {
        final Intent intent = new Intent(this, Results.class);

        final JSONObject results = (JSONObject) args[0];
        intent.putExtra("Results", createResults(results));
        try {
            intent.putExtra("Score", results.getInt("score"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    @NonNull
    private Result[] createResults(JSONObject arg) {
        List<Result> results = new ArrayList<>();
        try {
            JSONArray records = arg.getJSONArray("records");

            for (int i=0; i < records.length(); i++) {
                JSONObject record = (JSONObject) records.get(i);
                results.add(new Result(record.getString("definition"), record.getString("mot"), record.getString("answer"), record.getInt("points")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results.toArray(new Result[results.size()]);
    }

    private void updateScore(Object[] arg) {
        TextView scoreTextView = (TextView) findViewById(R.id.score);
        TextView bestScoreTextView = (TextView) findViewById(R.id.bestScore);
        JSONObject msg = (JSONObject) arg[0];
        try {
            scoreTextView.setText(String.valueOf(msg.get("score")));
            bestScoreTextView.setText(String.valueOf(msg.get("best")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void newIndice(Object[] args) {
        TextView textView = (TextView) findViewById(R.id.indice);
        JSONObject msg = (JSONObject) args[0];
        try {
            textView.setText((String) msg.get("indiceText"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void newSolution(Object[] args) {
        TextView textView = (TextView) findViewById(R.id.indice);
        JSONObject msg = (JSONObject) args[0];
        try {
            textView.setText((String) msg.get("solution"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void newDefinition(Object[] args) {
        TextView textView = (TextView) findViewById(R.id.definition);
        TextView definition_number = (TextView) findViewById(R.id.definition_number);
        TextView indice = (TextView) findViewById(R.id.indice);

        EditText reponse = (EditText) findViewById(R.id.reponse);
        JSONObject msg = (JSONObject) args[0];
        try {
            textView.setText((String) msg.get("definition"));
            definition_number.setText(String.valueOf(msg.get("current")));
            indice.setText(new String(new char[(int) msg.get("taille")]).replace("\0", "_"));
            reponse.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button send = (Button) findViewById(R.id.send);
        send.setEnabled(true);
        Button next = (Button) findViewById(R.id.next);
        next.setEnabled(true);
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
