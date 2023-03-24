import javax.swing.*;
import java.awt.*;

public class MovieMenuScroll {
    public JScrollPane scrollPane;
    private JPanel panel;

    public MovieMenuScroll(Header h){
        panel.setLayout(new GridLayout(0,1));
        panel.add(new MovieMenu(h).panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    }
}
