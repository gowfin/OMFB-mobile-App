package gowfinmfbsoftwares.com;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class AllClientActivity extends AppCompatActivity {
    DatabaseHelper OHAFIAMFBDB;
    String totalposted;
    String alltrans;
    String[] clients = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_client);
        OHAFIAMFBDB = new DatabaseHelper(this);
        Cursor total = OHAFIAMFBDB.getCashbal();
        if (total.moveToFirst()) {
            totalposted = total.getString(0);
            alltrans = totalposted;
        }
        total.close();

        Cursor All = OHAFIAMFBDB.getAlltrans("0");
        if (All.moveToFirst()) {
            clients = new String[All.getCount() + 1]; //add 1 to accommodate for the total column
            clients[0] = "Total transactions posted****" + totalposted + "****";
            for (int i = 0; i < All.getCount(); i++) {
                clients[i + 1] = All.getString(2) + "(" + All.getString(1) + ")  " + "=N=" + All.getString(3);
                All.moveToNext();
            } //end of for stmt
        }
        ; //end of if stmt
        All.close();

        ListView clientlist = (ListView) findViewById(R.id.listViewall);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, clients);
        clientlist.setAdapter(adapter);

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent prevpage = new Intent(AllClientActivity.this, optionActivity.class);
        startActivity(prevpage);
        this.finish();
    }
}
