package gowfinmfbsoftwares.com;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class optionActivity extends AppCompatActivity {
    Cursor rs = null;
    DatabaseHelper OhafiaMFBDB;

    Global glob = new Global();
    sqlposting sqlpost;
    Connection conn;

    sendSMS send = new sendSMS();
    String[] glnoA;
    String[] acnoA;
    String[] amountA;
    String[] DepositorA;
    String dbname = "Android";
    TextView cashbal;
    double loanBal = 0, SavBal = 0;
    String unionlist;

    public static String getcurrentDateAndTime() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


        OhafiaMFBDB = new DatabaseHelper(this);

        dbname = getIntent().getStringExtra("trxname");

        System.out.println("XXXXXXXXXXXXXXXXXX" + dbname + "XXXXXXXXXXXXXXXXXXXXX");


        cashbal = (TextView) findViewById(R.id.cashbal);
        try {
            rs = OhafiaMFBDB.getCashbal();
        } catch (Exception e) {
            cashbal.setText(e.getMessage());
        }
        if (rs.moveToFirst()) {
            SavBal = rs.getDouble(0);

        }
        try {
            rs = OhafiaMFBDB.getCashln();

        } catch (Exception e) {
            cashbal.setText(e.getMessage());
        }
        if (rs.moveToFirst()) {
            loanBal = rs.getDouble(0);

        }
        loanBal = loanBal + SavBal;
        cashbal.setText("=N= " + loanBal);
        Button deposit = (Button) findViewById(R.id.btndeposit);
        Button addNew = (Button) findViewById(R.id.btndaddnew);
        Button viewaccount = (Button) findViewById(R.id.viewaccount);
        Button depositacno = (Button) findViewById(R.id.PostByAcno);
        Button postloan = (Button) findViewById(R.id.postloan);
        Button postgroup = (Button) findViewById(R.id.postboth);
        viewaccount.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               if (cashbal.getText().toString().compareToIgnoreCase("=N= 0") != 0) {
                                                   Intent intent = new Intent(optionActivity.this, AllClientActivity.class);
                                                   startActivity(intent);
                                                   optionActivity.this.finish();

                                               } else {
                                                   AlertDialog.Builder bui = new AlertDialog.Builder(optionActivity.this);
                                                   bui.setMessage("No transaction to display");
                                                   bui.show();
                                               }
                                           }
                                       }

        );
        depositacno.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent intent = new Intent(optionActivity.this, depositActivity.class);
                                               startActivity(intent);
                                               optionActivity.this.finish();
                                           }
                                       }

        );
        deposit.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent intent = new Intent(optionActivity.this, searchActivity.class);
                                           startActivity(intent);
                                           optionActivity.this.finish();

                                       }
                                   }

        );
        postloan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(optionActivity.this, searchloanActivity.class);
                                            startActivity(intent);
                                            optionActivity.this.finish();

                                        }
                                    }

        );
        addNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(optionActivity.this, addnewActivity.class);
                startActivity(intent);
                optionActivity.this.finish();
            }
        });
        postgroup.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent intent = new Intent(optionActivity.this, GroupActivity.class);
                                              startActivity(intent);
                                              optionActivity.this.finish();
                                          }
                                      }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clearall:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog.Builder build = new AlertDialog.Builder(optionActivity.this);
                builder.setTitle("Do you want to clear all transactions?");

// Set up the input
                // final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                // builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        OhafiaMFBDB.deletedeposit();
                        OhafiaMFBDB.deleteloans();
                        build.setTitle("Trans Status");
                        build.setMessage("All transactions cleared out");
                        build.show();
                        //refresh Cash Balance

                        try {
                            rs = OhafiaMFBDB.getCashbal();
                        } catch (Exception e) {
                            cashbal.setText(e.getMessage());
                        }
                        if (rs.moveToFirst()) {
                            cashbal.setText("=N= " + rs.getString(0));
                        }
                        //m_Text = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            case R.id.chuserpw:
                final AlertDialog.Builder Mes = new AlertDialog.Builder(this);
                final AlertDialog.Builder mes = new AlertDialog.Builder(optionActivity.this);
                final EditText input = new EditText(this);
                Mes.setTitle("Enter New Password.");

