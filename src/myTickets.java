import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class myTickets {
    public JPanel panel;
    private JButton backButton;
    private JList ticketList;
    private JLabel ticketMovieLabel;
    private JLabel ticketCinemaLabel;
    private JLabel ticketDateTimeLabel;
    private JLabel ticketSeatLabel;
    private JLabel ticketFullNameLabel;
    private JLabel ticketEmailLabel;
    private JLabel ticketContactLabel;
    private JLabel ticketIDLabel;
    private JLabel ticketPurchaseIDlabel;
    private JPanel ticketPanel;
    private JLabel ticketPurchaseDateTimeLabel;
    private JLabel ticketPurchaseMethodLabel;
    private JLabel ticketPriceLabel;
    private JLabel showIDlabel;
    private DefaultListModel<String> ticketListModel = new DefaultListModel<>();
    public List<Integer> ticketIDs=new ArrayList<Integer>();
    public List<String> ticketsMovies=new ArrayList<String>();
    public List<String> ticketsCinemas=new ArrayList<String>();
    public List<String> ticketsDateTime=new ArrayList<String>();
    public List<String> ticketsSeats=new ArrayList<String>();
    public List<String> ticketsPurchaseIDs=new ArrayList<String>();
    public List<String> ticketsPrices=new ArrayList<String>();

    public List<String> showIDs=new ArrayList<String>();
    public myTickets(Header h, String accountid){

        String movieName="";
        String cinemaHall="";
        String showDate="";
        String showTime="";
        String seatID="";
        ticketPanel.setVisible(false);
        ticketList.setModel(ticketListModel);
        try { //// GET TICKETS
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("select * from payment where account_id=?");
            pst.setString(1,accountid);
            ResultSet rs = pst.executeQuery();
            System.out.println("rs success");
            while (rs.next()) {


                PreparedStatement pstTicket = conn.prepareStatement("select * from ticket where payment_id=?");
                pstTicket.setString(1, rs.getString("Payment_ID"));
                ResultSet rsTicket = pstTicket.executeQuery();


                while(rsTicket.next()){
                    ticketsPurchaseIDs.add(rsTicket.getString("Payment_ID"));
                    ticketsPrices.add(rsTicket.getString("ticket_price"));

                    ticketIDs.add(rsTicket.getInt(1));
                    showIDs.add(rsTicket.getString("show_id"));
                    ///// get seat ID
                    seatID=rsTicket.getString("seat_id");
                    ticketsSeats.add(seatID);
                    System.out.println("rsTicket success");

                    PreparedStatement pstShow = conn.prepareStatement("select * from show_time where show_id=?");
                    pstShow.setString(1, rsTicket.getString("show_id"));
                    ResultSet rsShow = pstShow.executeQuery();
                    System.out.println("rsShow success");
                    while(rsShow.next()){
                        ///// set showdate/time
                        showDate=dateTimeConvert.toShortDate(rsShow.getDate("show_date"));
                        showTime=dateTimeConvert.toShortTime(rsShow.getTime("show_time"));

                        ticketsDateTime.add(showDate+" | "+showTime);

                        ///// get movie name
                        PreparedStatement pstMovie = conn.prepareStatement("select * from movie where movie_id=?");
                        pstMovie.setString(1, rsShow.getString("movie_id"));
                        ResultSet rsMovie = pstMovie.executeQuery();
                        System.out.println("rsMovie success");

                        while(rsMovie.next()){
                            movieName=rsMovie.getString("movie_name");
                            ticketsMovies.add(rsMovie.getString("movie_name"));
                        }
                        ///// get cinema hall
                        pstMovie=conn.prepareStatement("select cinema_description from cinema_room where cinema_hall=?");
                        pstMovie.setString(1,rsShow.getString("cinema_hall"));
                        rsMovie=pstMovie.executeQuery();
                        System.out.println("rsMovie2 success");
                        while(rsMovie.next()){
                            ticketsCinemas.add(rsMovie.getString(1));
                            cinemaHall=rsMovie.getString(1);
                        }

                        ticketListModel.addElement(movieName+" • "+cinemaHall+" • "+ showDate+" : "+showTime+" • Seat "+seatID);
                    }
                }

            }

        } catch(Exception e){
            System.out.println(e.getMessage());
        }


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });
        ticketList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                ticketPanel.setVisible(true);

                ticketMovieLabel.setText(ticketsMovies.get(ticketList.getSelectedIndex()));
                ticketCinemaLabel.setText(ticketsCinemas.get(ticketList.getSelectedIndex()));
                ticketDateTimeLabel.setText(ticketsDateTime.get(ticketList.getSelectedIndex()));
                ticketSeatLabel.setText(ticketsSeats.get(ticketList.getSelectedIndex()));

                ticketFullNameLabel.setText(h.customerName);
                ticketEmailLabel.setText(h.customerEmail);
                ticketContactLabel.setText(h.customerContactNo);


                ticketIDLabel.setText("Ticket ID: "+ticketIDs.get(ticketList.getSelectedIndex()));
                ticketPurchaseIDlabel.setText("Purchase ID: "+ticketsPurchaseIDs.get(ticketList.getSelectedIndex()));
                ticketPriceLabel.setText("Ticket Price: ₱"+ticketsPrices.get(ticketList.getSelectedIndex()));
                showIDlabel.setText("Show ID: "+showIDs.get(ticketList.getSelectedIndex()));

                try{
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                    PreparedStatement pst = conn.prepareStatement("Select * from payment where payment_id=?");
                    pst.setString(1,ticketsPurchaseIDs.get(ticketList.getSelectedIndex()));
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        ticketPurchaseDateTimeLabel.setText(rs.getString("Payment_datetime"));
                        ticketPurchaseMethodLabel.setText(rs.getString("mode_of_payment"));

                    }
                }catch (Exception x){
                    System.out.println(x.getMessage());
                }

            }
        });
    }
}
