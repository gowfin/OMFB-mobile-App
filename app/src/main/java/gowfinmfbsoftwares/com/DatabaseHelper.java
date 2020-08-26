package gowfinmfbsoftwares.com;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "OhafiaMFBDB.db";
    public static final String TABLE_NAME = "memtrans";
    public static final String TABLE_NAMELN = "memloans";
    public static final String TABLE_NAME2 = "users";
    public static final String TABLE_NAME3 = "ConConfig";
    public static final String Col11 = "ID";
    public static final String Col22 = "PW";
    public static final String Col1 = "AC_NO";
    public static final String Col2 = "AC_NAME";
    public static final String Col3 = "AMOUNT";
    public static final String Col4 = "REMARK";
    public static final String Col5 = "GROUPID";
    public static final String Col6 = "BALANCE";
    public static final String Coln1 = "AC_NO";
    public static final String Coln2 = "AC_NAME";
    public static final String Coln3 = "PRODUCT";
    public static final String Coln4 = "OVERDUE";
    public static final String Coln5 = "EXPECTED";
    public static final String Coln6 = "AMOUNT";
    public static final String Coln7 = "BALANCE";
    public static final String Coln8 = "GROUPID";

    public static final String configCol1 = "ip";
    public static final String configCol2 = "db";
    public static final String configCol3 = "user";
    public static final String configCol4 = "pw";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating Savings table
        db.execSQL("create table " + TABLE_NAME + "(_id integer primary key autoincrement,AC_NO TEXT Unique,AC_NAME TEXT,AMOUNT REAL,REMARK TEXT, GROUPID TEXT,BALANCE TEXT )");
        //Creating loans table
        db.execSQL("create table " + TABLE_NAMELN + "(_id integer primary key autoincrement,AC_NO TEXT Unique,AC_NAME TEXT,PRODUCT TEXT,OVERDUE REAL, EXPECTED TEXT ,AMOUNT REAL,BALANCE REAL,GROUPID TEXT)");
