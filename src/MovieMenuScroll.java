import javax.swing.*;
import java.awt.*;

public class MovieMenuScroll {
    public JScrollPane scrollPane;
    private JPanel panel;
    private JLabel loadLabel;

    public MovieMenuScroll(Header h){

        panel.removeAll();
        panel.setLayout(new GridLayout(0,1));
        panel.add(new MovieMenu(h).panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    }
}
