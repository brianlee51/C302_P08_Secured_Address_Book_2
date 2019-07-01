package sg.edu.rp.c346.c302_p08_secured_address_book_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ArrayList<Contact> alContact;
    private ArrayAdapter<Contact> aaContact;
    private AsyncHttpClient client;

    // TODO (3) Declare loginId and apikey
    String loginId = "";
    String apiKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alContact = new ArrayList<Contact>();
        lvContact = findViewById(R.id.listViewContact);

        aaContact = new ContactAdapter(this, R.layout.contact_row, alContact);
        lvContact.setAdapter(aaContact);
        client = new AsyncHttpClient();

        // TODO (4) Get loginId and apikey from the previous Intent
        Intent intent = getIntent();
        loginId = intent.getStringExtra("id");
        apiKey = intent.getStringExtra("apikey");

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact selectedContact = alContact.get(position);

                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey
                Intent i = new Intent(getBaseContext(), ViewContactDetailsActivity.class);
                i.putExtra("contact_id", selectedContact.getContactId());
                i.putExtra("loginId", loginId);
                i.putExtra("apikey", apiKey);
                startActivity(i);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        alContact.clear();

        // TODO (5) Refresh the main activity with the latest list of contacts by calling getListOfContacts.php
        // What is the web service URL?
        // What is the HTTP method?
        // What parameters need to be provided?
        alContact = new ArrayList<Contact>();
        RequestParams params = new RequestParams();
        params.add("loginId", loginId);
        params.add("apikey", apiKey);
        client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getListOfContacts.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i=0; i<response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);
                        int contactId = jsonObj.getInt("id");
                        String firstName = jsonObj.getString("firstname");
                        String lastName = jsonObj.getString("lastname");
                        String mobile =  jsonObj.getString("mobile");
                        Contact contact = new Contact(contactId, firstName, lastName, mobile);
                        alContact.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                aaContact = new ContactAdapter(getApplicationContext(), R.layout.contact_row, alContact);
                lvContact.setAdapter(aaContact);
            }
        });

    }

    // TODO (6) In the HttpResponseListener for getListOfContacts.php, get all contacts from the results and show in the list


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey
            Intent i = new Intent(getBaseContext(), CreateContactActivity.class);
            i.putExtra("loginId", loginId);
            i.putExtra("apikey", apiKey);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}

