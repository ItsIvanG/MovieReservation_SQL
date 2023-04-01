import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfirmPurchase {
    public JPanel panel;
    private JLabel seatsOrderingLabel;
    private JLabel showIDLabel;
    private JButton backButton;
    private JLabel movieLabel;
    private JLabel dateLabel;
    private JLabel cinemaHallLabel;
    private JLabel timeLabel;
    private JButton confirmButton;
    private JLabel priceLabel;
    private JLabel ticketsLabel;
    private JRadioButton overTheCounterRadioButton;
    private JRadioButton creditCardRadioButton;
    private JRadioButton GCashRadioButton;
    private String cinemaHallCode;
    private double cinemaRate;
    private double ticketsTotalPrice;
    private double moviePrice;
    private int paymentID;
    private String[] paymentMethods = {"COUNTER","CREDIT","GCASH"};
    private int paymentMethodInt=0;
    private List<Integer> paymentIDs = new ArrayList<Integer>();
    public ConfirmPurchase(List<String> i, int ShowID, String m, Header h){

        /////get movie details
        try{
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("Select * from MOVIE where MOVIE_ID=?");

            pst.setString(1, m);

            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                movieLabel.setText(rs.getString(2));
                moviePrice = rs.getDouble("movie_price");

            }

            ////// get show details
            pst = conn.prepareStatement("select * from show_time where show_id=?");
            pst.setString(1, Integer.toString(ShowID));
            rs = pst.executeQuery();

            PreparedStatement pstCinema;
            ResultSet rsCinema;

            while(rs.next()){
                dateLabel.setText(dateTimeConvert.toShortDate(rs.getDate(4)));
                timeLabel.setText(dateTimeConvert.toShortTime(rs.getTime(3)));

                //cinema description
                cinemaHallCode=rs.getString(5);
                pstCinema=conn.prepareStatement("Select * from cinema_room where cinema_hallid=?");
                pstCinema.setString(1,cinemaHallCode);
                rsCinema = pstCinema.executeQuery();
                while (rsCinema.next()) {
                    cinemaHallLabel.setText(rsCinema.getString("cinema_description"));
//                    cinemaRate=rsCinema.getDouble("rateAdd");
                    System.out.println("CINEMA DESC:____"+rsCinema.getString(1)+"\nRATE: "+cinemaRate);
                }
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        for (String x:
             i) {
            seatsOrderingLabel.setText(seatsOrderingLabel.getText()+x+", ");
        }

        showIDLabel.setText(String.valueOf(ShowID));


        ////calculate price
        ticketsTotalPrice = i.size()*(moviePrice);
        priceLabel.setText(priceLabel.getText()+ticketsTotalPrice);
        ticketsLabel.setText(ticketsLabel.getText()+i.size()+"x");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieDetails(h);
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    PreparedStatement pst;
                    ResultSet rs;
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);

                    pst = conn.prepareStatement("select payment_id from payment");
                    rs = pst.executeQuery();
                    while (rs.next()){
                        paymentIDs.add(rs.getInt(1));
                    }
                    Collections.sort(paymentIDs);
                    if(!paymentIDs.isEmpty()){
                        paymentID=paymentIDs.get(paymentIDs.size()-1)+1;
                    }else{
                        paymentID=1;
                    }
                    System.out.println("PaymentID: "+paymentID);

                    ///insert into payment table
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    pst = conn.prepareStatement("insert into payment(payment_id,mode_of_payment,payment_amount,account_id) values (?,?,?,?)");
                    pst.setString(1,Integer.toString(paymentID));
                    pst.setString(2,paymentMethods[paymentMethodInt]);
                    pst.setString(3,Double.toString(ticketsTotalPrice));
                    pst.setString(4, h.accountid);
                    pst.execute();
                    System.out.println("PAYMENT RECORD ADDED");

                    for (String seats:
                         i) {
                        pst = conn.prepareStatement("insert into ticket(seat_id,show_id,payment_id,ticket_price) values (?,?,?,?)");
                        pst.setString(1,seats);
                        pst.setString(2, Integer.toString(ShowID));
                        pst.setString(3,Integer.toString(paymentID));
                        pst.setString(4,Double.toString(moviePrice));
                        pst.execute();
                    }
                    JOptionPane.showMessageDialog(null, "Purchase success.");
                    h.seeTickets(h);
                } catch(Exception x){
                    System.out.println(x.getMessage());
                }
            }
        });
        overTheCounterRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentMethodInt=0;
            }
        });
        creditCardRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentMethodInt=1;
            }
        });
        GCashRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paymentMethodInt=2;
            }
        });
    }
}
