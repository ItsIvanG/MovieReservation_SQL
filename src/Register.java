import com.healthmarketscience.jackcess.ConstraintViolationException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;

public class Register {
    private JTextField fullnameField;
    private JTextField emailField;
    private JTextField contactnoField;
    private JPasswordField passwordField;
    private JPasswordField confirmPassField;
    private JButton signUpButton;
    public JPanel panel;
    private JButton backButton;
    private JLabel passwordValidLabel;
    private JLabel requiredMsg;
    private Header h;
    private boolean passwordValid=false;

    public Register(Header hhh){
        h=hhh;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeMovieMenu(h);
            }
        });


        fullnameField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                checkRequired();
            }
        });
        contactnoField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                checkRequired();
            }
        });
        emailField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                checkRequired();
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
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{ //INSERT TO CUSTOMER RECORD
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString);
                    PreparedStatement pst = conn.prepareStatement("INSERT INTO account(account_email,account_name,account_mobileno,password) VALUES (?,?,?,?)");
                    pst.setString(1,emailField.getText());
                    pst.setString(2,fullnameField.getText());
                    pst.setString(3,contactnoField.getText());
                    pst.setString(4,new String(passwordField.getPassword()));
                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Account successfuly registered! Please log in.");
                    hhh.seeMovieMenu(hhh);


                    }
                catch (Exception x){
                    System. out.println(x.getMessage());
                    if(x.getMessage().startsWith("UCAExc:::5.0.1 integrity constraint violation: unique constraint or index violation"))
                        JOptionPane.showMessageDialog(null, "Error registering. E-mail already exists!","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public void checkPassword(){
        if(Arrays.equals(passwordField.getPassword(), confirmPassField.getPassword()) && !new String(passwordField.getPassword()).equals("")){
            passwordValid=true;
        } else{
            passwordValid=false;
        }
        if(passwordValid){
            passwordValidLabel.setText("Password is valid.");
            passwordValidLabel.setForeground(new Color(0,150,0));
        } else{
            passwordValidLabel.setText("Password is invalid/does NOT match.");
            passwordValidLabel.setForeground(Color.RED);
        }
        checkRequired();
    }

    public void checkRequired(){
        if(!fullnameField.getText().equals("") && !emailField.getText().equals("")&& !contactnoField.getText().equals("")&&passwordValid){
            signUpButton.setEnabled(true);
            requiredMsg.setVisible(false);
        } else{
            signUpButton.setEnabled(false);
            requiredMsg.setVisible(true);
        }
    }

}