// Set up the input

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Mes.setView(input);

// Set up the buttons
                Mes.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (dbname.compareToIgnoreCase(input.getText().toString()) == 0) {
                            mes.setTitle("Password Error");
                            mes.setMessage("Password and username cannot be thesame \n Please choose another password.");
                            mes.show();
                        } else {
                            boolean status = OhafiaMFBDB.setDbUserpw(dbname, input.getText().toString());
                            if (status) {
                                mes.setTitle("User's password");
                                mes.setMessage("Password Changed");
                                mes.show();
                            } else {
                                mes.setTitle("ERROR!");
                                mes.setMessage("Error Occurred or Invalid user");
                                mes.show();
                            }
                        }
                    }
                });
                Mes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Mes.show();
                return true;
            case R.id.chuserID:
                final AlertDialog.Builder Me = new AlertDialog.Builder(this);
                final AlertDialog.Builder me = new AlertDialog.Builder(optionActivity.this);
                final EditText inputx = new EditText(this);
                Me.setTitle("Enter New Username.");

// Set up the input

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                inputx.setInputType(InputType.TYPE_CLASS_TEXT);
                Me.setView(inputx);

// Set up the buttons
                Me.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor checkpw = OhafiaMFBDB.getDbUserpw(dbname);
                        checkpw.moveToFirst();
                        if (checkpw.getString(1).compareToIgnoreCase(inputx.getText().toString().trim().toLowerCase()) == 0) {
                            me.setTitle("User Error");
                            me.setMessage("Password and username cannot be thesame \n Please choose another username.");
                            me.show();
                        } else {
                            boolean status = OhafiaMFBDB.setDbUserID(dbname.toLowerCase(), inputx.getText().toString().trim().toLowerCase());
                            if (status) {
                                me.setTitle("User's Name");
                                me.setMessage("Password Changed");
                                me.show();
                            } else {
                                me.setTitle("ERROR!");
                                me.setMessage("Error Occurred or Invalid user");
                                me.show();
                            }
                        }
                    }
                });
                Me.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Me.show();
                return true;
            case R.id.logout:

                this.finish();
                return true;
            case R.id.sync:
                final AlertDialog.Builder Mesg = new AlertDialog.Builder(this);
                final AlertDialog.Builder msg = new AlertDialog.Builder(optionActivity.this);
                Mesg.setTitle("Do you want to extract and sync with Finance Solutions?");

// Set up the input
                // final EditText input2 = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                // builder.setView(input);

