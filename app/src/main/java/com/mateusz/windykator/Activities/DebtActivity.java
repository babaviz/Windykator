package com.mateusz.windykator.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mateusz.windykator.R;
import com.mateusz.windykator.pojos.Debt;


public class DebtActivity extends ActionBarActivity {

    private boolean lending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debt);

        lending = getIntent().getBooleanExtra("lending",true);

        TextView textView = (TextView)findViewById(R.id.titleText);
        if(lending){
            textView.setText("Pożycza pieniądze");
        }else {
            textView.setText("Oddaje pieniądze");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_debt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view){
        String title = ((EditText)findViewById(R.id.title)).getText().toString();
        String zlotowki = ((EditText)findViewById(R.id.zlotowki)).getText().toString();
        String grosze = ((EditText)findViewById(R.id.grosze)).getText().toString();

        if (title.isEmpty()){
            title= lending ? "Pożyczył" : "Oddał";
        }

        if (!grosze.isEmpty()){
            zlotowki=zlotowki.concat(".").concat(grosze);
        }

        if (!zlotowki.isEmpty()){

            float value = Float.parseFloat(zlotowki);
            if (!lending)
                value *=-1;

            Debt debt = new Debt(value,title);
            Intent intent = getIntent();
            intent.putExtra("Debt",debt);
            this.setResult(RESULT_OK, intent);
            this.finish();

        }
    }
}
