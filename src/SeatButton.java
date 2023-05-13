import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static java.awt.Label.CENTER;

public class SeatButton {
    public int seatStatus=0;
    //0 = available, 1 = added to cart, 2 = discount, 3 = taken :<
    public JPanel panel;
    private JButton seatButton;
    private JLabel label;

    SeatButton(String SEATID, MovieDetails m, boolean taken){
//        seatButton.setText(SEATID);

        panel.remove(1);
        if (taken){
            seatButton.setBackground(Color.decode("#CC0E26"));
            seatStatus=3;
        }
        seatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(seatStatus==0&&m.ticketType==0){
                    m.addSeatToCart(SEATID);
                    seatButton.setBackground(Color.decode("#13BC57"));
                    seatStatus=1;
                }

                 else if(seatStatus==0&&m.ticketType==1){
                    m.addSeatToCart(SEATID);
                    seatButton.setBackground(Color.decode("#FFB600"));
                    seatStatus=1;
                }
                 else if (seatStatus==1) {
                    m.removeSeatFromCart(SEATID);
                    seatButton.setBackground(Color.decode("#494949"));
                    seatStatus=0;
                }
            }
        });
    }
    SeatButton(String type){
        if (type.equals("blank")) {
            panel.removeAll();
        } else {
            panel.remove(0);
            label.setText(type);

        }

    }
}