// Set up the buttons
                Mesg.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            try {
                                Cursor rs = OhafiaMFBDB.getAlltrans("0");
                                String[] condetail = OhafiaMFBDB.getconfig();
                                conn = glob.getConnect(condetail[0], condetail[1], condetail[2], condetail[3]);
                                if (conn == null) {
                                    Toast.makeText(optionActivity.this, "Connection is null", Toast.LENGTH_SHORT).show();
                                }
                                sqlpost = new sqlposting();
                                conn.setAutoCommit(false);
                                if (rs.moveToFirst()) {
                                    glnoA = new String[rs.getCount()];
                                    acnoA = new String[rs.getCount()];
                                    amountA = new String[rs.getCount()];
                                    DepositorA = new String[rs.getCount()];
                                    for (int i = 0; i < rs.getCount(); i++) {//posting one by one

                                        //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + rs.getString(1) + " count= " + rs.getCount() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                                        sqlpost.postsqlserver(conn, rs.getString(1), rs.getString(3), rs.getString(2), dbname);   //Connection conn,String fsacno,String amount,String name
                                        //collect an array of depositors for SMS alert
                                        glnoA[i] = rs.getString(1).toString().substring(0, 6);
                                        acnoA[i] = rs.getString(1).toString().substring(7).trim();
                                        amountA[i] = rs.getString(3).trim();
                                        DepositorA[i] = rs.getString(2).trim();

                                        rs.moveToNext();
                                    }
                                }
                                conn.commit();
                                int rcount = rs.getCount();

                                String dtt = getcurrentDateAndTime();
                                for (int i = 0; i < rcount; i++) {
                                    if (glnoA[i].contains("200301")) {
                                    } else {

                                        send.getAcc("Credit", acnoA[i], glnoA[i], Double.parseDouble(amountA[i]), "CSH DEP BY " + DepositorA[i], dtt + " 00:00:00.000", conn);

                                    }// end of if
                                }//end of for

                                //finshed posting savings, now posting loans

                                //condetail=OhafiaMFBDB.getconfig();
                                //conn = glob.getConnect(condetail[0],condetail[1],condetail[2],condetail[3]);
                                if (conn == null) {
                                    Toast.makeText(optionActivity.this, "Connection is null", Toast.LENGTH_SHORT).show();
                                }
                                sqlpostingloan sqlpostln = new sqlpostingloan();
                                conn.setAutoCommit(false);
                                rs.close();
                                rs = OhafiaMFBDB.getAllrepayment("0");
                                if (rs.moveToFirst()) {
                                    for (int i = 0; i < rs.getCount(); i++) {//posting one by one

                                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + rs.getString(1) + " count= " + rs.getCount() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                                        sqlpostln.postsqlserver(conn, rs.getString(1), rs.getString(6), rs.getString(2), dbname, rs.getString(7), rs.getString(3));   //Connection conn,String fsacno,String amount,String name,String username,String prevBal,String Product
                                        //collect an array of depositors for SMS alert

                                        rs.moveToNext();
                                    }
                                }
                                conn.commit();


                                msg.setTitle("Trans Status");
                                //Clear all the transactions

                                // OhafiaMFBDB.Zerodata("0");

                                msg.setMessage("All Transactions Posted to FS");
                                msg.show();
                                rs.close();
                                try {
                                    rs = OhafiaMFBDB.getCashln();

                                } catch (Exception e) {
                                    cashbal.setText(e.getMessage());
                                }
                                if (rs.moveToFirst()) {
                                    loanBal = rs.getDouble(0);
                                }
                                rs.close();
                                try {
                                    rs = OhafiaMFBDB.getCashbal();

                                } catch (Exception e) {
                                    cashbal.setText(e.getMessage());
                                }
                                if (rs.moveToFirst()) {
                                    SavBal = rs.getDouble(0);
                                }
                                loanBal = loanBal + SavBal;
                                cashbal.setText("=N= " + loanBal);
                                rs.close();
                                conn.close();
                            } catch (Exception e) {
                                Toast.makeText(optionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + e.getMessage() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                            }

                        } else {
                            msg.setMessage("Not Connected to a network");
                            msg.show();
                        }

                    }
                });
                Mesg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Mesg.show();


                return true;
            case R.id.offline:

                final AlertDialog.Builder Mesgoff = new AlertDialog.Builder(this);
                final AlertDialog.Builder msgoff = new AlertDialog.Builder(optionActivity.this);
                Mesgoff.setTitle("Do you want to post via phone hotspot?");


