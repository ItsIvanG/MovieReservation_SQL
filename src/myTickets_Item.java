import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class myTickets_Item {
    public JPanel Panel;
    private JLabel movieTitleLabel;
    private JLabel detailsLabel;
    private JPanel itemPanel;
    private int thisTicketIndex;
    private myTickets m;

    public myTickets_Item(String title,String details, myTickets m,int index){
        this.m=m;
        thisTicketIndex=index;
        movieTitleLabel.setText(title);
        detailsLabel.setText(details);

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m.seeTicketDetails(index);

            }
        });
    }
    public void changeColor(){
        if(thisTicketIndex==m.ticketIndex){
            itemPanel.setBackground(new Color(162,20,20));
        } else{
            itemPanel.setBackground( Color.decode("#191919"));
        }
    }
}
