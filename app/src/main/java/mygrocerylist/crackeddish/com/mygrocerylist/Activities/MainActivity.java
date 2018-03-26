package mygrocerylist.crackeddish.com.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import mygrocerylist.crackeddish.com.mygrocerylist.Data.DatabaseHandler;
import mygrocerylist.crackeddish.com.mygrocerylist.Model.Grocery;
import mygrocerylist.crackeddish.com.mygrocerylist.R;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        db=new DatabaseHandler(this);
        byPassActivity();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createPopupDialog(){
        dialogBuilder=new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        groceryItem=(EditText) view.findViewById(R.id.groceryItem);
        quantity=(EditText) view.findViewById(R.id.groceryQty);
        saveButton=(Button)view.findViewById(R.id.saveButton);
        dialogBuilder.setView(view);
        dialog=dialogBuilder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo : save to db
                //Go to next screen
                if (!groceryItem.getText().toString().isEmpty()&&!quantity.getText().toString().isEmpty()){
                    saveGroceryToDB(v);
                }

            }
        });
    }

    private void saveGroceryToDB(View v) {
        Grocery grocery=new Grocery();
        String newGrocery=groceryItem.getText().toString();
        String newGroceryQuantity=quantity.getText().toString();
        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);
        //Save to db
        db.addGrocery(grocery);
        Snackbar.make(v,"Item Saved",Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200);
        Log.d("Item Added id",String.valueOf(db.getGroceryCount()));
    }
    public void byPassActivity(){
        if (db.getGroceryCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }
}
