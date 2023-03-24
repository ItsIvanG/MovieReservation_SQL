import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class editShowDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel showIDlabel;
    private JComboBox movieCombo;
    private JSpinner hourSpinner;
    private JSpinner minuteSpin;
    private JComboBox amCombo;
    private List<String> movieList = new ArrayList<>();
    private String defaultMovieCode;
    private String setTime;
    private int thisShowID;
    public editShowDialog(ShowManage sm, int showID, boolean isNew) {
        thisShowID=showID;
        SpinnerModel hourModel = new SpinnerNumberModel(1, 1, 12, 1);
        SpinnerModel minuteModel = new SpinnerNumberModel(0, 0, 60, 1);
        showIDlabel.setText("Edit Show ID: "+showID);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(300,200);
        setLocationRelativeTo(null);
        hourSpinner.setModel(hourModel);
        minuteSpin.setModel(minuteModel);
        try{
            Connection conn = DriverManager.getConnection(connectionClass.connectionString);
            PreparedStatement pst = conn.prepareStatement("select * from show_time where show_id=?");
            pst.setInt(1,showID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                hourSpinner.setValue(Integer.parseInt(dateTimeConvert.parseHour(rs.getTime("show_time"))));
                minuteModel.setValue(Integer.parseInt(dateTimeConvert.parseMinute(rs.getTime("show_time"))));
                if(!dateTimeConvert.parseAM(rs.getTime("show_time")).equalsIgnoreCase("am")){
                    amCombo.setSelectedIndex(1);
                }
//                timeField.setText(dateTimeConvert.toShortTime(rs.getTime("show_time")));

                defaultMovieCode = rs.getString("movie_id");
            }

            /////////get all movies
            pst = conn.prepareStatement("select * from movie");
            rs = pst.executeQuery();

            while (rs.next()){
                movieList.add(rs.getString("movie_id"));
                movieCombo.addItem("["+rs.getString("movie_id")+"] "+rs.getString("movie_name"));
                movieCombo.setSelectedIndex(movieList.indexOf(defaultMovieCode));
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(amCombo.getSelectedIndex()==0){
                    setTime= hourSpinner.getValue() +":"+ minuteSpin.getValue()+":00";
                } else{
                    setTime= (Integer.parseInt(hourSpinner.getValue().toString())+12) +":"+ minuteSpin.getValue()+":00";
                }
                System.out.println(setTime);
                try{
                    Connection conn = DriverManager.getConnection(connectionClass.connectionString);
                    PreparedStatement pst = conn.prepareStatement("update show_time set show_time=?, movie_id=? where show_id=?");
                    pst.setTime(1, Time.valueOf(setTime));
                    pst.setString(2,movieList.get(movieCombo.getSelectedIndex()));
                    pst.setInt(3,showID);
                    pst.execute();
                    System.out.println("EDITED SHOWID "+showID+" TO TIME "+setTime+" MOVIE "+movieList.get(movieCombo.getSelectedIndex()));
                } catch (Exception x){
                    System.out.println(x.getMessage());
                }
                sm.seeShows();
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel(isNew);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(isNew);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel(isNew);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel(boolean isNew) {
        if(isNew){
            try{
                Connection conn = DriverManager.getConnection(connectionClass.connectionString);
                PreparedStatement pst = conn.prepareStatement("delete from show_time where show_id=?");
                pst.setInt(1,thisShowID);
                pst.execute();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        // add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        editShowDialog dialog = new editShowDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
