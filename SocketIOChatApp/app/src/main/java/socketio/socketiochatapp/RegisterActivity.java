package socketio.socketiochatapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class RegisterActivity extends Activity {

    protected Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            mSocket = IO.socket("http://192.168.0.8:3000/");
            mSocket.connect();
            mSocket.on("connection", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                }
            });
        } catch (URISyntaxException e) {
            Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }

        Button registerBtn = (Button)findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText)findViewById(R.id.editText);
                String username = editText.getText().toString();
                registerUser(username);
            }
        });

        mSocket.on("echo", new Emitter.Listener(){

            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //JSONObject data = (JSONObject) args[0];
                        String username;
                        try {
                            //username = data.getString("username").toString();
                            Toast.makeText(getApplicationContext(), (String)args[0], Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "Echo error", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }

    private void registerUser(String username)
    {

        JSONObject sendRegister = new JSONObject();
        try{
            sendRegister.put("username",username);
            mSocket.emit("register", sendRegister);
        }catch(JSONException e){
            Toast.makeText(getApplicationContext(), "JSON error!", Toast.LENGTH_LONG).show();
        }
    }

}
