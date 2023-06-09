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
    private JButton viewProfileButton;
    private JButton adminControlsButton;

    public String movieCode;
    public String customerEmail="";
    public String customerName;
    public String customerContactNo;

    public List<String> purchasingSeats=new ArrayList<String>();
    public List<String> purchasingSeatsType=new ArrayList<String>();
    public int selectedShowID;
    public boolean isAdmin=false;
    public JFrame frame;
    public String accountid;
    private JPopupMenu popup;


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
        UIManager.put("Button.background", Color.white); //SET BUTTON BG TO WHITE - LIGHTMODE
//        UIManager.put("Button.background", Color.decode("#383838")); //SET BUTTON BG TO BLACK - DARKMODE
//        UIManager.put("Button.foreground", Color.white); //SET BUTTON FG TO WHITE - DARKMODE
//        //DARKMODE \/  \/  \/
//        UIManager.put("Panel.background", Color.decode("#262626"));
//        UIManager.put("Panel.foreground", Color.white);
//        UIManager.put("ComboBox.background", Color.decode("#262626"));
//        UIManager.put("ComboBox.foreground", Color.white);
//        UIManager.put("Label.foreground",Color.white);
//        UIManager.put("RadioButton.background", Color.decode("#262626"));
//        UIManager.put("RadioButton.foreground", Color.white);
//        UIManager.put("OptionPane.foreground", Color.white);


//        UIManager.put("Panel.background", Color.white);
        initalizeAdminControls();
        Header h = this;

        Login loginstart = new Login(h);
        ///------------------------------------/AUTOLOGIN
//        loginstart.tryLogin(h, "ivan.gonzales@gmail.com","pogi");



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

                checkLoginStatus();
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

        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeAccount();
            }
        });
        adminControlsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.show(frame, adminControlsButton.getX(), adminControlsButton.getY());
            }
        });
    }

    public static void main(String[] args) {


        Header h = new Header();
        h.frame = new JFrame();


        h.checkLoginStatus();

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

        ///----------AUTO MOVIE

//        h.movieCode="m1";
//        h.seeMovieDetails(h);

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

    public void seeAccount(){
        this.contentPanel.remove(0);
        this.contentPanel.add(new myAccount(this).panel);
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }

    public void checkLoginStatus(){
        if(customerEmail.equals("")){
            signOutButton.setVisible(false);
            myTicketsButton.setVisible(false);
            signInButton.setVisible(true);
            registerButton.setVisible(true);
            myPurchasesButton.setVisible(false);
            viewProfileButton.setVisible(false);
        } else{
            signOutButton.setVisible(true);
            myTicketsButton.setVisible(true);
            signInButton.setVisible(false);
            registerButton.setVisible(false);
            myPurchasesButton.setVisible(true);
            viewProfileButton.setVisible(true);
        }
        if(isAdmin){
            adminControlsButton.setVisible(true);

        }else{
            adminControlsButton.setVisible(false);

        }
    }

    public void confirmPurchaseScreen(Header h){
        h.contentPanel.remove(0);
        h.contentPanel.add(new ConfirmPurchase(purchasingSeats, selectedShowID,movieCode, h,purchasingSeatsType).panel);
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
    private void initalizeAdminControls(){
        popup = new JPopupMenu();

        JMenuItem menuItem = new JMenuItem("Manage movies", new ImageIcon("src/1x/Asset 5.png"));
        menuItem.setBackground(Color.decode("#383639"));
        menuItem.setForeground(Color.WHITE);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                seeMovieManager();
            }
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Manage cinema halls", new ImageIcon("src/1x/Asset 4.png"));
        menuItem.setBackground(Color.decode("#383639"));
        menuItem.setForeground(Color.WHITE);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                seeHallManager();
            }
        });

        popup.add(menuItem);

        menuItem = new JMenuItem("Manage show times", new ImageIcon("src/1x/Asset 3.png"));
        menuItem.setBackground(Color.decode("#383639"));
        menuItem.setForeground(Color.WHITE);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                seeShowManager();
            }
        });

        popup.add(menuItem);

    }
}

