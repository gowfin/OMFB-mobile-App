package gowfinmfbsoftwares.com;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class sqlpostingloan {


    private static String getcurrentDateAndTime() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    public void postsqlserver(Connection conn, String fsacno, String amount, String name, String username, String prevBal, String Product) {
        boolean posted;

        String dtt = getcurrentDateAndTime();
        String truncatname = "Cash";
        if (name.length() > 20) {
            truncatname = name.substring(0, 20);
        } else {
            truncatname = name;
        }


        if (dtt.length() < 10) {
            dtt = dtt.substring(0, 5) + "0" + dtt.substring(5);
        }
        ;// to take care of 01/03/2018


        String acno = "", glno = "";
        int n = 0;
        String ID;


        try {

            glno = fsacno.substring(0, 7);
            acno = fsacno.substring(8);
            conn.setAutoCommit(false);//for transaction posting

            String StrQuery = "select GLCode,intGL from Product where productid='" + Product + "'";
            String Qrylnsched = "select top 1 RepayWithInt/PrinRepay intrate,count from Loanschedule where Remark='P' and  LoanID='" + acno + "'";
            String loanGL = "";
            String IntGL = "";
            int repaycount = 1;
            double intrate = 1.0;
            try {
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet cur = stmt.executeQuery(StrQuery);
                cur.next();
                loanGL = cur.getString("GLCode") + "-" + glno.substring(0, 3);
                IntGL = cur.getString("intGL") + "-" + glno.substring(0, 3);
                cur = stmt.executeQuery(Qrylnsched);
                cur.next();
                repaycount = cur.getInt("count");
                intrate = cur.getDouble("intrate");

                cur.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            PreparedStatement pstmt;
            String pamount, bal;
            double intamount;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date cdate = new Date();
            String strnow = sdf.format(cdate);


            ID = acno;

            pamount = Double.parseDouble(amount) / intrate + "";
            intamount = Double.parseDouble(amount) - Double.parseDouble(pamount);
            bal = Double.parseDouble(prevBal) - Double.parseDouble(amount) + "";
            double ovapaid = Double.parseDouble(amount) - Double.parseDouble(prevBal);
            //check for full or partial payment or over payment
            if (Double.parseDouble(amount) > Double.parseDouble(prevBal)) {
                System.out.println("Transaction cancelled" + "\n" + "This Account cannot be Overpaid by " + BigDecimal.valueOf(ovapaid).setScale(2, RoundingMode.HALF_UP));
            } else {


                //convert amount to negative because you are lessing as below:
                double amnt = Double.parseDouble(pamount);
                String query3 = "update loans set OutstandingBal=OutstandingBal+" + BigDecimal.valueOf(amnt).setScale(2, RoundingMode.HALF_UP) + " where loanID='" + ID + "'";
                String query4 = ("insert into transactn (AccountID,tranid,Amount,DebitGL,CreditGL,Runningbal,ValueDate,DateEffective,custNo,StmtRef,BranchID,ChequeNbr,CreatedBy,transactionNbr)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                try {
                    //generating transaction number
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Random rand = new Random(timestamp.getTime());
                    int Rnum = rand.nextInt(999999);
                    String tranctno = strnow.substring(2, 4) + strnow.substring(5, 7) + strnow.substring(8, 10) + Rnum + "LRP";
                    //strnow=dtt;
                    conn.setAutoCommit(false);//for transaction posting

                    if (Math.abs(ovapaid) < 5) {
                        query3 = "update loans set OutstandingBal= 0.00 where loanID='" + ID + "'";
                    }
                    pstmt = conn.prepareStatement(query3);
                    pstmt.executeUpdate();

                    pstmt = conn.prepareStatement(query4);
                    pstmt.setString(1, ID);
                    pstmt.setString(2, "001"); //tranid
                    pstmt.setString(3, amount);
                    pstmt.setString(4, "11102-" + glno.substring(0, 3));
                    pstmt.setString(5, loanGL);

                    double curbal = Double.parseDouble(prevBal) / intrate - Double.parseDouble(pamount);//to get principal bal division is made

                    if (Math.abs(ovapaid) < 5) curbal = 0.00;
                    pstmt.setString(6, -curbal + "");
                    pstmt.setString(7, dtt);
                    pstmt.setString(8, dtt);

                    pstmt.setString(9, glno);
                    pstmt.setString(10, Product + " Mobile Payment " + truncatname);

                    pstmt.setString(11, name);
                    pstmt.setString(12, "NA");
                    pstmt.setString(13, username);
                    pstmt.setString(14, tranctno);
                    pstmt.executeUpdate();


//updating for interest
                    if (intamount != 0.0) {
                        pstmt = conn.prepareStatement(query4);
                        pstmt.setString(1, ID);
                        pstmt.setString(2, "011");
                        pstmt.setString(3, BigDecimal.valueOf(intamount).setScale(2, RoundingMode.HALF_UP) + "");
                        pstmt.setString(4, loanGL);
                        pstmt.setString(5, IntGL);
                        pstmt.setString(6, -curbal + "");
                        pstmt.setString(7, dtt);
                        pstmt.setString(8, dtt);
//pstmt.setString(8,strnow);
                        pstmt.setString(9, glno);
                        pstmt.setString(10, "Interest on Repayment");
//JOptionPane.showMessageDialog(this,jTextFieldDescription.getToolTipText());
                        pstmt.setString(11, name);
                        pstmt.setString(12, "NA");
                        pstmt.setString(13, username);
                        pstmt.setString(14, tranctno);
                        pstmt.executeUpdate();
                    }
                    double AmountD = Double.parseDouble(amount);
                    String query5 = "";
                    String qq = "";


                    while (AmountD > 0) {
                        String query55 = "select * from loanschedule where remark='P' and loanID='" + ID + "'";
                        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet current = stmt.executeQuery(query55);
                        current.next();
                        //check for presence of partial or none serviced condition
                        Double PplusInt, PrinNonserv, IntNonserv;
                        int count;
                        if (current.getString("status").equalsIgnoreCase("Partial")) {
                            PplusInt = current.getDouble("PrinRepay") - current.getDouble("servicedPrin") + current.getDouble("intRepay") - current.getDouble("servicedint");
                            PrinNonserv = current.getDouble("PrinRepay") - current.getDouble("servicedPrin");
                            IntNonserv = current.getDouble("intRepay") - current.getDouble("servicedInt");
                            count = current.getInt("count");//remain in thesame schedule,do not go next
                        } else {
                            PplusInt = current.getDouble("PrinRepay") + current.getDouble("intRepay");
                            PrinNonserv = current.getDouble("PrinRepay");
                            IntNonserv = current.getDouble("intRepay");
                            count = current.getInt("count");
                        }
                        if (AmountD >= PplusInt) {
                            query5 = "update loanschedule set status='Serviced',servicedprin=servicedprin+'" + PrinNonserv + "',"
                                    + "ServicedInt=ServicedInt+'" + IntNonserv + "',remark='D' where count='" + count + "'"
                                    + " and loanID='" + ID + "'";
                            //check for last schedule item to avoid errors of updating unexisting row
                            if (current.getDouble("RunningBal") > (current.getDouble("RepayWithInt") + 1)) {
                                qq = "update loanschedule set remark='P' where count='" + (current.getInt("count") + 1) + "'";
                            } else {
                                qq = "update loanschedule set remark='P' where count='" + (current.getInt("count")) + "'";

                            }
                            AmountD = AmountD - PplusInt;
                            //JOptionPane.showMessageDialog(this, "Amount= "+AmountD);
                        } else if (AmountD < PplusInt) {

                            if (intamount == 0.0)// takes care of principal repay without interest
                            {
                                double AmountRInt = IntNonserv * AmountD / PplusInt; // to calculate interest on remaining principal
                                AmountRInt = BigDecimal.valueOf(AmountRInt).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                AmountD = AmountD - AmountRInt;
                                query5 = "update loanschedule set status='Partial',servicedprin=servicedprin+'" + AmountD + "',"
                                        + "ServicedInt=ServicedInt+'" + AmountRInt + "',remark='P' where count='" + (current.getInt("count")) + "'"
                                        + " and loanID='" + ID + "'";

                                AmountD = AmountD - PplusInt;

                            } else if (AmountD > IntNonserv) {
                                double AmountRInt = IntNonserv * AmountD / PplusInt; // to calculate interest on remaining AmountD (principal+interest)
                                AmountRInt = BigDecimal.valueOf(AmountRInt).setScale(2, RoundingMode.HALF_UP).doubleValue();

                                double AmountR = AmountD - AmountRInt;
                                query5 = "update loanschedule set status='Partial',servicedprin=servicedprin+'" + AmountR + "',"
                                        + "ServicedInt=ServicedInt+'" + AmountRInt + "',remark='P' where count='" + (current.getInt("count")) + "'"
                                        + " and loanID='" + ID + "'";

                                AmountD = AmountD - PplusInt;

                                //JOptionPane.showMessageDialog(this, "Amount= "+AmountD);
                            } else {

                                query5 = "update loanschedule set status='Partial',"
                                        + "ServicedInt=ServicedInt+'" + AmountD + "',remark='P' where count='" + (current.getInt("count")) + "'"
                                        + " and loanID='" + ID + "'";

                                //JOptionPane.showMessageDialog(this, "Amount= "+AmountD);
                                AmountD = 0;
                                repaycount = repaycount + 1;
                            }
                        }
                        pstmt = conn.prepareStatement(query5);
                        pstmt.executeUpdate();
                        pstmt = conn.prepareStatement(qq);
                        pstmt.executeUpdate();

                    }

                    conn.commit();//transaction saved


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // try{conn.rollback();}catch(SQLException sqx){//throw new Exception.("Roll back failed: "+sqx.getMessage();)
                    //    System.out.println( e.getMessage());
                    // }
                }
            }


        } catch (Exception e) {

            System.out.println(e.getCause() + " and " + e.getMessage() + "Customer name: " + name + "\n A/C=" + glno + "-" + acno + " Amount=" + amount);
            posted = false;
        }


    }
}