// Set up the buttons
                Mesgoff.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //   if( isOnline()) {
                        try {
                            Cursor rs = OhafiaMFBDB.getAlltrans("0");
                            String[] condetail = OhafiaMFBDB.getconfig();
                            conn = glob.getConnect(condetail[0], condetail[1], condetail[2], condetail[3]);
                            if (conn == null) {
                                Toast.makeText(optionActivity.this, "Connection is null", Toast.LENGTH_SHORT).show();
                            }
                            sqlpost = new sqlposting();
                            conn.setAutoCommit(false);
                            if (rs.moveToFirst()) {
                                glnoA = new String[rs.getCount()];
                                acnoA = new String[rs.getCount()];
                                amountA = new String[rs.getCount()];
                                DepositorA = new String[rs.getCount()];
                                for (int i = 0; i < rs.getCount(); i++) {//posting one by one

                                    //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + rs.getString(1) + " count= " + rs.getCount() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                                    sqlpost.postsqlserver(conn, rs.getString(1), rs.getString(3), rs.getString(2), dbname);   //Connection conn,String fsacno,String amount,String name
                                    //collect an array of depositors for SMS alert
                                    glnoA[i] = rs.getString(1).toString().substring(0, 6);
                                    acnoA[i] = rs.getString(1).toString().substring(7).trim();
                                    amountA[i] = rs.getString(3).trim();
                                    DepositorA[i] = rs.getString(2).trim();

                                    rs.moveToNext();

                                }
                                int rcount = rs.getCount();

                                String dtt = getcurrentDateAndTime();
                                for (int i = 0; i < rcount; i++) {
                                    if (glnoA[i].contains("200301-")) {
                                    } else {

                                        send.getAcc("Credit", acnoA[i], glnoA[i], Double.parseDouble(amountA[i]), "CSH DEP BY " + DepositorA[i], dtt + " 00:00:00.000", conn);
//
                                    }// end of if
                                }//end of for

                            }
                            conn.commit();
                            //finshed posting savings, now posting loans

                            //condetail=OhafiaMFBDB.getconfig();
                            //conn = glob.getConnect(condetail[0],condetail[1],condetail[2],condetail[3]);
                            if (conn == null) {
                                Toast.makeText(optionActivity.this, "Connection is null", Toast.LENGTH_SHORT).show();
                            }
                            sqlpostingloan sqlpostln = new sqlpostingloan();
                            conn.setAutoCommit(false);
                            rs.close();
                            rs = OhafiaMFBDB.getAllrepayment("0");
                            if (rs.moveToFirst()) {

                                for (int i = 0; i < rs.getCount(); i++) {//posting one by one

                                    // System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + rs.getString(1) + " count= " + rs.getCount() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                                    // Toast.makeText(optionActivity.this, rs.getString(1)+rs.getString(6)+ rs.getString(2)+dbname+rs.getString(7)+rs.getString(3),Toast.LENGTH_LONG).show();   //Connection conn,String fsacno,String amount,String name,String username,String prevBal,String Product

                                    sqlpostln.postsqlserver(conn, rs.getString(1), rs.getString(6), rs.getString(2), dbname, rs.getString(7), rs.getString(3));   //Connection conn,String fsacno,String amount,String name,String username,String prevBal,String Product


                                    rs.moveToNext();

                                }
                            }

                            conn.commit();

                            rs.close();
                            try {
                                rs = OhafiaMFBDB.getCashln();

                            } catch (Exception e) {
                                cashbal.setText(e.getMessage());
                            }
                            if (rs.moveToFirst()) {
                                loanBal = rs.getDouble(0);
                            }
                            rs.close();
                            try {
                                rs = OhafiaMFBDB.getCashbal();

                            } catch (Exception e) {
                                cashbal.setText(e.getMessage());
                            }
                            if (rs.moveToFirst()) {
                                SavBal = rs.getDouble(0);
                            }
                            loanBal = loanBal + SavBal;
                            cashbal.setText("=N= " + loanBal);
                            conn.commit();
                            rs.close();
                            // Toast.makeText(optionActivity.this, "ok10", Toast.LENGTH_LONG).show();

                            conn.close();


                            msgoff.setTitle("Trans Status");
                            //Clear all the transactions

                            // OhafiaMFBDB.Zerodata("0");

                            msgoff.setMessage("All Transactions Posted to FS");
                            msgoff.show();


                        } catch (Exception e) {
                            Toast.makeText(optionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + e.getMessage() + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                        }


                    }
                });
                Mesgoff.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Mesgoff.show();
                return true;
            case R.id.getfsclient:
                final AlertDialog.Builder M = new AlertDialog.Builder(this);
                final AlertDialog.Builder m = new AlertDialog.Builder(optionActivity.this);
                final EditText inputf = new EditText(this);
                M.setTitle("Enter unions,separated with comma\nthen click download to start");

// Set up the input

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                inputf.setInputType(InputType.TYPE_CLASS_TEXT);
                M.setView(inputf);
                unionlist = inputf.getText().toString().replaceAll(",", "','");
