package mygrocerylist.crackeddish.com.mygrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import mygrocerylist.crackeddish.com.mygrocerylist.Data.DatabaseHandler;
import mygrocerylist.crackeddish.com.mygrocerylist.Model.Grocery;
import mygrocerylist.crackeddish.com.mygrocerylist.R;
import mygrocerylist.crackeddish.com.mygrocerylist.UI.RecyclerViewAdapter;

public class ListActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private List<Grocery> groceryList;
    private List<Grocery> listItems;
    private RecyclerViewAdapter recyclerViewAdapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText groceryItem;
    private EditText quantity;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                createPopDialog();
            }
        });
        db=new DatabaseHandler(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        groceryList=new ArrayList<>();
        listItems=new ArrayList<>();
        //Get Items from Data base
        groceryList=db.getAllGrocery();
        for (Grocery c:groceryList){
            Grocery grocery=new Grocery();
            Log.d("Name ",c.getName().toString());
            grocery.setName(c.getName());
            grocery.setQuantity("Qty: "+c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded("Added  on: "+c.getDateItemAdded());
            listItems.add(grocery);
        }
        recyclerViewAdapter=new RecyclerViewAdapter(this,listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private void createPopDialog() {



        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        groceryItem = (EditText) view.findViewById(R.id.groceryItem);
        quantity = (EditText) view.findViewById(R.id.groceryQty);
        saveButton = (Button) view.findViewById(R.id.saveButton);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroceryToDB(v);
            }
        });



    }

    private void saveGroceryToDB(View v) {

        Grocery grocery = new Grocery();

        String newGrocery = groceryItem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()))
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200); //  1 second.



    }

}