//Creating users table,
        db.execSQL("create table " + TABLE_NAME2 + "(ID TEXT primary key,PW TEXT)");
        //Creating Connection table
        db.execSQL("create table " + TABLE_NAME3 + "(IP TEXT primary key DEFAULT '192.168.101.2',DB TEXT DEFAULT 'OHAFIAMFBDB',User TEXT DEFAULT 'sa' ,PW TEXT DEFAULT 'Admin123Server')");
        //Inserting default user
        db.execSQL("insert into " + TABLE_NAME3 + " values('192.168.101.2','DUMMY','sa','Admin123Server')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        db.execSQL("Drop table if exists " + TABLE_NAME2);
        db.execSQL("Drop table if exists " + TABLE_NAME3);
        db.execSQL("Drop table if exists " + TABLE_NAMELN);
        onCreate(db);
    }

    public boolean insertdata(String acno, String acname, String Amount, String Group, String Balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col1, acno);
        cv.put(Col2, acname);
        cv.put(Col3, Amount);
        cv.put(Col4, Balance);
        cv.put(Col5, Group);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean insertdataln(String acno, String acname, String product, String overdue
            , String expected, String Amount, String Balance, String group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Coln1, acno);
        cv.put(Coln2, acname);
        cv.put(Coln3, product);
        cv.put(Coln4, overdue);
        cv.put(Coln5, expected);
        cv.put(Coln6, Amount);
        cv.put(Coln7, Balance);
        cv.put(Coln8, group);

        long result = db.insert(TABLE_NAMELN, null, cv);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean updatedata(String accno, String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // cv.put(Col1,acno);
        // cv.put(Col2,acname);
        cv.put(Col3, Amount);
        // cv.put( Col4,Balance);

        long result = db.update(TABLE_NAME, cv, "AC_NO=?", new String[]{accno});
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean updateln(String accno, String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // cv.put(Col1,acno);
        // cv.put(Col2,acname);
        cv.put(Coln6, Amount);
        // cv.put( Col4,Balance);

        long result = db.update(TABLE_NAMELN, cv, "AC_NO=?", new String[]{accno});
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean updatedb(String ip, String dbn, String user, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(configCol1, ip);
        cv.put(configCol2, dbn);
        cv.put(configCol3, user);
        cv.put(configCol4, pw);

        long result = db.update(TABLE_NAME3, cv, null, null);
        if (result == -1)
            return false;

        else
            return true;
    }

    public String[] getconfig() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from Conconfig", null);
        String ip = "0", dbuser = "0", ui = "0", pw = "0";
        String[] creden;
        if (rs.moveToFirst()) {
            ip = rs.getString(0);
            dbuser = rs.getString(1);
            ui = rs.getString(2);
            pw = rs.getString(3);
        }
        creden = new String[]{ip, dbuser, ui, pw};
        return creden;
    }

    public boolean updatedatafull(String accno, String Amount, String changedacname, String changedAccno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col1, changedAccno);
        cv.put(Col2, changedacname);
        cv.put(Col3, Amount);
        // cv.put( Col4,Balance);

        long result = db.update(TABLE_NAME, cv, "AC_NO=?", new String[]{accno});
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean Zerodata(String Amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Amount = "0";
        // cv.put(Col1,acno);
        // cv.put(Col2,acname);
        cv.put(Col3, Amount);
        // cv.put( Col4,Balance);

        long result = db.update(TABLE_NAME, cv, null, null);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean deleteloans() {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAMELN, null, null);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean deletedeposit() {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, null, null);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean insertuser(String ID, String passwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col11, ID);
        cv.put(Col22, passwd);


        long result = db.insert(TABLE_NAME2, null, cv);
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean setDbUserpw(String user, String curpw) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // cv.put(Col1,acno);
        // cv.put(Col2,acname);
        cv.put("PW", curpw);
        // cv.put( Col4,Balance);

        long result = db.update(TABLE_NAME2, cv, "ID=?", new String[]{user});
        if (result == -1)
            return false;

        else
            return true;
    }

    public boolean setDbUserID(String user, String curID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", curID);


        long result = db.update(TABLE_NAME2, cv, "ID=?", new String[]{user});
        if (result == -1)
            return false;

        else
            return true;
    }

    public Cursor getDbUserpw(String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from users where id='" + user + "'", null);
        return rs;
    }

    public Cursor getAccname(String acno) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from memtrans where ac_no='" + acno + "'", null);
        return rs;
    }

    public String getBal(String acno) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Bal = "Not Found";
        Cursor rs = db.rawQuery("select * from memtrans where ac_no='" + acno + "'", null);
        if (rs.moveToFirst())
            Bal = rs.getString(4);
        Bal = BigDecimal.valueOf(Double.parseDouble(Bal)).setScale(2) + "";
        rs.close();
        return Bal;
    }

    //for loan bal
    public String getBaln(String acno) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Bal = "Not Found";
        Cursor rs = db.rawQuery("select * from memloans where ac_no='" + acno + "'", null);
        if (rs.moveToFirst())
            Bal = rs.getString(7);
        Bal = BigDecimal.valueOf(Double.parseDouble(Bal)).setScale(2) + "";
        rs.close();
        return Bal;
    }


    public Cursor getAlltrans(String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from memtrans where amount<>'" + amount + "'", null);
        return rs;
    }

    public Cursor getAllrepayment(String amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from memloans where amount<>'" + amount + "'", null);
        return rs;
    }


    public Cursor getDbUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from users where id='" + user + "'", null);
        return rs;
    }

    public Cursor getAllacc() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor rs = db.rawQuery("select * from memtrans where Amount<>'0'", null);
        return rs;
    }

    public Cursor getCashbal() {
        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor rs = db.rawQuery("select sum(Amount) from memtrans where Amount <> ?", new String[] { "0" } );
        Cursor rs = db.rawQuery("select sum(Amount) from memtrans", null);

        return rs;
    }

    public Cursor getCashln() {
        SQLiteDatabase db = this.getWritableDatabase();

        //Cursor rs = db.rawQuery("select sum(Amount) from memtrans where Amount <> ?", new String[] { "0" } );
        Cursor rs = db.rawQuery("select sum(Amount) from memloans", null);

        return rs;
    }

    public Cursor search(String keyword, String Group) {
        //String[] contact = new String[200];

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // int i=0;
            if (Group.isEmpty() || Group == "none") {
                cursor = db.rawQuery("select *,AC_NAME+' ('+AC_NO+')'+AMOUNT AS NOW from " + TABLE_NAME + " where " + Col2 + " like ?", new String[]{"%" + keyword + "%"});
            } else {
                cursor = db.rawQuery("select *,AC_NAME+' ('+AC_NO+')'+AMOUNT AS NOW from " + TABLE_NAME + " where " + Col2 + " like ? and " + "Upper("+Col5+")" + " = ?", new String[]{"%" + keyword + "%", Group.trim().toUpperCase()});

            }
        } catch (Exception e) {
            cursor = null;
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXX"+e.getMessage());
        }
        return cursor;
    }

    public Cursor searchbygrp(String keyword, String Group) {

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // check for empty group name and convert to all
            if (Group.isEmpty() || Group == "none") {
                cursor = db.rawQuery("select *,round(EXPECTED-AMOUNT,2) AS EXPECTEDBAL,AC_NAME+' ('+AC_NO+')'+AMOUNT AS NOW from " + TABLE_NAMELN + " where " + Coln2 + " like ?", new String[]{"%" + keyword + "%"});

            } else {
                cursor = db.rawQuery("select *,round(EXPECTED-AMOUNT,2) AS EXPECTEDBAL,AC_NAME+' ('+AC_NO+')'+AMOUNT AS NOW from " + TABLE_NAMELN + " where " + Coln2 + " like ? and Upper(GroupID)=?", new String[]{"%" + keyword + "%", Group.trim().toUpperCase()});
            }
        } catch (Exception e) {
            cursor = null;
        }
        return cursor;
    }
}

