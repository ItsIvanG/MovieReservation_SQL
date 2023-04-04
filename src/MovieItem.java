
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MovieItem{
    private JLabel movieTitle;
    private JButton openMovie;
    private JLabel movieDesc;
    public JPanel movieItemPanel;
    private JLabel movieCodeLabel;
    private JLabel moviePhoto;

    public Header h;
    public MovieItem(String a, String b, String m, Header x){
        h = x;
        movieTitle.setText(a);
        movieDesc.setText(b);
        movieCodeLabel.setText(m);
        moviePhoto.setText("<html><img src=\"file:C:\\MovieReserv\\"+m+"\" width=220 height=317></html>");

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
