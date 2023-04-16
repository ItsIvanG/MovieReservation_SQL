import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
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
    private double ticketDiscount;

    private double ticketDiscountSum;
    private List<Integer> paymentIDs = new ArrayList<Integer>();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public ConfirmPurchase(List<String> seats, int ShowID, String m, Header h, List<String> type){

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
                    cinemaRate=rsCinema.getDouble("cinema_rate");
                    System.out.println("CINEMA DESC:____"+rsCinema.getString(1)+"\nRATE: "+cinemaRate);
                }
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        int seatForEachDisplay=0;

        for (String x:
             seats) {
            seatsOrderingLabel.setText(seatsOrderingLabel.getText()+x+" - "+type.get(seatForEachDisplay)+", ");
            if(type.get(seatForEachDisplay).equals("REG")){
                ticketDiscountSum++;
            } else if (type.get(seatForEachDisplay).equals("DISC")) {
                ticketDiscountSum+=0.8;
            }
            seatForEachDisplay++;
        }

        showIDLabel.setText(String.valueOf(ShowID));


        ////calculate price
        df.setRoundingMode(RoundingMode.UP);

        ticketsTotalPrice = Double.parseDouble(df.format(seats.size()*(moviePrice*cinemaRate*(ticketDiscountSum/seats.size()))));

        System.out.println("SEAT SIZE: "+seats.size());
        System.out.println("MOVIE PRICE: "+moviePrice);
        System.out.println("CINEMA RATE: "+cinemaRate);
        System.out.println("TICKET DISCOUNT SUM: "+ticketDiscountSum);

        priceLabel.setText(priceLabel.getText()+ticketsTotalPrice);
        ticketsLabel.setText(ticketsLabel.getText()+seats.size()+"x");

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

                    pst = conn.prepareStatement("insert into payment(payment_id,mode_of_payment,account_id) values (?,?,?)");
                    pst.setString(1,Integer.toString(paymentID));
                    pst.setString(2,paymentMethods[paymentMethodInt]);
                    pst.setString(3, h.accountid);
                    pst.execute();
                    System.out.println("PAYMENT RECORD ADDED");
                    int seatindex=0;
                    for (String seats:
                         seats) {

                        if(type.get(seatindex).equals("REG")){
                            ticketDiscount=1;
                        } else if (type.get(seatindex).equals("DISC")) {
                            ticketDiscount=0.8;
                        }

                        pst = conn.prepareStatement("insert into ticket(seat_id,show_id,payment_id,ticket_type) values (?,?,?,?)");
                        pst.setString(1,seats);
                        pst.setString(2, Integer.toString(ShowID));
                        pst.setString(3,Integer.toString(paymentID));
                        pst.setString(4, type.get(seatindex));
                        pst.execute();
                        seatindex++;
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
