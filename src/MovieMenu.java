import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class MovieMenu {
    JPanel panel = new JPanel();

//    JFrame frame = new JFrame();
    public Header h;
    public MovieMenu(Header x) {

        h=x;
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        panel.setLayout(new GridLayout(0,2));


//        frame.setLayout(new GridLayout(0,2));
//
//        frame.setContentPane(panel);
//        frame.setSize(1000,800);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setTitle("Now Showing");
//        frame.setVisible(true);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);

            PreparedStatement pst = conn.prepareStatement("Select * from movie");

            ResultSet rs = pst.executeQuery();


            while(rs.next()){

                panel.add(new MovieItem(rs.getString(2),"<html>"+rs.getString(3)+"</html>",rs.getString(1), h,rs.getString("movie_poster")).movieItemPanel);
                System.out.println("\n"+rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));

            }

        } catch (Exception e){
            System. out.println(e.getMessage());
        }

    }
}
