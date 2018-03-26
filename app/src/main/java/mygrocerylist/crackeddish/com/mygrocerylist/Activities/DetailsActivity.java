package mygrocerylist.crackeddish.com.mygrocerylist.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mygrocerylist.crackeddish.com.mygrocerylist.Data.DatabaseHandler;
import mygrocerylist.crackeddish.com.mygrocerylist.Model.Grocery;
import mygrocerylist.crackeddish.com.mygrocerylist.R;

import mygrocerylist.crackeddish.com.mygrocerylist.UI.RecyclerViewAdapter;

public class DetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView quantity;
    private TextView dateAdded;
    private int griceryId;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private Button editButton;
    private Button deleteButton;
    private Context context;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        itemName=(TextView)findViewById(R.id.itemNameDet);
        quantity=(TextView)findViewById(R.id.quantityDet);
        dateAdded=(TextView)findViewById(R.id.dateAddedDet);
        final Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));
            griceryId=bundle.getInt("id");

        }
        final Grocery grocery=new Grocery();
        grocery.setName(itemName.getText().toString());
        grocery.setQuantity(quantity.getText().toString());
        grocery.setDateItemAdded(dateAdded.getText().toString());
        grocery.setId(bundle.getInt("id"));
        editButton=(Button)findViewById(R.id.editButtonDet);
        deleteButton=(Button)findViewById(R.id.deleteButtonDet);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(grocery);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(bundle.getInt("id"));

            }
        });

    }
    public void deleteItem(final int id){
        //create an AlertDialog
        alertDialogBuilder=new AlertDialog.Builder(this);
        inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.confirmation_dialog,null);
        Button noButton=(Button)view.findViewById(R.id.noButton);
        Button yesButton=(Button)view.findViewById(R.id.yesButton);
        alertDialogBuilder.setView(view);
        dialog=alertDialogBuilder.create();
        dialog.show();
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete item.
                DatabaseHandler db=new DatabaseHandler(context);
                Log.d("after db creation","1");
                db.deleteGrocery(id);
                Log.d("after db creation","2");
                startActivity(new Intent(DetailsActivity.this,ListActivity.class));
                Log.d("after db creation","3");
                //notifyItemRemoved(getAdapterPosition());
                dialog.dismiss();
            }
        });
    }
    public void editItem(final Grocery  grocery){
        Log.d("after db creation edit","1");
        alertDialogBuilder=new AlertDialog.Builder(context);
        inflater=LayoutInflater.from(context);
        final View view =inflater.inflate(R.layout.popup,null);
        final EditText groceryItem=(EditText)view.findViewById(R.id.groceryItem);
        final EditText  quantity=(EditText)view.findViewById(R.id.groceryQty);
        final TextView title=(TextView)view.findViewById(R.id.tile);
        groceryItem.setText(grocery.getName().toString());
        quantity.setText(grocery.getQuantity().toString().substring(5));
        title.setText("Edit Grocery");
        Button saveButton=(Button)view.findViewById(R.id.saveButton);
        alertDialogBuilder.setView(view);
        dialog=alertDialogBuilder.create();
        dialog.show();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db=new DatabaseHandler(context);
                //update Item
                grocery.setName(groceryItem.getText().toString());
                grocery.setQuantity(quantity.getText().toString());
                if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()){
                    Log.d("after db creation edit","2");
                    db.updateGrocery(grocery);
                    dialog.dismiss();
                    startActivity(new Intent(DetailsActivity.this,ListActivity.class));

                    //startActivity(new Intent(DetailsActivity.this,DetailsActivity.class));


                    Log.d("after db creation edit","3");

                }else {
                    Snackbar.make(view,"Add Grocery and quantity",Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });
    }
}
