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

public class CreateContactActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnCreate;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnCreate = findViewById(R.id.btnCreate);
        client = new AsyncHttpClient();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String mobile = etMobile.getText().toString();
                if (firstName.length() == 0 || lastName.length() == 0 || mobile.length() == 0) {
                    Toast.makeText(getBaseContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = getIntent();
                    String id = i.getStringExtra("loginId");
                    String apikey = i.getStringExtra("apikey");
                    RequestParams params = new RequestParams();
                    params.add("FirstName", firstName);
                    params.add("LastName", lastName);
                    params.add("Mobile", mobile);
                    params.add("loginId", id);
                    params.add("apikey", apikey);
                    client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/createContact.php", params, new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                finish();
            }
        });
    }
}
