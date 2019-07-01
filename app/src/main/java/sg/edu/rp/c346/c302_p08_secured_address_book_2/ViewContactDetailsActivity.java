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

public class ViewContactDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnUpdate, btnDelete;
    private int contactId;
    private AsyncHttpClient client;

    String loginID = "";
    String apikey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String mobile = etMobile.getText().toString();
                if (firstName.length() == 0 || lastName.length() == 0 || mobile.length() == 0) {
                    Toast.makeText(getBaseContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = getIntent();
                    apikey = intent.getStringExtra("apikey");
                    loginID = intent.getStringExtra("loginId");
                    RequestParams params = new RequestParams();
                    params.add("id", String.valueOf(contactId));
                    params.add("FirstName", firstName);
                    params.add("LastName", lastName);
                    params.add("Mobile", mobile);
                    params.add("loginId", loginID);
                    params.add("apikey", apikey);
                    client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/updateContact.php", params, new JsonHttpResponseHandler() {
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String mobile = etMobile.getText().toString();
                if (firstName.length() == 0 || lastName.length() == 0 || mobile.length() == 0) {
                    Toast.makeText(getBaseContext(), "Cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = getIntent();
                    apikey = intent.getStringExtra("apikey");
                    loginID = intent.getStringExtra("loginId");
                    RequestParams params = new RequestParams();
                    params.add("id", String.valueOf(contactId));
                    params.add("FirstName", firstName);
                    params.add("LastName", lastName);
                    params.add("Mobile", mobile);
                    params.add("loginId", loginID);
                    params.add("apikey", apikey);
                    client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/deleteContact.php", params, new JsonHttpResponseHandler() {
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

    @Override
    protected void onResume() {
        super.onResume();

        // Code for step 1 start
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);
        apikey = intent.getStringExtra("apikey");
        loginID = intent.getStringExtra("loginId");
        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(contactId));
        params.add("loginId", loginID);
        params.add("apikey", apikey);
        client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getContactDetails.php", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String firstName = response.getString("firstname");
                    String lastName = response.getString("lastname");
                    String mobile = response.getString("mobile");
                    etMobile.setText(mobile);
                    etFirstName.setText(firstName);
                    etLastName.setText(lastName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
