import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails {
    private JLabel movieTitle;
    private JLabel movieDesc;
    public JPanel panel;
    private JButton backButton;
    private JComboBox dateBox;
    private JComboBox timeBox;
    private JComboBox hallBox;
    private JButton confirmButton;
    private JPanel seatsPanel;
    private JLabel selectedSeatsLabel;
    private JLabel showIDLabel;
    private JLabel ticketPriceLabel;
    private JLabel timeLabel;
    private JLabel priceLabel;
    private JLabel moviePhoto;
    private JLabel selectedSeatsVisibleLabel;
    private JLabel movieDurationLabel;

    public String movieCode;

    public Header head;

    private MovieDetails m = this;
    private int seatsPerRow;

    public List<String> dateList=new ArrayList<String>();
    public List<String> timeList=new ArrayList<String>();
    public List<String> hallList=new ArrayList<String>();
    private String[] rowCodes = {"A","B","C","D","E","F","G","H","I","J"};
    public List<String> selectedSeats=new ArrayList<String>();
    private int noOfSeats;
    public GridLayout seatsLayout = new GridLayout(0,10);
    public int ShowID;
    private double rateAdd;
    private double moviePrice;

    private double ticketsTotalPrice;
    public MovieDetails(String a, Header h){
        head=h;
        movieCode=a;
        selectedSeatsLabel.setVisible(false);


        try{
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            // GET MOVIE DETAILS
            PreparedStatement sql = conn.prepareStatement("Select * from movie where movie_id=?");
            sql.setString(1,movieCode);
            ResultSet rs = sql.executeQuery();
            while(rs.next()){
                moviePhoto.setText("<html><img src=\"file:C:\\MovieReserv\\"+a+"\" width=220 height=317></html>");
                movieTitle.setText(rs.getString(2));
                movieDesc.setText("<html>"+rs.getString(3)+"</html>");
                moviePrice=rs.getDouble("movie_price");
                movieDurationLabel.setText(dateTimeConvert.minutesToHours(rs.getInt("duration_minutes")));
                System.out.println("\n"+rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));
            }
            // GET SHOW DATES
            sql = conn.prepareStatement("Select distinct show_date from show_time where movie_id=?");
            sql.setString(1,movieCode);
            rs = sql.executeQuery();
            while(rs.next()){
                dateList.add(rs.getString(1));
                dateBox.addItem(dateTimeConvert.toShortDate(rs.getDate(1)));
            }
            System.out.println(dateList);
        } catch (Exception e){
            System. out.println(e.getMessage());
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                head.seeMovieMenu(head);
            }
        });
        dateBox.addActionListener(new ActionListener() { ////// GET SHOW TIMES
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DATE: "+dateList.get(dateBox.getSelectedIndex()));
                timeBox.removeAllItems();
                timeList.clear();
                try{
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                    PreparedStatement sql = conn.prepareStatement("Select distinct show_time from show_time where movie_id=? and show_date=?");
                    sql.setString(1,movieCode);
                    sql.setString(2,dateList.get(dateBox.getSelectedIndex()));
                    ResultSet rs = sql.executeQuery();
                    while(rs.next()){

                        timeList.add(rs.getString(1));
                        timeBox.addItem(dateTimeConvert.toShortTime(rs.getTime(1)));

                    }
                }catch (Exception x){
                    System. out.println(x.getMessage());
                }
                System.out.println(timeList);


            }
        });
        timeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { ////GET CINEMAS
                System.out.println("GETTING CINEMAS");
                hallBox.removeAllItems();
                hallList.clear();

                try{
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                    PreparedStatement pst = conn.prepareStatement("Select cinema_hallid,cinema_description from cinema_room where cinema_hallid=(Select cinema_hallid from show_time where movie_id=? and show_date=? and show_time=?)");
                    pst.setString(1,movieCode);
                    pst.setString(2,dateList.get(dateBox.getSelectedIndex()) );
                    pst.setString(3, timeList.get(timeBox.getSelectedIndex()));
                    ResultSet rs = pst.executeQuery();

                    while(rs.next()){
                        hallList.add(rs.getString(1));
                        hallBox.addItem(rs.getString(2));
                        System.out.println("SUBQUERY SUCCESS! CINEMA DESC: "+rs.getString(2));
//                        /////////////list to box w description
//                        System.out.println("LAST CINEMA HALL ADDED: "+hallList.get(hallList.size()-1));
////                        String getCinemaDescCommand = "Select cinema_description from cinema_room where cinema_hall='"+hallList.get(hallList.size()-1)+"'";
//                        PreparedStatement pstCinemaHall = conn.prepareStatement("Select cinema_description from cinema_room where cinema_hallid=?");
//                        pstCinemaHall.setString(1, hallList.get(hallList.size()-1));
//                        ResultSet rsCinemaHall = pstCinemaHall.executeQuery();
//                        while(rsCinemaHall.next()){
//                            hallBox.addItem(rsCinemaHall.getString(1));
//                        }
                    }
                    System.out.println(hallList);
                }catch (Exception x){
                    System. out.println(x.getMessage());
                }
            }
        });
        hallBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //////// GENERATE SEATS
                selectedSeats.clear();
                caluclatePrice();
                try
                {
                    // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
//                    Statement st = conn.createStatement();
                    ///GET CINEMA RATE
//                    PreparedStatement sql = conn.prepareStatement("Select rateAdd from cinema_room where cinema_hall=?");
//                    sql.setString(1,hallList.get(hallBox.getSelectedIndex()));
//                    ResultSet rs = sql.executeQuery();
//                    while(rs.next()){
//                        rateAdd = rs.getDouble(1);
//                    }

                    ticketPriceLabel.setText("Ticket price: ₱"+moviePrice);


                    PreparedStatement sql = conn.prepareStatement("Select * from cinema_room where cinema_hallid=?");
                    sql.setString(1,hallList.get(hallBox.getSelectedIndex()));
                    ResultSet rs = sql.executeQuery();
                    while(rs.next()){

                        noOfSeats=rs.getInt("no_of_seats");
                        seatsPerRow=rs.getInt("seatsperrow");
                    }
                    System.out.println("SELECTED CINEMA: "+hallList.get(hallBox.getSelectedIndex())+" WHERE NO. OF SEATS: "+noOfSeats+" AND SEATSPERROW: "+seatsPerRow);
                    seatsPanel.removeAll();
                    seatsLayout = new GridLayout(0,seatsPerRow);
                    seatsPanel.setLayout(seatsLayout);
                    seatsPanel.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));

                    ///SET SHOWID
                    sql = conn.prepareStatement("Select show_id from show_time where cinema_hallid=? and movie_id=? and show_date=? and show_time=?");
                    sql.setString(1,hallList.get(hallBox.getSelectedIndex()));
                    sql.setString(2,movieCode);
                    sql.setString(3,dateList.get(dateBox.getSelectedIndex()));
                    sql.setString(4,timeList.get(timeBox.getSelectedIndex()));
                    rs = sql.executeQuery();
                    while(rs.next()){

                        ShowID=rs.getInt(1);

                    }
                    System.out.println("SHOW ID: "+ShowID);
                    showIDLabel.setText("ShowID: "+Integer.toString(ShowID));


                }catch (Exception x){
                    System. out.println(x.getMessage());
                }

                /////ADD SEATS TO PANEL

                int currentSeat=0;
                int currentRow=0;
                int currentSeatReset=0;
                while(currentSeat<noOfSeats){
                    String seatIDdebug = rowCodes[currentRow]+(currentSeatReset+1);
                    seatsPanel.add( new SeatButton(seatIDdebug,m, ShowID).panel);
                    System.out.println(seatIDdebug+" added");
                    currentSeat++;
                    currentSeatReset++;

                    if(currentSeat%seatsPerRow==0&&currentSeat!=0){
                        currentRow++;
                        currentSeatReset=0;
                    }
                }
