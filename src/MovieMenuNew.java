import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieMenuNew {
    JPanel panel;
    private JPanel moviesPanelGrid;
    private JPanel headerPanel;
    private JLabel dateTime;
    private Header h;

    public MovieMenuNew(Header x){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();


        h=x;
        moviesPanelGrid.setLayout(new GridLayout(0,2));


        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);

            PreparedStatement pst = conn.prepareStatement("Select * from movie");

            ResultSet rs = pst.executeQuery();



            while(rs.next()){
                new File("C:\\MovieReserv\\").mkdirs();
                File moviePosterDisk = new File("C:\\MovieReserv\\"+rs.getString(1));
                FileOutputStream fos = new FileOutputStream(moviePosterDisk);

                if(rs.getObject("movie_poster")!=null){
                    InputStream moviePosterIS = rs.getBinaryStream("movie_poster");
                    System.out.println("MOVIEPOSTERIS: "+moviePosterIS.available());
                    int mpx;

                    while((mpx = moviePosterIS.read()) != -1)
                    {
                        fos.write(mpx);
                    }
                }


                fos.flush();
                fos.close();



                moviesPanelGrid.add(new MovieItem(rs.getString(2),"<html>"+rs.getString(3)+"</html>",rs.getString(1), h,rs.getInt("duration_minutes"),rs.getString("movie_rating"), new utilToSqlDate().convertJavaDateToSqlDate(date)).movieItemPanel);
                System.out.println("\n"+rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4));

            }

        } catch (Exception e){
            System. out.println(e.getMessage());
        }

        dateTime.setText(dateFormat.format(date));

    }
}
