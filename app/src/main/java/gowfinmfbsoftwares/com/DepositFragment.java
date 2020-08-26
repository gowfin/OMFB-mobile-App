package gowfinmfbsoftwares.com;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositFragment extends Fragment {

     DatabaseHelper OHAFIAMFBDB ;
    SimpleCursorAdapter adapter;
    ListView listView = null;
    AlertDialog.Builder msg;
    String grp = "none";
    Cursor cursor;
    TextView ac_no;

    @Override
    public View onCreateView(LayoutInflater inflater,   ViewGroup container,
                              Bundle savedInstanceState) {


       final View rootView =inflater.inflate(R.layout.fragment_deposit, container, false);
        listView =   rootView.findViewById(R.id.listViewfrag1);

        // Inflate the layout for this fragment
        OHAFIAMFBDB =new DatabaseHelper(getActivity());


        int[] toViews = {R.id.searchnamefra, R.id.searchAcnofra, R.id.searchamountfra};
        adapter = new SimpleCursorAdapter(getContext(),
                R.layout.fragment_deposito, cursor, new String[]{"AC_NAME", "AC_NO", "AMOUNT"}, toViews, 0);

        SearchView simpleSearchView =  rootView.findViewById(R.id.simpleSearchViewFrag1);


        simpleSearchView.setOnQueryTextListener(
                /////////////////////////

        new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
           public boolean onQueryTextChange(String s) {

                if (s.length() > 1) {

                    cursor = OHAFIAMFBDB.search(s, grp);





                    msg = new AlertDialog.Builder(getActivity());

                    try {
                         msg = new AlertDialog.Builder(getActivity());
                       // cursor.moveToFirst();
                        listView.setAdapter(adapter);
                       // msg.setCancelable(true);
                      // msg.setMessage(cursor.getString(1));
                      //  msg.show();
                    } catch (Exception e) {
                        msg.setCancelable(true);
                        msg.setMessage(e.getMessage());
                        msg.show();
                    }
                }//end of if(s.length>1)
                return false;

            }


    }

                /////////////////





        );



      return rootView;


    }



    public void post(View v) {
        AlertDialog.Builder Build = new AlertDialog.Builder(this.getActivity());
        //fetching the view parent
        View parentRow = (View) v.getParent();
        //assigning the parent to a view
        ListView listView = (ListView) parentRow.getParent();
// getting the clicked button position
        final int position = listView.getPositionForView(parentRow);

        Cursor items = (Cursor) adapter.getItem(position);

        String acno = items.getString(1).trim();
        // String amt= items.getString(3).trim();
        TextView amt = (TextView) parentRow.findViewById(R.id.searchamount);
        TextView changename = (TextView) parentRow.findViewById(R.id.searchname);
        TextView changeacno = (TextView) parentRow.findViewById(R.id.searchAcno);


        boolean t = false;
        try {

            t = OHAFIAMFBDB.updatedatafull(acno.trim(), amt.getText().toString().trim(), changename.getText().toString(), changeacno.getText().toString());
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
            Cursor rs = OHAFIAMFBDB.getCashbal();
            if (rs.moveToFirst()) {
                cbal = rs.getString(0);
            }
            Build.setMessage("Transaction Successful \n Total Deposit:" + cbal);
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







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.group:
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getActivity());
                builder.setTitle("ENTER GROUPID");

// Set up the input
                final EditText input = new EditText(this.getActivity());
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
            case R.id.zerodeposit:
                final android.app.AlertDialog.Builder builderr = new android.app.AlertDialog.Builder(this.getActivity());
                final android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(this.getActivity());
                builderr.setTitle("Do you want to zero all deposit?");

// Set up the input
                // final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                // builder.setView(input);

// Set up the buttons
                builderr.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        OHAFIAMFBDB.Zerodata("0");

                        build.setTitle("Trans Status");
                        build.setMessage("All Transactions Zeroed Out");
                        build.show();
                        //refresh Cash Balance


                        //m_Text = input.getText().toString();
                    }
                });
                builderr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builderr.show();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }  //end of case

    } // end of menu method

    ////////////////////////
    public void press(View v) {
        AlertDialog.Builder Build = new AlertDialog.Builder(getActivity());
        //fetching the view parent
        View parentRow = (View) v.getParent();
        //assigning the parent to a view
        ListView listView = (ListView) parentRow.getParent();
// getting the clicked button position
        final int position = listView.getPositionForView(parentRow);

        Cursor items = (Cursor) adapter.getItem(position);

        String acno = items.getString(1).trim();
        // String amt= items.getString(3).trim();
        TextView amt = (TextView) parentRow.findViewById(R.id.searchamount);
        TextView changename = (TextView) parentRow.findViewById(R.id.searchname);
        TextView changeacno = (TextView) parentRow.findViewById(R.id.searchAcno);
        try {

            String bal = OHAFIAMFBDB.getBal(acno.trim());
            Build.setTitle("Deposit Bal");
            Build.setMessage(bal);
            Build.show();
        } catch (Exception e) {
            Build.setCancelable(true);
            Build.setTitle("ERROR Reported");
            Build.setMessage(e.getMessage());
        }


        //Toast.makeText(searchActivity.this,"Your acno= "+acno+" and amount= "+amt,Toast.LENGTH_LONG).show();

    }




}

