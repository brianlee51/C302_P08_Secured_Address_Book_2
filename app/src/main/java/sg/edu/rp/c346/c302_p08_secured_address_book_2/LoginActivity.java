package sg.edu.rp.c346.c302_p08_secured_address_book_2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO (1) When Login button is clicked, call doLogin.php web service to check if the user is able to log in
                // What is the web service URL?
                // What is the HTTP method?
                // What parameters need to be provided?
                if (etUsername.length() == 0 || etPassword.length() == 0) {
                    Toast.makeText(getBaseContext(), "Please key in credentials", Toast.LENGTH_LONG).show();
                } else {
                    client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add("username", etUsername.getText().toString());
                    params.add("password", etPassword.getText().toString());
                    client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/doLogin.php", params, new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                boolean authenticated = response.getBoolean("authenticated");
                                if (authenticated) {
                                    String id = response.getString("id");
                                    String apiKey = response.getString("apikey");
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("apikey", apiKey);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    // TODO (2) Using AsyncHttpClient, check if the user has been authenticated successfully
    // If the user can log in, extract the id and API Key from the JSON object, set them into Intent and start MainActivity Intent.
    // If the user cannot log in, display a toast to inform user that login has failed.

}
