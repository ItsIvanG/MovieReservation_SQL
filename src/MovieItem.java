
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
    private boolean titleCut=false;
    private int titleCutLength=25;

    public Header h;
    public MovieItem(String a, String b, String m, Header x,int duration, String rating){
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
