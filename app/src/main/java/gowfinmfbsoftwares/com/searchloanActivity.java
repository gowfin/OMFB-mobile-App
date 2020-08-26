package gowfinmfbsoftwares.com;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;


public class searchloanActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    final DatabaseHelper OHAFIAMFBDB = new DatabaseHelper(this);
    String grp = "none";
    SimpleCursorAdapter adapter;
    ListView listView = null;
    AlertDialog.Builder msg;
    TextView ac_no;

    @Override
    public boolean onQueryTextChange(String s) {
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
//Extract the group name…
        grp = bundle.getString("Group");
        if (s.length() > 1) {
            Cursor cursor = OHAFIAMFBDB.searchbygrp(s, grp);


            int[] toViews = {R.id.searchnameln, R.id.searchAcnoln, R.id.searchamountln};


            msg = new AlertDialog.Builder(this);
            try {
                adapter = new SimpleCursorAdapter(this,
                        R.layout.activity_searchloano, cursor, new String[]{"AC_NAME", "AC_NO", "EXPECTEDBAL"}, toViews, 0);
                listView.setAdapter(adapter);
            } catch (Exception e) {
                msg.setCancelable(true);
                msg.setMessage(e.getMessage());
                msg.show();
            }
        }//end of if(s.length>1)
        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchloan);

        SearchView simpleSearchView = (SearchView) findViewById(R.id.simpleSearchViewln);
        simpleSearchView.setOnQueryTextListener(this);

        listView = (ListView) findViewById(R.id.listViewln);
        Button deposit_btn = (Button) findViewById(R.id.searchbtndepositln);


    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    public void postln(View v) {
        AlertDialog.Builder Build = new AlertDialog.Builder(this);
        //fetching the view parent
        View parentRow = (View) v.getParent();
        //assigning the parent to a view
        ListView listView = (ListView) parentRow.getParent();
// getting the clicked button position
        final int position = listView.getPositionForView(parentRow);

        Cursor items = (Cursor) adapter.getItem(position);

        String acno = items.getString(1).trim();
        // String amt= items.getString(3).trim();
        TextView amt = (TextView) parentRow.findViewById(R.id.searchamountln);


        boolean t = false;
        try {

            t = OHAFIAMFBDB.updateln(acno.trim(), amt.getText().toString().trim());
            //t = OHAFIAMFBDB.Zerodata(amt);
        } catch (Exception e) {
            Build.setCancelable(true);
            Build.setTitle("ERROR Reported");
            Build.setMessage(e.getMessage());
        }
        if (t) {
            Build.setCancelable(true);
            Build.setTitle("Posted");
            String cbal = "0";
            Cursor rs = OHAFIAMFBDB.getCashln();
            if (rs.moveToFirst()) {
                cbal = rs.getString(0);
            }
            Build.setMessage("Transaction Successful \n Total Rep:" + cbal);
            Build.show();
            amt.setText(amt.getText());


        } else {
            Build.setCancelable(true);
            Build.setTitle("ERROR Reported");
            //Build.setMessage("Transaction not posted");


            Build.show();
        }


        //Toast.makeText(searchActivity.this,"Your acno= "+acno+" and amount= "+amt,Toast.LENGTH_LONG).show();

    }

    public void pressln(View v) {
        AlertDialog.Builder Build = new AlertDialog.Builder(this);
        //fetching the view parent
        View parentRow = (View) v.getParent();
        //assigning the parent to a view
        ListView listView = (ListView) parentRow.getParent();
// getting the clicked button position
        final int position = listView.getPositionForView(parentRow);

        Cursor items = (Cursor) adapter.getItem(position);

        String acno = items.getString(1).trim();
        // String amt= items.getString(3).trim();
        TextView amt = (TextView) parentRow.findViewById(R.id.searchamountln);
        TextView changename = (TextView) parentRow.findViewById(R.id.searchnameln);
        TextView changeacno = (TextView) parentRow.findViewById(R.id.searchAcnoln);
        try {

            String bal = OHAFIAMFBDB.getBaln(acno.trim());
            Build.setTitle("Loan Bal");
            Build.setMessage(bal);
            Build.show();
        } catch (Exception e) {
            Build.setCancelable(true);
            Build.setTitle("ERROR Reported");
            Build.setMessage(e.getMessage());
        }


        //Toast.makeText(searchActivity.this,"Your acno= "+acno+" and amount= "+amt,Toast.LENGTH_LONG).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent prevpage = new Intent(searchloanActivity.this, optionActivity.class);
        startActivity(prevpage);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.group:
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("ENTER GROUPID");

// Set up the input
                final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        grp = input.getText().toString();


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                grp = input.getText().toString();

                return true;
            case R.id.logout:
                this.finish();
                return true;
            case R.id.opendeposit:
                Intent deposit = new Intent(searchloanActivity.this, searchActivity.class);
                deposit.putExtra("Group", grp);
                startActivity(deposit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }  //end of case

    } // end of menu method
} //end of class
