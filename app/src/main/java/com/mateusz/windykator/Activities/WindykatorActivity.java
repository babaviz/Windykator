package com.mateusz.windykator.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mateusz.windykator.R;
import com.mateusz.windykator.database.DBHandler;
import com.mateusz.windykator.pojos.Debt;
import com.mateusz.windykator.pojos.Person;

import java.util.ArrayList;


public class WindykatorActivity extends ActionBarActivity {

    private ArrayAdapter adapter;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windykator);

        person = (Person) getIntent().getSerializableExtra("person");

        ((TextView) findViewById(R.id.person)).setText(person.toString());

        if (person.isSendMessage())
            ((TextView) findViewById(R.id.number)).setText(person.getPhoneNo());
        else
            findViewById(R.id.number).setVisibility(View.INVISIBLE);

        final ArrayList<Debt> debts = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, debts);
        ListView list = (ListView) findViewById(R.id.debtsList);
        list.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_windykator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.lend:
                lend();
                return true;

            case R.id.receive:
                receive();
                return true;

            case R.id.edit:
                edit();
                return true;

            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage(person.toString() + "\nCzy chcesz usunąć tę osobę?")
                        .setNegativeButton("Anuluj", null)
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                            }
                        }).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void edit() {

        Intent intent = new Intent(WindykatorActivity.this, PersonActivity.class);
        intent.putExtra("person", person);
        startActivityForResult(intent, 1238);

    }

    private void delete() {
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.deletePerson(person);

        this.setResult(RESULT_OK, getIntent());
        this.finish();

    }

    private void lend() {
        Intent intent = new Intent(WindykatorActivity.this, DebtActivity.class);
        intent.putExtra("lending", true);
        startActivityForResult(intent, 1237);
    }

    private void receive() {
        Intent intent = new Intent(WindykatorActivity.this, DebtActivity.class);
        intent.putExtra("lending", false);
        startActivityForResult(intent, 1237);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1237) {
            if (resultCode == RESULT_OK) {

                Debt debt = (Debt) data.getExtras().getSerializable("Debt");
                debt.setPersonID(person.getId());
                adapter.add(debt);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 1238) {
            if (resultCode == RESULT_OK) {
                Bundle bun = data.getExtras();
                Person p = (Person) bun.getSerializable("person");

                DBHandler dbHandler = new DBHandler(this);
                dbHandler.updatePerson(p);
                person = dbHandler.getPerson((int) p.getId());
                dbHandler.close();

                ((TextView) findViewById(R.id.person)).setText(person.toString());

                if (p.isSendMessage()) {

                    ((TextView) findViewById(R.id.number)).setText(person.getPhoneNo());
                    findViewById(R.id.number).setVisibility(View.VISIBLE);

                } else
                    findViewById(R.id.number).setVisibility(View.INVISIBLE);
            }
        }
    }

}
