import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieManage {
    private JButton backButton;
    public JPanel panel;
    private JComboBox moviesCombobox;
    private JTextField movieTextField;
    private JTextArea movieDescField;
    private JTextField moviePriceField;
    private JLabel moviePosterLabel;
    private JTextField movieDurationField;
    private JTextField movieCodeField;
    private JButton deleteButton;
    private JButton browseIconButton;
    private JButton applyButton;
    private List<String> movieList = new ArrayList<>();
    private File moviePoster;
    private String moviePosterURL;
    private byte[] moviePosterByte;
    public MovieManage(Header h){
        System.out.println("HEADER: "+h);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });

        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("select * from movie");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                moviesCombobox.addItem("["+rs.getString("movie_id")+"] "+rs.getString("movie_name"));
                movieList.add(rs.getString("movie_Id"));
                File moviePosterDisk = new File("C:\\MovieReserv\\"+rs.getString(1));
                FileOutputStream fos = new FileOutputStream(moviePosterDisk);

                //GET MOVIEPOSTER STREAM

                if(rs.getObject("movie_poster")!=null){
                    InputStream moviePosterIS = rs.getBinaryStream("movie_poster");
                    System.out.println("MOVIEPOSTERIS: "+moviePosterIS.available());
                    int mpx;

                    while((mpx = moviePosterIS.read()) != -1)
                    {
                        fos.write(mpx);
                    }
                }
            }
            moviesCombobox.addItem("+ Add new movie...");
            System.out.println(movieList);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if(movieList.size()>0){
            viewMovieDetails(movieList.get(0));
        }


        moviesCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(moviesCombobox.getSelectedIndex()<movieList.size()){
                   viewMovieDetails(movieList.get(moviesCombobox.getSelectedIndex()));
               }else{
                   movieTextField.setText("");
                   movieDescField.setText("");
                   moviePriceField.setText("");
                   movieDurationField.setText("");
                   movieCodeField.setText("");
                   moviePosterLabel.setText("");
               }
            }
        });
        browseIconButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File newPoster = openDialogImg.openDialog(h.frame,moviePoster);
                moviePosterLabel.setText("<html><img src=\"file:"+newPoster+"\" width=220 height=317></html>");
                moviePoster = newPoster;
                moviePosterURL = moviePoster.getAbsolutePath();

                System.out.println("URL: "+moviePosterURL);
            }
        });
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(movieList.contains(movieCodeField.getText())){///////////////////////////UPDATE

                    try{
                        Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                        PreparedStatement pst = conn.prepareStatement("update movie set movie_name=?, movie_description=?, movie_price=?, duration_minutes=?, movie_poster=? where movie_id=?");
                        pst.setString(1, movieTextField.getText());
                        pst.setString(2, movieDescField.getText());
                        pst.setDouble(3, Double.parseDouble(moviePriceField.getText()));
                        pst.setInt(4,Integer.parseInt(movieDurationField.getText()));

                        FileInputStream fis = new FileInputStream(moviePoster);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buf=new byte[1024];
                        for(int readNum;(readNum=fis.read(buf))!=-1;){
                            bos.write(buf,0,readNum);

                        }
                        moviePosterByte=bos.toByteArray();

                        pst.setBytes(5,moviePosterByte); ////MOVIE POSTER
                        pst.setString(6, movieCodeField.getText());
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Movie code already exists! Updating existing record.");
                    } catch (Exception x){
                        System.out.println(x.getMessage());
                    }

                } else{///////////////////////////INSERT NEW
                    try{
                        Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                        PreparedStatement pst = conn.prepareStatement("insert into movie(movie_name, movie_description, movie_price, duration_minutes, movie_poster, movie_id) values(?,?,?,?,?,?)");
                        pst.setString(1, movieTextField.getText());
                        pst.setString(2, movieDescField.getText());
                        pst.setDouble(3, Double.parseDouble(moviePriceField.getText()));
                        pst.setInt(4,Integer.parseInt(movieDurationField.getText()));
                        pst.setString(5,moviePosterURL);
                        pst.setString(6, movieCodeField.getText());
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Movie code does not exist! Inserting new record.");
                        h.seeMovieManager();
                    } catch (Exception x){
                        System.out.println(x.getMessage());
                    }

                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
                    PreparedStatement pst = conn.prepareStatement("delete from movie where movie_id=?");
                    pst.setString(1,movieList.get(moviesCombobox.getSelectedIndex()));

                    pst.execute();
                    h.seeMovieManager();
                    JOptionPane.showMessageDialog(null, "Movie deleted.");
                }catch (Exception x){
                    System.out.println(x.getMessage());
                    if(x.getMessage().equals("UCAExc:::5.0.1 integrity constraint violation: foreign key no action; SHOW_TIME_MOVIESHOW_TIME table: SHOW_TIME")){
                        JOptionPane.showMessageDialog(null, "Movie already has show times associated and cannot be deleted.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
    public void viewMovieDetails(String id){
        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("select * from movie where movie_Id=?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                movieTextField.setText(rs.getString("movie_name"));
                movieDescField.setText(rs.getString("movie_description"));
                moviePriceField.setText(rs.getString("movie_price"));
                movieDurationField.setText(rs.getString("duration_minutes"));
                movieCodeField.setText(rs.getString("movie_id"));
//                moviePosterLabel.setIcon(new ImageIcon(rs.getString("movie_poster")));


                if(rs.getObject("movie_poster")!=null){
                    moviePosterLabel.setText("<html><img src=\"file:C:\\MovieReserv\\"+movieList.get(moviesCombobox.getSelectedIndex())+"\" width=220 height=317></html>");
                    System.out.println(" MOVIE POSTER SET ");
                } else{
                    moviePosterLabel.setText("");
                    System.out.println("NO MOVIE POSTER");
                }
            }

        } catch (Exception x){
            System.out.println(x.getMessage());
        }
    }
}
