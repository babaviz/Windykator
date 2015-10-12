package com.mateusz.windykator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mateusz.windykator.R;
import com.mateusz.windykator.database.DBHandler;
import com.mateusz.windykator.pojos.Person;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbHandler = new DBHandler(this);

        final List<Person> persons = dbHandler.getAllPersons();
        dbHandler.close();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, persons);

        ListView listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Person p = (Person) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, WindykatorActivity.class);

                intent.putExtra("person", p);

                startActivityForResult(intent, 1235);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            case R.id.add:
                startAddingUser();
                return true;

            case R.id.send:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startAddingUser() {
        Intent go2ActIntent = new Intent(MainActivity.this, PersonActivity.class);
        startActivityForResult(go2ActIntent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Person Activity started for Adding
        if (requestCode == 1234) {
            if (resultCode == RESULT_OK) {
                Bundle bun = data.getExtras();
                Person p = (Person) bun.getSerializable("person");
                DBHandler dbHandler = new DBHandler(this);
                dbHandler.addPerson(p);
                final List<Person> persons = dbHandler.getAllPersons();
                dbHandler.close();

                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, persons);

                ListView listView = (ListView) findViewById(R.id.list_item);
                listView.setAdapter(adapter);
            }
        }

        //Windykator Activity result
        else if (requestCode == 1235) {
            if (resultCode == RESULT_OK) {
                DBHandler dbHandler = new DBHandler(this);
                final List<Person> persons = dbHandler.getAllPersons();
                dbHandler.close();

                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, persons);

                ListView listView = (ListView) findViewById(R.id.list_item);
                listView.setAdapter(adapter);

            }
            else if(resultCode == RESULT_CANCELED){

                DBHandler dbHandler = new DBHandler(this);
                final List<Person> persons = dbHandler.getAllPersons();
                dbHandler.close();

                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, persons);

                ListView listView = (ListView) findViewById(R.id.list_item);
                listView.setAdapter(adapter);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
