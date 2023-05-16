
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class MovieItem{
    private JLabel movieTitle;
    private JButton openMovie;
    private JLabel movieDesc;
    public JPanel movieItemPanel;
    private JLabel movieCodeLabel;
    private JLabel moviePhoto;
    private JLabel movieDurationLabel;
    private JPanel ratingPanel;
    private JLabel ratingLabel;
    private JPanel timePanel;
    private boolean titleCut=false;
    private int titleCutLength=25;

    public Header h;
    public MovieItem(String a, String b, String m, Header x,int duration, String rating, Date today){
        h = x;
        ratingLabel.setText(rating);
        movieTitle.setText(a);
        movieDesc.setText(b);
        movieCodeLabel.setText(m);
        moviePhoto.setText("<html><img src=\"file:C:\\MovieReserv\\"+m+"\" width=220 height=317></html>");
        movieDurationLabel.setText(dateTimeConvert.minutesToHours(duration));

        if(movieTitle.getText().length()>titleCutLength&& !titleCut){
            movieTitle.setText(movieTitle.getText().substring(0,titleCutLength)+"...");
            titleCut=true;
        }

        //SET RATING PANEL BG

        if (rating.startsWith("R")){
            ratingPanel.setBackground(Color.decode("#CC0E26"));
        } else if (rating.startsWith("PG")) {
            ratingPanel.setBackground(Color.decode("#028DD3"));
        }

        timePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        try {
            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username, connectionClass.password);
            // GET MOVIE DETAILS
            PreparedStatement sql = conn.prepareStatement("select distinct show_time from show_time where movie_id=? and show_date=?");
            sql.setString(1,m);
            sql.setString(2, dateTimeConvert.toShortDate(today));
            ResultSet rs = sql.executeQuery();
            while (rs.next()){
                timePanel.add(new menuTimeItem(dateTimeConvert.toShortTime(rs.getTime(1))).panel);
            }
//            sql.setString(1, movieCode);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        openMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked on "+movieTitle.getText());
                h.movieCode = movieCodeLabel.getText();
                System.out.println("Current movie code: "+h.movieCode);
                h.seeMovieDetails(h);

            }
        });
    }
}
