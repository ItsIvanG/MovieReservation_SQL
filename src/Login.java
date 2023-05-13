import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton logInButton;
    public JPanel panel;
    private JButton backButton;
    private int attempts;

    public Login(Header h){

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(attempts<3){
                    tryLogin(h, emailField.getText(),new String(passwordField.getPassword()));
                } else{
                    JOptionPane.showMessageDialog(null, "Maximum amount of attempts reached. Try again later.","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(attempts<3){
                    tryLogin(h, emailField.getText(),new String(passwordField.getPassword()));
                } else{
                    JOptionPane.showMessageDialog(null, "Maximum amount of attempts reached. Try again later.","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void tryLogin(Header h,String email, String pass){
        System.out.println("Attempting login "+email+" | "+pass);
        try{
//            // Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("Select * from account where account_email=? and account_password=?");
            pst.setString(1, email);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();
            boolean found=false;
            while(rs.next()){
                JOptionPane.showMessageDialog(null, "Logged in as: "+rs.getString("account_fname"));
                System.out.println(rs.getString("account_fname")+" ID: "+rs.getString("account_id"));
                h.customerEmail=rs.getString("account_email");
                h.customerName=rs.getString("account_fname")+" "+rs.getString("account_lname");
                h.customerContactNo=rs.getString("account_mobileno");
                h.customerNameLabel.setText(h.customerName);
                h.isAdmin=rs.getBoolean("account_admin");
                h.accountid=rs.getString("account_id");

                h.seeMovieMenu(h);
                h.checkLoginStatus();

                found=true;
            }
            if(!found){
                attempts++;
                JOptionPane.showMessageDialog(null, "Credentials not found. Attempt "+attempts+"/3.","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception x){
            System. out.println(x.getMessage());
        }
    }
}


