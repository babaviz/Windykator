package com.mateusz.windykator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mateusz.windykator.R;
import com.mateusz.windykator.pojos.Person;


public class PersonActivity extends ActionBarActivity {

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        person = (Person) getIntent().getSerializableExtra("person");

        if (person != null) {
            ((TextView) (findViewById(R.id.name))).setText(person.getName());
            ((TextView) (findViewById(R.id.surname))).setText(person.getSurname());
            ((TextView) (findViewById(R.id.phone))).setText(person.getPhoneNo());
            ((CheckBox) (findViewById(R.id.ifSendMess))).setChecked(person.isSendMessage());
        }
        else
            person = new Person();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        String name = ((TextView) (findViewById(R.id.name))).getText().toString();
        String surname = ((TextView) (findViewById(R.id.surname))).getText().toString();
        String phone = ((TextView) (findViewById(R.id.phone))).getText().toString();
        boolean sendMess = ((CheckBox) (findViewById(R.id.ifSendMess))).isChecked();

        if (name != null && surname != null) {
            if (!name.isEmpty() && !surname.isEmpty()) {

                person.setName(name);
                person.setSurname(surname);
                person.setPhoneNo(phone);
                person.setSendMessage(sendMess);

                Intent intent = getIntent();
                intent.putExtra("person", person);
                this.setResult(RESULT_OK, intent);
                this.finish();

            }
        }
    }
}
