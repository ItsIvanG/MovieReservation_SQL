import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class myAccount {
    public JPanel panel;
    private JButton backButton;
    private JTextField fnameField;
    private JTextField contactnoField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPassField;
    private JButton applyChangesButton;
    private JLabel passwordValidLabel;
    private JLabel requiredMsg;
    private JTextField mnameField;
    private JTextField lnameField;
    private boolean passwordValid;
    myAccount(Header h){
        requiredMsg.setVisible(false);

        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username,connectionClass.password);
            PreparedStatement pst = conn.prepareStatement("Select * from account where account_id=?");
            pst.setString(1,h.accountid);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                fnameField.setText(rs.getString("account_fname"));
                mnameField.setText(rs.getString("account_mname"));
                lnameField.setText(rs.getString("account_lname"));
                contactnoField.setText(rs.getString("account_mobileno"));
                emailField.setText(rs.getString("account_email"));

            }

        }
        catch (Exception x){
            System. out.println(x.getMessage());
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });
        applyChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Arrays.equals(passwordField.getPassword(), "".toCharArray())){
                    try {
                        Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username, connectionClass.password);
                        PreparedStatement pst = conn.prepareStatement("update account set account_fname=?, account_mname=?, account_lname=?, account_mobileno=?, account_email=? where account_id=?");
                        pst.setString(1,fnameField.getText());
                        pst.setString(2,mnameField.getText());
                        pst.setString(3, lnameField.getText());
                        pst.setString(4, contactnoField.getText());
                        pst.setString(5, emailField.getText());
                        pst.setString(6,h.accountid);
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Account details successfully edited.");

                    } catch (Exception x){
                        System.out.println(x.getMessage());
                    }
                } else {
                    try {
                        Connection conn = DriverManager.getConnection(connectionClass.connectionString, connectionClass.username, connectionClass.password);
                        PreparedStatement pst = conn.prepareStatement("update account set account_fname=?, account_mname=?, account_lname=?, account_mobileno=?, account_email=?, account_password=? where account_id=?");
                        pst.setString(1,fnameField.getText());
                        pst.setString(2,mnameField.getText());
                        pst.setString(3, lnameField.getText());
                        pst.setString(4, contactnoField.getText());
                        pst.setString(5, emailField.getText());
                        pst.setString(6, new String(passwordField.getPassword()));
                        pst.setString(7,h.accountid);
                        pst.execute();
                        JOptionPane.showMessageDialog(null, "Account details successfully edited.");

                    } catch (Exception x){
                        System.out.println(x.getMessage());
                    }
                }
            }
        });
        passwordField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                checkPassword();
            }
        });
        confirmPassField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                checkPassword();
            }
        });

    }
    public void checkPassword(){

        if(Arrays.equals(passwordField.getPassword(), confirmPassField.getPassword())){
            passwordValid=true;
        } else{
            passwordValid=false;
        }
        if(passwordValid){
            if (!Arrays.equals(passwordField.getPassword(), "".toCharArray())){
                passwordValidLabel.setText("Password is valid.");
                passwordValidLabel.setForeground(new Color(0,150,0));
            } else {
                passwordValidLabel.setText("");
            }
        } else{
            passwordValidLabel.setText("Password is invalid/does NOT match.");
            passwordValidLabel.setForeground(Color.RED);
        }

        checkRequired();

    }

    public void checkRequired(){

        if(!fnameField.getText().equals("") &&!lnameField.getText().equals("") && !emailField.getText().equals("")&& !contactnoField.getText().equals("")&&passwordValid){
            applyChangesButton.setEnabled(true);
            requiredMsg.setVisible(false);
            passwordValidLabel.setText("");
        } else{
            applyChangesButton.setEnabled(false);
            requiredMsg.setVisible(true);
        }


    }
}
