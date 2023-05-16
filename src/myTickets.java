import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class myTickets {
    public JPanel panel;
    private JButton backButton;
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
    private JScrollPane ticketsScrollPane;
    private JPanel ticketListPanel;
    public List<Integer> ticketIDs=new ArrayList<Integer>();
    public List<myTickets_Item> ticketItemsArray=new ArrayList<myTickets_Item>();
    public List<String> ticketsMovies=new ArrayList<String>();
    public List<String> ticketsCinemas=new ArrayList<String>();
    public List<String> ticketsDateTime=new ArrayList<String>();
    public List<String> ticketsSeats=new ArrayList<String>();
    public List<String> ticketsPurchaseIDs=new ArrayList<String>();
    public List<String> ticketsPrices=new ArrayList<String>();

    public List<String> showIDs=new ArrayList<String>();
    private Header h;
    private int currentTickIndex;
    public int ticketIndex;
    public myTickets(Header h, String accountid){
        this.h=h;

        ticketListPanel.setLayout(new GridLayout(0,1));
        String movieName="";
        String cinemaHall="";
        String showDate="";
        String showTime="";
        String seatID="";
        ticketPanel.setVisible(false);
        ticketsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        try { //// GET TICKETS
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("select payment.Payment_id, ticket.ticket_type, ticket.ticket_number," +
                    "ticket.seat_id, ticket.show_id, movie.movie_price, movie.movie_name, cinema_room.cinema_rate," +
                    "cinema_room.cinema_description, show_time.show_date, show_time.show_time from TICKET\n" +

                    "join show_time on ticket.Show_ID=show_time.show_id\n" +
                    "join movie on show_time.movie_id=movie.movie_id\n" +
                    "join cinema_room on show_time.Cinema_hallID=cinema_room.Cinema_hallID\n" +
                    "join payment on ticket.Payment_ID=payment.payment_id where payment.account_id=?\n" +
                    "order by payment.payment_datetime desc");
            pst.setString(1,accountid);
            ResultSet rs = pst.executeQuery();
            System.out.println("rs success");
            while (rs.next()) {
                ticketsPurchaseIDs.add(rs.getString("Payment_ID"));

                if(rs.getString("ticket_type").equals("REG")){
                    ticketsPrices.add(String.valueOf(rs.getDouble("movie_price")*rs.getDouble("cinema_rate")));
                } else if (rs.getString("ticket_type").equals("DISC")) {
                    ticketsPrices.add(String.valueOf(rs.getDouble("movie_price")*rs.getDouble("cinema_rate")*0.8));
                }


                ticketIDs.add(rs.getInt("ticket_number"));
                showIDs.add(rs.getString("show_id"));
                ticketsSeats.add(rs.getString("seat_id"));
                seatID=rs.getString("seat_id");
                showDate=dateTimeConvert.toShortDate(rs.getDate("show_date"));
                showTime=dateTimeConvert.toShortTime(rs.getTime("show_time"));

                ticketsDateTime.add(showDate+" | "+showTime);

                movieName=rs.getString("movie_name");
                ticketsMovies.add(rs.getString("movie_name"));
                ticketsCinemas.add(rs.getString("cinema_description"));
                cinemaHall=rs.getString("cinema_description");
//                ticketListModel.addElement(movieName+" • "+cinemaHall+" • "+ showDate+" : "+showTime+" • Seat "+seatID);

                ticketItemsArray.add(new myTickets_Item(movieName,cinemaHall+" • "+ showDate+" : "+showTime+" • Seat "+seatID, this,currentTickIndex));
                ticketListPanel.add(ticketItemsArray.get(ticketItemsArray.size()-1).Panel);

                currentTickIndex++;
                /*
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
                        pstMovie=conn.prepareStatement("select cinema_description from cinema_room where cinema_hallid=?");
                        pstMovie.setString(1,rsShow.getString("cinema_hallid"));
                        rsMovie=pstMovie.executeQuery();
                        System.out.println("rsMovie2 success");
                        while(rsMovie.next()){
                            ticketsCinemas.add(rsMovie.getString(1));
                            cinemaHall=rsMovie.getString(1);
                        }

                        ticketListModel.addElement(movieName+" • "+cinemaHall+" • "+ showDate+" : "+showTime+" • Seat "+seatID);
                    }
                }*/

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


    }
    void seeTicketDetails(int i){
        ticketIndex=i;

        for (myTickets_Item a:
                ticketItemsArray) {
            a.changeColor();
        }

        ticketPanel.setVisible(true);

        ticketMovieLabel.setText(ticketsMovies.get(i));
        ticketCinemaLabel.setText(ticketsCinemas.get(i));
        ticketDateTimeLabel.setText(ticketsDateTime.get(i));
        ticketSeatLabel.setText(ticketsSeats.get(i));

        ticketFullNameLabel.setText(h.customerName);
        ticketEmailLabel.setText(h.customerEmail);
        ticketContactLabel.setText(h.customerContactNo);


        ticketIDLabel.setText("Ticket ID: "+ticketIDs.get(i));
        ticketPurchaseIDlabel.setText("Purchase ID: "+ticketsPurchaseIDs.get(i));
        ticketPriceLabel.setText("Ticket Price: ₱"+ticketsPrices.get(i));
        showIDlabel.setText("Show ID: "+showIDs.get(i));

        try{
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("Select * from payment where payment_id=?");
            pst.setString(1,ticketsPurchaseIDs.get(i));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ticketPurchaseDateTimeLabel.setText(rs.getString("Payment_datetime"));
                ticketPurchaseMethodLabel.setText(rs.getString("mode_of_payment"));

            }
        }catch (Exception x){
            System.out.println(x.getMessage());
        }
    }
}
