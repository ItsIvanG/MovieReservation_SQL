import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class myPurchases {
    private JButton backButton;
    private JList purchaseList;
    public JPanel panel;
    private JLabel purchaseIDLabel;
    private JLabel purchaseDateLabel;
    private JLabel movieLabel;
    private JLabel timeLabel;
    private JLabel seatsLabel;
    private JLabel purchaseMethodLabel;
    private JLabel cinemaHallLabel;
    private JLabel totalPriceLabel;

    private DefaultListModel<String> purchaseListModel = new DefaultListModel<>();
    private List<Integer> purchaseIDs = new ArrayList<>();
    private List<String> seats = new ArrayList<>();

    public myPurchases(Header h,String accountid){
        purchaseList.setModel(purchaseListModel);

        try{
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("select * from payment where account_id=?");
            pst.setString(1, accountid);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                purchaseListModel.addElement(dateTimeConvert.toShortDate(rs.getDate("payment_datetime"))+" ("+rs.getString("payment_Id")+")");
                purchaseIDs.add(rs.getInt("payment_id"));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });
        purchaseList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("select changed "+purchaseList.getSelectedIndex());
                seats.clear();
                try {
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                    PreparedStatement pst = conn.prepareStatement("select * from payment\n" +
                            "join ticket on ticket.Payment_ID=payment.payment_id\n" +
                            "join show_time on show_time.show_id=ticket.show_id\n" +
                            "join cinema_room on cinema_room.cinema_hallID=show_time.cinema_hallID\n" +
                            "join movie on movie.movie_id=show_time.movie_id\n" +
                            "where payment.payment_id=?");
                    pst.setInt(1, purchaseIDs.get(purchaseList.getSelectedIndex()));
                    ResultSet rs = pst.executeQuery();
                    while(rs.next()){
                        System.out.println("RS payID: "+rs.getString("payment_id"));
                        purchaseIDLabel.setText("Purchase ID: "+purchaseIDs.get(purchaseList.getSelectedIndex()));
                        purchaseDateLabel.setText("Purchase Date: "+dateTimeConvert.toShortDate(rs.getDate("payment_datetime")));
                        purchaseMethodLabel.setText("Purchase Method: "+rs.getString("mode_of_payment"));
                        totalPriceLabel.setText("Total Price: ₱"+rs.getString("payment_amount"));
                        seats.add(rs.getString("seat_id"));

                        timeLabel.setText("Show date/time: "+dateTimeConvert.toShortDate(rs.getDate("show_Date"))+" "+dateTimeConvert.toShortTime(rs.getTime("show_time")));

                        movieLabel.setText("Movie: "+rs.getString("movie_name"));
                        cinemaHallLabel.setText("Cinema hall: "+rs.getString("cinema_description"));

                        /*
                        purchaseIDLabel.setText("Purchase ID: "+purchaseIDs.get(purchaseList.getSelectedIndex()));
                        purchaseDateLabel.setText("Purchase Date: "+dateTimeConvert.toShortDate(rs.getDate("payment_datetime")));
                        purchaseMethodLabel.setText("Purchase Method: "+rs.getString("mode_of_payment"));
                        totalPriceLabel.setText("Total Price: ₱"+rs.getString("payment_amount"));

                        PreparedStatement pstTickets = conn.prepareStatement("select * from ticket where payment_id=?");

                        pstTickets.setInt(1,purchaseIDs.get(purchaseList.getSelectedIndex()));
                        ResultSet rsTicket = pstTickets.executeQuery();
                        while(rsTicket.next()){
//                            showid=rsTicket.getInt("payment_id");
                            seats.add(rsTicket.getString("seat_id"));


                            PreparedStatement pstShow = conn.prepareStatement("select * from show_time where show_id=?");

                            pstShow.setInt(1,rsTicket.getInt("show_id"));
                            ResultSet rsShow = pstShow.executeQuery();
                            while(rsShow.next()){
                                PreparedStatement pstMovie = conn.prepareStatement("select * from movie where movie_id=?");
                                timeLabel.setText("Show date/time: "+dateTimeConvert.toShortDate(rsShow.getDate("show_Date"))+" "+dateTimeConvert.toShortTime(rsShow.getTime("show_time")));
                                pstMovie.setString(1,rsShow.getString("movie_id"));

                                ResultSet rsMovie = pstMovie.executeQuery();
                                while(rsMovie.next()){
                                    movieLabel.setText("Movie: "+rsMovie.getString("movie_name"));
                                }

                                PreparedStatement pstCinema = conn.prepareStatement("Select * from cinema_room where cinema_hallid=?");
                                pstCinema.setString(1,rsShow.getString("cinema_hallid") );
                                ResultSet rsCinema = pstCinema.executeQuery();
                                while (rsCinema.next()) {
                                    cinemaHallLabel.setText("Cinema hall: "+rsCinema.getString("cinema_description"));
                                }

                            }
                        }*/
                    }
                    seatsLabel.setText("Seats: ");
                    for (String x:
                         seats) {
                        seatsLabel.setText(seatsLabel.getText()+x+" ");
                    }
                }
                catch (Exception x){
                    System.out.println(x.getMessage());
                }
            }
        });
    }

}