//                for (int i = 0; i < noOfSeats/seatsPerRow; i++) {
//                    for (int x = 0; x < seatsPerRow; x++) {
//
//                        seatsPanel.add(new SeatButton(seatIDdebug,m, ShowID).panel);
//                        System.out.println(seatIDdebug+" added");
//                    }
//                }

                seatsPanel.revalidate();
                seatsPanel.repaint();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!h.customerEmail.isEmpty()) {
                    if(!selectedSeats.isEmpty()){
                    h.purchasingSeats = selectedSeats;
                    h.selectedShowID = ShowID;
                    h.confirmPurchaseScreen(h);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select atleast one seat!");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Please log in!");
                }
            }
        });
    }
    public void addSeatToCart(String seatID){
        System.out.println("ADDED TO CART, SEATiD: "+seatID);
        selectedSeats.add(seatID);
        selectedSeatsLabel.setText("");
        for (String x:
                selectedSeats) {
            selectedSeatsLabel.setText(selectedSeatsLabel.getText()+x+", ");
        }
        selectedSeatsVisibleLabel.setText("<html>Selected seats: "+selectedSeatsLabel.getText()+"</html>");
        caluclatePrice();

    }
    public void removeSeatFromCart(String seatID){
        System.out.println("REMOVED FROM CART, SEATiD: "+seatID);
        selectedSeats.remove(seatID);
        selectedSeatsLabel.setText("");
        for (String x:
                selectedSeats) {
            selectedSeatsLabel.setText(selectedSeatsLabel.getText()+x+", ");
        }
        selectedSeatsVisibleLabel.setText("<html>Selected seats: "+selectedSeatsLabel.getText()+"</html>");
        caluclatePrice();

    }

    public void caluclatePrice(){
        ////calculate price
        ticketsTotalPrice = selectedSeats.size()*(moviePrice);
        priceLabel.setText("Total price: ₱"+ticketsTotalPrice);

    }

}