// Set up the buttons
                M.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dtt = getcurrentDateAndTime();
                        String[] condetail = OhafiaMFBDB.getconfig();
                        conn = glob.getConnect(condetail[0], condetail[1], condetail[2], condetail[3]);
                        if (conn != null) {
                            //String SQL = " Select distinct custno+'-'+accountid fsacno,Replace(Branchid,'CASH DEP BY' ,'' ) name from transactn " +
                            //        " where dateeffective<'"+dtt+"' and accountid like'2000%'";
                            String SQL;
                            if (unionlist.isEmpty() || unionlist == "none") {
                                SQL = "select custno+'-'+accountid fsacno, accountname+'-'+left(ProductID,1) name,Groupid ,RunningBal Balance from deposit " +
                                        " where Status='Active' and AccountID like'2%'";
                            } else if (unionlist.trim().compareToIgnoreCase("D") == 0) {
                                SQL = "select custno+'-'+accountid fsacno, accountname+'-'+left(ProductID,1) name,Groupid ,RunningBal Balance from deposit " +
                                        " where Status='Active' and AccountID like'250%'";
                            } else {
                                SQL = "select custno+'-'+accountid fsacno, accountname+'-'+left(ProductID,1) name,Groupid ,RunningBal Balance from deposit " +
                                        " where Status='Active' and GroupID in ('" + unionlist + "')";

                            }
                            String SQLN = "select custno+'-'+Lnid acno,Accountname+'-'+left(Loanproduct,1) Accountname,Loanproduct,overdue,instalment+overdue expected,disbursed-repaid bal,groupID from Fieldprintview " +
                                    " where custno+'-'+Lnid like'0%' and Loanproduct <>'NULL' and disbursed-repaid>4 ";
                            try {
                                Statement stmt = conn.createStatement();
                                ResultSet current = stmt.executeQuery(SQL);
                                while (current.next()) {
                                    boolean status = OhafiaMFBDB.insertdata(current.getString(1), current.getString(2), "0", current.getString(3), current.getString(4));
                                }
                                current = stmt.executeQuery(SQLN);

                                while (current.next()) {
                                    boolean status = OhafiaMFBDB.insertdataln(current.getString(1), current.getString(2), current.getString(3), current.getString(4), current.getString(5), "0", current.getString(6), current.getString(7));
                                }
                                current.close();
                                conn.close();
                            } catch (Exception e) {
                                Toast.makeText(optionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            m.setTitle("Password Error");
                            m.setMessage("Cannot download clients. \n Please try again.");
                            m.show();
                        }
                    }
                });
                M.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                M.show();

                return true;

            case R.id.report:
                // do your code
                return true;
            case R.id.config:

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                final AlertDialog.Builder build2 = new AlertDialog.Builder(optionActivity.this);
                builder2.setTitle("Enter your Server Credentials.");

// Set up the input
                final EditText input0 = new EditText(this);
                final EditText input1 = new EditText(this);
                final EditText input2 = new EditText(this);
                final EditText input3 = new EditText(this);
                String[] consetting = OhafiaMFBDB.getconfig();
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input0.setHint("192.168.101.X");
                input0.setText(consetting[0]);
                input1.setHint("DBase");
                input1.setText(consetting[1]);
                input2.setHint("sa");
                input2.setText(consetting[2]);
                input3.setHint("password");
                input3.setText(consetting[3]);

                layout.addView(input0);
                layout.addView(input1);
                layout.addView(input2);
                layout.addView(input3);
                builder2.setView(layout);
// Set up the buttons
                builder2.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input0.getText() == null) {
                            Toast.makeText(optionActivity.this, "Ip cannot be empty", Toast.LENGTH_LONG).show();
                        } else if (input1.getText() == null) {
                            Toast.makeText(optionActivity.this, "Database cannot be empty", Toast.LENGTH_LONG).show();
                        } else if (input2.getText() == null) {
                            Toast.makeText(optionActivity.this, "Username cannot be empty", Toast.LENGTH_LONG).show();
                        } else if (input3.getText() == null) {
                            Toast.makeText(optionActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                        } else {
                            OhafiaMFBDB.updatedb(input0.getText().toString(), input1.getText().toString(), input2.getText().toString(), input3.getText().toString());
                            build2.setTitle("Saved to DB");
                            build2.setMessage("Credential updated Successfully");
                            build2.show();
                        }


                        //m_Text = input.getText().toString();
                    }
                });
                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder2.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent prevpage = new Intent(optionActivity.this, MainActivity.class);
        startActivity(prevpage);
        this.finish();
    }

    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}



