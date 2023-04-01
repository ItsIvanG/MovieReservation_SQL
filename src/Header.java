import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Header {
    private JPanel panel;
    private JPanel contentPanel;
    public JLabel customerNameLabel;
    private JButton signInButton;
    private JButton registerButton;
    private JButton signOutButton;
    private JButton myTicketsButton;
    private JButton myPurchasesButton;
    private JButton manageMoviesButton;
    private JButton manageShowsButton;
    private JButton manageHallsButton;

    public String movieCode;
    public String customerEmail="";
    public String customerName;
    public String customerContactNo;

    public List<String> purchasingSeats=new ArrayList<String>();
    public int selectedShowID;
    public boolean isAdmin=false;
    public JFrame frame;
    public String accountid;

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
    public Header() {

//        try {
//            // Set System L&F
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//
//        }
//        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
//               IllegalAccessException e) {
//            System.out.println(e.getMessage());
//        }

        setUIFont((new javax.swing.plaf.FontUIResource("Inter", Font.PLAIN, 14))); // SET UI FONT
        UIManager.put("Button.background", Color.white); //SET BUTTON BG TO WHITE


//        UIManager.put("Panel.background", Color.white);

        Header h = this;

        Login loginstart = new Login(h);
        ///------------------------------------/AUTOLOGIN
        loginstart.tryLogin(h, "ivan.gonzales@gmail.com","pogi");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               contentPanel.remove(0);
               contentPanel.add(new Register(h).panel);
               contentPanel.revalidate();
               contentPanel.repaint();
            }
        });
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.contentPanel.remove(0);
                h.contentPanel.add(new Login(h).panel);
                h.contentPanel.revalidate();
                h.contentPanel.repaint();
            }
        });
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerEmail="";
                customerName="";
                customerNameLabel.setText("");
                customerContactNo="";
                accountid="";
                isAdmin=false;

                checkLoginStatus(h);
                seeMovieMenu(h);
            }
        });
        myTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeTickets(h);
            }
        });
        myPurchasesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seePurchases(h);
            }
        });
        manageMoviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeMovieManager();
            }
        });
        manageShowsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeShowManager();
            }
        });
        manageHallsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.seeHallManager();
            }
        });
    }

    public static void main(String[] args) {


        Header h = new Header();
        h.frame = new JFrame();


        h.checkLoginStatus(h);

        h.frame.setSize(1000,800);
        h.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        h.frame.setTitle("Now Showing");
        h.frame.setVisible(true);
        h.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        h.frame.setContentPane(h.panel);
        h.contentPanel.setLayout(new GridLayout(0,1));

        h.contentPanel.add(new MovieMenuScroll(h).scrollPane);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();


    }

    public void seeMovieDetails( Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new MovieDetails(movieCode,h).panel);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();
    }

    public void seeMovieMenu(Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new MovieMenuScroll(h).scrollPane);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();
    }

    public void checkLoginStatus(Header h){
        if(customerEmail.equals("")){
            signOutButton.setVisible(false);
            myTicketsButton.setVisible(false);
            signInButton.setVisible(true);
            registerButton.setVisible(true);
            myPurchasesButton.setVisible(false);
        } else{
            signOutButton.setVisible(true);
            myTicketsButton.setVisible(true);
            signInButton.setVisible(false);
            registerButton.setVisible(false);
            myPurchasesButton.setVisible(true);
        }
        if(isAdmin){
            manageHallsButton.setVisible(true);
            manageMoviesButton.setVisible(true);
            manageShowsButton.setVisible(true);
        }else{
            manageHallsButton.setVisible(false);
            manageMoviesButton.setVisible(false);
            manageShowsButton.setVisible(false);
        }
    }

    public void confirmPurchaseScreen(Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new ConfirmPurchase(purchasingSeats, selectedShowID,movieCode, h).panel);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();
    }

    public void seeTickets(Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new myTickets(h, accountid).panel);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();
    }

    public void seePurchases(Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new myPurchases(h, accountid).panel);
        h.contentPanel.revalidate();
        h.contentPanel.repaint();
    }

    public void seeMovieManager(){
        this.contentPanel.remove(0);
        this.contentPanel.add(new MovieManage(this).panel);
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }
    public void seeShowManager(){
        this.contentPanel.remove(0);
        this.contentPanel.add(new ShowManage(this).panel);
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }
    public void seeHallManager(){
        this.contentPanel.remove(0);
        this.contentPanel.add(new HallManager(this).panel);
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }
}

