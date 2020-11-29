package cdms;

import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Date;

/**
 *
 * @author lenovo
 */
// Empty Text Field Exception
class EmptyTextField extends Exception {

    public EmptyTextField() {
        super();
    }
}
// Invalid Age Exception
class InvalidAgeException extends Exception {
    
    public InvalidAgeException() {
        super();
    }
}

public class CDMS extends javax.swing.JFrame {

    Connection cn;
    Statement st;
    String sql;
    ResultSet rs;
    String userName;
    String passwd;
    String passwdHint;
    int admId;
    int policeId;
    int criminalId;
    int caseId;
    int jailId;
    int hospitalId;
    boolean insert = false;
    boolean update = false;
    boolean hospIdEntered;
    boolean jailIdEntered;
    boolean caseIdEntered;
    boolean crimIdEntered;
    boolean pidEntered;
    boolean buttonVisible56;
    String adminSelect = "select * from admin order by admin_id";
    String policeSelect = "select * from police order by p_id";
    String criminalSelect = "select * from criminal order by cr_id";
    String crimeSelect = "select * from crime order by case_id";
    String jailSelect = "select * from jail order by jail_id";
    String hospRecSelect = "select * from hosprecord order by hosp_id";
    String rowInsert = "New data inserted successfully.";
    String rowUpdate = "Row(s) updated successfully.";
    String rowDelete = "Current data deleted sucessfully.";

    // Connect to Oracle Database
    private void connect2Db(String s) {
        try {
            sql = s;
            Class.forName("oracle.jdbc.OracleDriver");
            cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/SID", "username", "password");
            st = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(sql);
            rs.first();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex + " Please Contact Developer. ");
        }
    }

    // Password Hint in case if admin enters wrong password but correct username.
    private void passwordHint(String s) {
        try {
            sql = "select passwd_hint from admin where username = '" + s + "'";
            rs = st.executeQuery(sql);
            rs.first();
            String hint = rs.getString(1);
            jLabel61.setText(hint);
        } catch (SQLException ex) {
            jLabel60.setVisible(false);
        }
    }

    // CLear the Text Fields of Add Admin.
    private void clearAATF() {
        jTextField30.setText(null);
        jTextField29.setText(null);
        jPasswordField2.setText(null);
        jPasswordField5.setText(null);
        jTextField33.setText(null);
    }

    // Clear the Text Fields of Change Password.
    private void clearCPTF() {
        jPasswordField4.setText(null);
        jPasswordField3.setText(null);
        jPasswordField6.setText(null);
        jTextField31.setText(null);
    }

    // Execute Update
    private void exeUpdate(String s, String success, String sel) {
        try {
            sql = s;
            st.executeUpdate(s);
            JOptionPane.showMessageDialog(null, "" + success);
            cn.commit();
            cn.close();
            connect2Db(sel);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "The data you are trying to insert/modify is not unique.");
        } catch (SQLDataException ex) {
            JOptionPane.showMessageDialog(null, "Please Insert the data in DD-Mon-YYYY only.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error in Database. Please contact Developer.");
        }
    }

    // Fetch Police Data
    public void popPData() throws Exception {
        jTextField4.setText(rs.getString(2));
        jTextField5.setText(rs.getString(3));
        jTextField6.setText(rs.getString(4));
        jTextField7.setText(rs.getString(5));
        jTextArea1.setText(rs.getString(6));
    }

    // Clear Police Text Fields
    public void clearPoliceTF() {
        jTextField3.setText(null);
        jTextField4.setText(null);
        jTextField5.setText(null);
        jTextField6.setText(null);
        jTextField7.setText(null);
        jTextArea1.setText(null);
    }

    // View Police
    public void Policeview(String s) {
        try {
            do {
                sql = s;
                rs = st.executeQuery(sql);
                rs.first();
                if (rs.getInt(1) == policeId) {
                    popPData();
                    break;
                } else {
                    rs.next();
                }
            } while (true);
        } catch (Exception ex) {
            jTextField3.setText(null);
            jTextField3.setEditable(true);
            JOptionPane.showMessageDialog(null, "Police ID : " + policeId + " Not found.");
        }
    }

    // Check if Police ID entered.
    public void isPidEntered() {
        pidEntered = true;
        try {
            policeId = Integer.parseInt(jTextField3.getText());
        } catch (NumberFormatException ex) {
            jTextField3.setText(null);
            pidEntered = false;
            JOptionPane.showMessageDialog(null, "Please Enter Police ID");
        }
    }

    // Fetch Criminal Data
    private void popCrimlData() throws Exception {
        jTextField15.setText(rs.getString(2));
        jTextField19.setText(rs.getString(3));
        jTextField16.setText(rs.getString(4));
        jTextField17.setText(rs.getString(5));
        jTextField18.setText(rs.getString(6));
        jTextArea3.setText(rs.getString(7));
    }

    // Clear Criminal Text Fields
    private void clearCrimlTF() {
        jTextField14.setText(null);
        jTextField15.setText(null);
        jTextField19.setText(null);
        jTextField16.setText(null);
        jTextField17.setText(null);
        jTextField18.setText(null);
        jTextArea3.setText(null);
    }

    // View Criminal
    private void criminalView(String s) {
        try {
            do {
                sql = s;
                rs = st.executeQuery(sql);
                rs.first();
                if (rs.getInt(1) == criminalId) {
                    popCrimlData();
                    break;
                } else {
                    rs.next();
                }
            } while (true);
        } catch (Exception ex) {
            jTextField14.setText(null);
            jTextField14.setEditable(true);
            JOptionPane.showMessageDialog(null, "Criminal ID : " + criminalId + " Not found.");
        }
    }

    // Check if Criminal ID entered.
    private void isCrimIdEntered() {
        crimIdEntered = true;
        try {
            criminalId = Integer.parseInt(jTextField14.getText());
        } catch (Exception ex) {
            jTextField14.setText(null);
            crimIdEntered = false;
            JOptionPane.showMessageDialog(null, "Please Enter Criminal ID");
        }
    }

    // Fetch Crime Data
    private void popCrimeData() throws Exception {
        jTextField20.setText(rs.getString(2));
        jTextField23.setText(rs.getString(3));
        jTextField21.setText(rs.getString(4));
        String s = rs.getString(5);
        Date d = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.S").parse(s);
        String sd = new SimpleDateFormat("dd-MMM-yyyy").format(d);
        jTextField22.setText(sd);
    }

    // Clear Crime Text Fields
    private void clearCrimeTF() {
        jTextField12.setText(null);
        jTextField20.setText(null);
        jTextField23.setText(null);
        jTextField21.setText(null);
        jTextField22.setText(null);
    }

    // View Crime data.
    private void crimeView(String s) {
        try {
            do {
                sql = s;
                rs = st.executeQuery(sql);
                rs.first();
                if (rs.getInt(1) == caseId) {
                    popCrimeData();
                    break;
                } else {
                    rs.next();
                }
            } while (true);
        } catch (Exception ex) {
            jTextField12.setText(null);
            jTextField12.setEditable(true);
            JOptionPane.showMessageDialog(null, "Case ID : " + caseId + " Not found.");
        }
    }

    // Check if case ID entered.
    private void isCaseIdEntered() {
        caseIdEntered = true;
        try {
            caseId = Integer.parseInt(jTextField12.getText());
        } catch (Exception ex) {
            jTextField12.setText(null);
            caseIdEntered = false;
            JOptionPane.showMessageDialog(null, "Please Enter Crime ID");
        }
    }

    // Fetch Jail Data
    private void popJailData() throws Exception {
        jTextField9.setText(rs.getString(2));
        jTextField13.setText(rs.getString(3));
        jTextField10.setText(rs.getString(4));
    }

    //  Clear Jail Text Fields
    private void clearJailTF() {
        jTextField8.setText(null);
        jTextField9.setText(null);
        jTextField13.setText(null);
        jTextField10.setText(null);
    }

    // view jail data.
    private void jailView(String s) {
        try {
            do {
                sql = s;
                rs = st.executeQuery(sql);
                rs.first();
                if (rs.getInt(1) == jailId) {
                    popJailData();
                    break;
                } else {
                    rs.next();
                }
            } while (true);
        } catch (Exception ex) {
            jTextField8.setText(null);
            jTextField8.setEditable(true);
            JOptionPane.showMessageDialog(null, "Jail ID : " + jailId + " Not found.");
        }
    }

    // Check if jail ID entered.
    private void isJailIdEntered() {
        jailIdEntered = true;
        try {
            jailId = Integer.parseInt(jTextField8.getText());
        } catch (Exception ex) {
            jTextField8.setText(null);
            jailIdEntered = false;
            JOptionPane.showMessageDialog(null, "Please Enter Jail ID");
        }
    }

    // Fetch Hospital Record Data.
    private void popHospRecData() throws Exception {
        jTextField24.setText(rs.getString(2));
        jTextField26.setText(rs.getString(3));
        jTextField25.setText(rs.getString(4));
        jTextField27.setText(rs.getString(5));
        jTextField28.setText(rs.getString(6));
    }

    // Clear Hospital Record Text Fields.
    private void clearHospRecTF() {
        jTextField11.setText(null);
        jTextField24.setText(null);
        jTextField26.setText(null);
        jTextField25.setText(null);
        jTextField27.setText(null);
        jTextField28.setText(null);
    }

    // View Hospital record data.
    private void hospitalRecordView(String s) {
        try {
            do {
                sql = s;
                rs = st.executeQuery(sql);
                rs.first();
                if (rs.getInt(1) == hospitalId) {
                    popHospRecData();
                    break;
                } else {
                    rs.next();
                }
            } while (true);
        } catch (Exception ex) {
            jTextField11.setText(null);
            jTextField11.setEditable(true);
            JOptionPane.showMessageDialog(null, "Hospital ID : " + hospitalId + " Not found.");
        }
    }

    // Check if Hospital Id Entered.
    private void isHospIdEntered() {
        hospIdEntered = true;
        try {
            hospitalId = Integer.parseInt(jTextField11.getText());
        } catch (Exception ex) {
            jTextField11.setText(null);
            hospIdEntered = false;
            JOptionPane.showMessageDialog(null, "Please Enter Hospital ID");
        }
    }

    /**
     * Creates new form CDMS
     */
    public CDMS() {
        initComponents();
        // Connect to Database on Application Startup.
        connect2Db(adminSelect);
        connect2Db(policeSelect);
        connect2Db(criminalSelect);
        connect2Db(crimeSelect);
        connect2Db(jailSelect);
        connect2Db(hospRecSelect);
        jLabel60.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField32 = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        BorderLayout = new javax.swing.JPanel();
        CD = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        CardLayout = new javax.swing.JPanel();
        AdminLogin = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        AdminPage = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        ManageAdmin = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jPasswordField3 = new javax.swing.JPasswordField();
        jLabel47 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jPasswordField4 = new javax.swing.JPasswordField();
        jPasswordField5 = new javax.swing.JPasswordField();
        jLabel48 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jPasswordField6 = new javax.swing.JPasswordField();
        jLabel59 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        SearchRecords = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        Police = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        Criminal = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel30 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        Crime = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jButton40 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        Jail = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton57 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        jButton62 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        HospitalRecord = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jButton75 = new javax.swing.JButton();
        jButton76 = new javax.swing.JButton();
        jButton77 = new javax.swing.JButton();

        jTextField32.setBackground(new java.awt.Color(0, 0, 0));
        jTextField32.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField32.setForeground(new java.awt.Color(255, 255, 255));

        jLabel57.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Password Hint");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Criminal Database Management System");
        setBackground(new java.awt.Color(30, 30, 30));
        setResizable(false);

        BorderLayout.setPreferredSize(new java.awt.Dimension(960, 720));
        BorderLayout.setLayout(new java.awt.BorderLayout());

        CD.setBackground(new java.awt.Color(51, 51, 51));
        CD.setPreferredSize(new java.awt.Dimension(960, 100));

        jLabel1.setFont(new java.awt.Font("Eras Demi ITC", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CRIMINAL DATABASE");

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("HOME");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Eras Demi ITC", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout CDLayout = new javax.swing.GroupLayout(CD);
        CD.setLayout(CDLayout);
        CDLayout.setHorizontalGroup(
            CDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CDLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 435, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        CDLayout.setVerticalGroup(
            CDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CDLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(CDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        BorderLayout.add(CD, java.awt.BorderLayout.PAGE_START);

        CardLayout.setBackground(new java.awt.Color(51, 51, 51));
        CardLayout.setPreferredSize(new java.awt.Dimension(960, 620));
        CardLayout.setLayout(new java.awt.CardLayout());

        AdminLogin.setBackground(new java.awt.Color(22, 22, 22));
        AdminLogin.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel3.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password");

        jLabel4.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Admin Login");

        jLabel5.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Username");

        jTextField2.setBackground(new java.awt.Color(0, 0, 0));
        jTextField2.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("LOGIN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPasswordField1.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField1.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField1.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setPreferredSize(new java.awt.Dimension(175, 30));

        jLabel60.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Password Hint :");

        jLabel61.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setMinimumSize(new java.awt.Dimension(75, 30));

        javax.swing.GroupLayout AdminLoginLayout = new javax.swing.GroupLayout(AdminLogin);
        AdminLogin.setLayout(AdminLoginLayout);
        AdminLoginLayout.setHorizontalGroup(
            AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminLoginLayout.createSequentialGroup()
                .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AdminLoginLayout.createSequentialGroup()
                        .addGap(361, 361, 361)
                        .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(AdminLoginLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(AdminLoginLayout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(AdminLoginLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AdminLoginLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel60)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(362, 362, 362))
        );
        AdminLoginLayout.setVerticalGroup(
            AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminLoginLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addGroup(AdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(207, 207, 207))
        );

        CardLayout.add(AdminLogin, "card2");

        AdminPage.setBackground(new java.awt.Color(30, 30, 30));
        AdminPage.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel6.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Admin Dashboard");

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Search Records");
        jButton3.setBorder(null);
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Manage Admin");
        jButton4.setBorder(null);
        jButton4.setBorderPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AdminPageLayout = new javax.swing.GroupLayout(AdminPage);
        AdminPage.setLayout(AdminPageLayout);
        AdminPageLayout.setHorizontalGroup(
            AdminPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminPageLayout.createSequentialGroup()
                .addGroup(AdminPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AdminPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(AdminPageLayout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(jLabel6))
                        .addGroup(AdminPageLayout.createSequentialGroup()
                            .addGap(400, 400, 400)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(446, Short.MAX_VALUE))
        );
        AdminPageLayout.setVerticalGroup(
            AdminPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdminPageLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(176, 176, 176)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        CardLayout.add(AdminPage, "card3");

        ManageAdmin.setBackground(new java.awt.Color(30, 30, 30));
        ManageAdmin.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel42.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Manage Admin");

        jLabel43.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Username");

        jTextField29.setBackground(new java.awt.Color(0, 0, 0));
        jTextField29.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField29.setForeground(new java.awt.Color(255, 255, 255));

        jButton10.setBackground(new java.awt.Color(0, 0, 0));
        jButton10.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("ADD");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPasswordField2.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField2.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField2.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField2.setPreferredSize(new java.awt.Dimension(175, 30));

        jLabel44.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Password");

        jLabel45.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("ADMIN ID");

        jTextField30.setBackground(new java.awt.Color(0, 0, 0));
        jTextField30.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField30.setForeground(new java.awt.Color(255, 255, 255));

        jLabel46.setFont(new java.awt.Font("Eras Demi ITC", 1, 24)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Add Admin");

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("SUBMIT");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jPasswordField3.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField3.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField3.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField3.setPreferredSize(new java.awt.Dimension(175, 30));

        jLabel47.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("New Password");

        jLabel49.setFont(new java.awt.Font("Eras Demi ITC", 1, 24)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Change Password");

        jLabel50.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Current Password");

        jLabel52.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("1. Maximum Admin limit is 3. ");

        jLabel53.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Note : ");

        jLabel54.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("2. When adding new Admin, set Admin ID = 2 (3 for 3rd Admin).");

        jLabel55.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("3. Please don't forget the password.");

        jPasswordField4.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField4.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField4.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField4.setMinimumSize(new java.awt.Dimension(14, 30));
        jPasswordField4.setPreferredSize(new java.awt.Dimension(175, 30));
        jPasswordField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField4ActionPerformed(evt);
            }
        });

        jPasswordField5.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField5.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField5.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField5.setPreferredSize(new java.awt.Dimension(175, 30));

        jLabel48.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Confirm Password");

        jTextField31.setBackground(new java.awt.Color(0, 0, 0));
        jTextField31.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField31.setForeground(new java.awt.Color(255, 255, 255));

        jLabel56.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Password Hint");

        jTextField33.setBackground(new java.awt.Color(0, 0, 0));
        jTextField33.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField33.setForeground(new java.awt.Color(255, 255, 255));

        jLabel58.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Password Hint");

        jPasswordField6.setBackground(new java.awt.Color(0, 0, 0));
        jPasswordField6.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jPasswordField6.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField6.setPreferredSize(new java.awt.Dimension(175, 30));

        jLabel59.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Confirm Password");

        jLabel62.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("4. Please Restart the application after adding new admin or changing the password.");

        javax.swing.GroupLayout ManageAdminLayout = new javax.swing.GroupLayout(ManageAdmin);
        ManageAdmin.setLayout(ManageAdminLayout);
        ManageAdminLayout.setHorizontalGroup(
            ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ManageAdminLayout.createSequentialGroup()
                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ManageAdminLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel42))
                    .addGroup(ManageAdminLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ManageAdminLayout.createSequentialGroup()
                                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel46)
                                        .addGroup(ManageAdminLayout.createSequentialGroup()
                                            .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel43)
                                                .addComponent(jLabel44)
                                                .addComponent(jLabel45)
                                                .addComponent(jLabel48)
                                                .addComponent(jLabel58))
                                            .addGap(41, 41, 41)
                                            .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jPasswordField5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField29)
                                                    .addComponent(jPasswordField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField30))
                                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(ManageAdminLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ManageAdminLayout.createSequentialGroup()
                                                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel47)
                                                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel56)
                                                    .addComponent(jLabel59))
                                                .addGap(41, 41, 41)
                                                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jPasswordField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jPasswordField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField31, javax.swing.GroupLayout.Alignment.TRAILING))))))
                                    .addGroup(ManageAdminLayout.createSequentialGroup()
                                        .addGap(320, 320, 320)
                                        .addComponent(jPasswordField6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(27, 27, 27))
        );
        ManageAdminLayout.setVerticalGroup(
            ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ManageAdminLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ManageAdminLayout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel45)
                            .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43)
                            .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48)
                            .addComponent(jPasswordField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ManageAdminLayout.createSequentialGroup()
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel50)
                            .addComponent(jPasswordField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jPasswordField6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel59))
                        .addGap(18, 18, 18)
                        .addGroup(ManageAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel62)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        CardLayout.add(ManageAdmin, "card10");

        SearchRecords.setBackground(new java.awt.Color(30, 30, 30));
        SearchRecords.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel7.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Search Records");

        jButton5.setBackground(new java.awt.Color(0, 0, 0));
        jButton5.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Health Records");
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 0, 0));
        jButton6.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Police");
        jButton6.setBorder(null);
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 0, 0));
        jButton7.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Criminal");
        jButton7.setBorder(null);
        jButton7.setBorderPainted(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 0, 0));
        jButton8.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Crime");
        jButton8.setBorder(null);
        jButton8.setBorderPainted(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 0, 0));
        jButton9.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Jail");
        jButton9.setBorder(null);
        jButton9.setBorderPainted(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SearchRecordsLayout = new javax.swing.GroupLayout(SearchRecords);
        SearchRecords.setLayout(SearchRecordsLayout);
        SearchRecordsLayout.setHorizontalGroup(
            SearchRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchRecordsLayout.createSequentialGroup()
                .addGroup(SearchRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SearchRecordsLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel7))
                    .addGroup(SearchRecordsLayout.createSequentialGroup()
                        .addGap(392, 392, 392)
                        .addGroup(SearchRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SearchRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(439, Short.MAX_VALUE))
        );
        SearchRecordsLayout.setVerticalGroup(
            SearchRecordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchRecordsLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(186, Short.MAX_VALUE))
        );

        CardLayout.add(SearchRecords, "card4");

        Police.setBackground(new java.awt.Color(30, 30, 30));
        Police.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel8.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Police");

        jLabel9.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Police ID");

        jLabel10.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Last name");

        jLabel11.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("First Name");

        jLabel12.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Police Station");
        jLabel12.setPreferredSize(new java.awt.Dimension(150, 35));

        jLabel13.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Phone Number");

        jLabel14.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Address");

        jTextField3.setBackground(new java.awt.Color(0, 0, 0));
        jTextField3.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(255, 255, 255));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setBackground(new java.awt.Color(0, 0, 0));
        jTextField4.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jTextField5.setBackground(new java.awt.Color(0, 0, 0));
        jTextField5.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(255, 255, 255));

        jTextField6.setBackground(new java.awt.Color(0, 0, 0));
        jTextField6.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(255, 255, 255));

        jTextField7.setBackground(new java.awt.Color(0, 0, 0));
        jTextField7.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(255, 255, 255));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jTextArea1.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton34.setBackground(new java.awt.Color(0, 0, 0));
        jButton34.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("Insert");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(0, 0, 0));
        jButton35.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.setText("Search");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(0, 0, 0));
        jButton36.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.setText("Delete");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setBackground(new java.awt.Color(0, 0, 0));
        jButton37.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.setText("Modify");
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton38.setBackground(new java.awt.Color(0, 0, 0));
        jButton38.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.setText("View");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setBackground(new java.awt.Color(0, 0, 0));
        jButton39.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton39.setForeground(new java.awt.Color(255, 255, 255));
        jButton39.setText("Back");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton42.setBackground(new java.awt.Color(0, 0, 0));
        jButton42.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton42.setForeground(new java.awt.Color(255, 255, 255));
        jButton42.setPreferredSize(new java.awt.Dimension(30, 35));
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PoliceLayout = new javax.swing.GroupLayout(Police);
        Police.setLayout(PoliceLayout);
        PoliceLayout.setHorizontalGroup(
            PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PoliceLayout.createSequentialGroup()
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton35)
                        .addGap(18, 18, 18)
                        .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PoliceLayout.createSequentialGroup()
                .addGap(0, 267, Short.MAX_VALUE)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PoliceLayout.createSequentialGroup()
                        .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField7)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(221, 221, 221))
        );
        PoliceLayout.setVerticalGroup(
            PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PoliceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PoliceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton34)
                    .addComponent(jButton35)
                    .addComponent(jButton37)
                    .addComponent(jButton38)
                    .addComponent(jButton36)
                    .addComponent(jButton39))
                .addGap(82, 82, 82))
        );

        CardLayout.add(Police, "card5");

        Criminal.setBackground(new java.awt.Color(30, 30, 30));
        Criminal.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel23.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Criminal");

        jLabel24.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Criminal ID");

        jLabel25.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Last Name");

        jLabel26.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("First Name");

        jLabel27.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Phone Number");
        jLabel27.setPreferredSize(new java.awt.Dimension(150, 35));

        jLabel28.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Gender");

        jLabel29.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Address");

        jTextField14.setBackground(new java.awt.Color(0, 0, 0));
        jTextField14.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField14.setForeground(new java.awt.Color(255, 255, 255));

        jTextField15.setBackground(new java.awt.Color(0, 0, 0));
        jTextField15.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField15.setForeground(new java.awt.Color(255, 255, 255));

        jTextField16.setBackground(new java.awt.Color(0, 0, 0));
        jTextField16.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField16.setForeground(new java.awt.Color(255, 255, 255));

        jTextField17.setBackground(new java.awt.Color(0, 0, 0));
        jTextField17.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(255, 255, 255));

        jTextField18.setBackground(new java.awt.Color(0, 0, 0));
        jTextField18.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField18.setForeground(new java.awt.Color(255, 255, 255));
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });

        jTextArea3.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextArea3.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea3.setRows(5);
        jTextArea3.setPreferredSize(new java.awt.Dimension(320, 100));
        jScrollPane3.setViewportView(jTextArea3);

        jLabel30.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Age");

        jTextField19.setBackground(new java.awt.Color(0, 0, 0));
        jTextField19.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField19.setForeground(new java.awt.Color(255, 255, 255));

        jButton28.setBackground(new java.awt.Color(0, 0, 0));
        jButton28.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton28.setForeground(new java.awt.Color(255, 255, 255));
        jButton28.setText("Delete");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(0, 0, 0));
        jButton29.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton29.setForeground(new java.awt.Color(255, 255, 255));
        jButton29.setText("Modify");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setBackground(new java.awt.Color(0, 0, 0));
        jButton30.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.setText("View");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setBackground(new java.awt.Color(0, 0, 0));
        jButton31.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton31.setForeground(new java.awt.Color(255, 255, 255));
        jButton31.setText("Back");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton41.setBackground(new java.awt.Color(0, 0, 0));
        jButton41.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton41.setForeground(new java.awt.Color(255, 255, 255));
        jButton41.setPreferredSize(new java.awt.Dimension(73, 36));
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton32.setBackground(new java.awt.Color(0, 0, 0));
        jButton32.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton32.setForeground(new java.awt.Color(255, 255, 255));
        jButton32.setText("Insert");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setBackground(new java.awt.Color(0, 0, 0));
        jButton33.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("Search");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CriminalLayout = new javax.swing.GroupLayout(Criminal);
        Criminal.setLayout(CriminalLayout);
        CriminalLayout.setHorizontalGroup(
            CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CriminalLayout.createSequentialGroup()
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CriminalLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CriminalLayout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField18)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(CriminalLayout.createSequentialGroup()
                                .addGap(193, 193, 193)
                                .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CriminalLayout.createSequentialGroup()
                .addGap(0, 185, Short.MAX_VALUE)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton33)
                .addGap(18, 18, 18)
                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
        CriminalLayout.setVerticalGroup(
            CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CriminalLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField19)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(CriminalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton32)
                    .addComponent(jButton33)
                    .addComponent(jButton29)
                    .addComponent(jButton30)
                    .addComponent(jButton28)
                    .addComponent(jButton31))
                .addGap(51, 51, 51))
        );

        CardLayout.add(Criminal, "card5");

        Crime.setBackground(new java.awt.Color(30, 30, 30));
        Crime.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel19.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Crime");

        jLabel21.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Case ID");

        jLabel31.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Crime ID");

        jLabel32.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Crime Type");

        jLabel33.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("(DD-Mon-YYYY)");

        jTextField12.setBackground(new java.awt.Color(0, 0, 0));
        jTextField12.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField12.setForeground(new java.awt.Color(255, 255, 255));
        jTextField12.setPreferredSize(new java.awt.Dimension(350, 30));

        jTextField20.setBackground(new java.awt.Color(0, 0, 0));
        jTextField20.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField20.setForeground(new java.awt.Color(255, 255, 255));
        jTextField20.setPreferredSize(new java.awt.Dimension(350, 30));

        jTextField21.setBackground(new java.awt.Color(0, 0, 0));
        jTextField21.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField21.setForeground(new java.awt.Color(255, 255, 255));
        jTextField21.setPreferredSize(new java.awt.Dimension(350, 30));

        jTextField22.setBackground(new java.awt.Color(0, 0, 0));
        jTextField22.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField22.setForeground(new java.awt.Color(255, 255, 255));
        jTextField22.setPreferredSize(new java.awt.Dimension(350, 30));

        jLabel34.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Police ID");

        jTextField23.setBackground(new java.awt.Color(0, 0, 0));
        jTextField23.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField23.setForeground(new java.awt.Color(255, 255, 255));
        jTextField23.setPreferredSize(new java.awt.Dimension(350, 30));

        jButton40.setBackground(new java.awt.Color(0, 0, 0));
        jButton40.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setText("Insert");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton43.setBackground(new java.awt.Color(0, 0, 0));
        jButton43.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.setText("Search");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jButton44.setBackground(new java.awt.Color(0, 0, 0));
        jButton44.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton44.setForeground(new java.awt.Color(255, 255, 255));
        jButton44.setText("Delete");
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setBackground(new java.awt.Color(0, 0, 0));
        jButton45.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton45.setForeground(new java.awt.Color(255, 255, 255));
        jButton45.setText("Modify");
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        jButton46.setBackground(new java.awt.Color(0, 0, 0));
        jButton46.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton46.setForeground(new java.awt.Color(255, 255, 255));
        jButton46.setText("View");
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton47.setBackground(new java.awt.Color(0, 0, 0));
        jButton47.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton47.setForeground(new java.awt.Color(255, 255, 255));
        jButton47.setText("Back");
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jButton56.setBackground(new java.awt.Color(0, 0, 0));
        jButton56.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton56.setForeground(new java.awt.Color(255, 255, 255));
        jButton56.setPreferredSize(new java.awt.Dimension(350, 35));
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Crime date");

        javax.swing.GroupLayout CrimeLayout = new javax.swing.GroupLayout(Crime);
        Crime.setLayout(CrimeLayout);
        CrimeLayout.setHorizontalGroup(
            CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CrimeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(208, 208, 208))
            .addGroup(CrimeLayout.createSequentialGroup()
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton43)
                        .addGap(18, 18, 18)
                        .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        CrimeLayout.setVerticalGroup(
            CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CrimeLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(CrimeLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(CrimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton40)
                    .addComponent(jButton43)
                    .addComponent(jButton45)
                    .addComponent(jButton46)
                    .addComponent(jButton44)
                    .addComponent(jButton47))
                .addGap(191, 191, 191))
        );

        CardLayout.add(Crime, "card5");

        Jail.setBackground(new java.awt.Color(30, 30, 30));
        Jail.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel15.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Jail");

        jLabel16.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Jail ID");
        jLabel16.setToolTipText("");

        jLabel17.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Jail Name");

        jLabel18.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Criminal ID");

        jTextField8.setBackground(new java.awt.Color(0, 0, 0));
        jTextField8.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField8.setForeground(new java.awt.Color(255, 255, 255));

        jTextField9.setBackground(new java.awt.Color(0, 0, 0));
        jTextField9.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(255, 255, 255));

        jTextField10.setBackground(new java.awt.Color(0, 0, 0));
        jTextField10.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField10.setForeground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Sentence Time");

        jTextField13.setBackground(new java.awt.Color(0, 0, 0));
        jTextField13.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField13.setForeground(new java.awt.Color(255, 255, 255));

        jButton57.setBackground(new java.awt.Color(0, 0, 0));
        jButton57.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton57.setForeground(new java.awt.Color(255, 255, 255));
        jButton57.setText("Insert");
        jButton57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton57ActionPerformed(evt);
            }
        });

        jButton58.setBackground(new java.awt.Color(0, 0, 0));
        jButton58.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton58.setForeground(new java.awt.Color(255, 255, 255));
        jButton58.setText("Search");
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });

        jButton59.setBackground(new java.awt.Color(0, 0, 0));
        jButton59.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton59.setForeground(new java.awt.Color(255, 255, 255));
        jButton59.setText("Delete");
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });

        jButton60.setBackground(new java.awt.Color(0, 0, 0));
        jButton60.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton60.setForeground(new java.awt.Color(255, 255, 255));
        jButton60.setText("Modify");
        jButton60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton60ActionPerformed(evt);
            }
        });

        jButton61.setBackground(new java.awt.Color(0, 0, 0));
        jButton61.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton61.setForeground(new java.awt.Color(255, 255, 255));
        jButton61.setText("View");
        jButton61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton61ActionPerformed(evt);
            }
        });

        jButton62.setBackground(new java.awt.Color(0, 0, 0));
        jButton62.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton62.setForeground(new java.awt.Color(255, 255, 255));
        jButton62.setText("Back");
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jButton63.setBackground(new java.awt.Color(0, 0, 0));
        jButton63.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton63.setForeground(new java.awt.Color(255, 255, 255));
        jButton63.setPreferredSize(new java.awt.Dimension(350, 35));
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JailLayout = new javax.swing.GroupLayout(Jail);
        Jail.setLayout(JailLayout);
        JailLayout.setHorizontalGroup(
            JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JailLayout.createSequentialGroup()
                .addGap(0, 258, Short.MAX_VALUE)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JailLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JailLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JailLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JailLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(205, 205, 205))
            .addGroup(JailLayout.createSequentialGroup()
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JailLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JailLayout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton58)
                        .addGap(18, 18, 18)
                        .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JailLayout.setVerticalGroup(
            JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JailLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10))
                .addGap(18, 18, 18)
                .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(JailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton57)
                    .addComponent(jButton58)
                    .addComponent(jButton60)
                    .addComponent(jButton61)
                    .addComponent(jButton59)
                    .addComponent(jButton62))
                .addGap(252, 252, 252))
        );

        CardLayout.add(Jail, "card5");

        HospitalRecord.setBackground(new java.awt.Color(30, 30, 30));
        HospitalRecord.setPreferredSize(new java.awt.Dimension(960, 620));

        jLabel20.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Hospital Record");

        jLabel35.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Hospital ID");
        jLabel35.setToolTipText("");

        jLabel36.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Height");

        jLabel37.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Criminal ID");

        jTextField11.setBackground(new java.awt.Color(0, 0, 0));
        jTextField11.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField11.setForeground(new java.awt.Color(255, 255, 255));

        jTextField24.setBackground(new java.awt.Color(0, 0, 0));
        jTextField24.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField24.setForeground(new java.awt.Color(255, 255, 255));

        jTextField25.setBackground(new java.awt.Color(0, 0, 0));
        jTextField25.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField25.setForeground(new java.awt.Color(255, 255, 255));

        jLabel38.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Weight");

        jTextField26.setBackground(new java.awt.Color(0, 0, 0));
        jTextField26.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField26.setForeground(new java.awt.Color(255, 255, 255));

        jTextField27.setBackground(new java.awt.Color(0, 0, 0));
        jTextField27.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField27.setForeground(new java.awt.Color(255, 255, 255));

        jTextField28.setBackground(new java.awt.Color(0, 0, 0));
        jTextField28.setFont(new java.awt.Font("Eras Demi ITC", 0, 18)); // NOI18N
        jTextField28.setForeground(new java.awt.Color(255, 255, 255));
        jTextField28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField28ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Blood Group");

        jLabel40.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Identification");

        jLabel51.setBackground(new java.awt.Color(38, 38, 38));
        jLabel51.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));

        jButton64.setBackground(new java.awt.Color(0, 0, 0));
        jButton64.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton64.setForeground(new java.awt.Color(255, 255, 255));
        jButton64.setText("Insert");

        jButton65.setBackground(new java.awt.Color(0, 0, 0));
        jButton65.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton65.setForeground(new java.awt.Color(255, 255, 255));
        jButton65.setText("Search");

        jButton66.setBackground(new java.awt.Color(0, 0, 0));
        jButton66.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton66.setForeground(new java.awt.Color(255, 255, 255));
        jButton66.setText("Delete");

        jButton67.setBackground(new java.awt.Color(0, 0, 0));
        jButton67.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton67.setForeground(new java.awt.Color(255, 255, 255));
        jButton67.setText("Update");

        jButton68.setBackground(new java.awt.Color(0, 0, 0));
        jButton68.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton68.setForeground(new java.awt.Color(255, 255, 255));
        jButton68.setText("View");

        jButton69.setBackground(new java.awt.Color(0, 0, 0));
        jButton69.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton69.setForeground(new java.awt.Color(255, 255, 255));
        jButton69.setText("Back");

        jButton70.setBackground(new java.awt.Color(0, 0, 0));
        jButton70.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton70.setForeground(new java.awt.Color(255, 255, 255));
        jButton70.setText("Save");

        jButton71.setBackground(new java.awt.Color(0, 0, 0));
        jButton71.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton71.setForeground(new java.awt.Color(255, 255, 255));
        jButton71.setText("Insert");
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jButton72.setBackground(new java.awt.Color(0, 0, 0));
        jButton72.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton72.setForeground(new java.awt.Color(255, 255, 255));
        jButton72.setText("Search");
        jButton72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton72ActionPerformed(evt);
            }
        });

        jButton73.setBackground(new java.awt.Color(0, 0, 0));
        jButton73.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton73.setForeground(new java.awt.Color(255, 255, 255));
        jButton73.setText("Delete");
        jButton73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton73ActionPerformed(evt);
            }
        });

        jButton74.setBackground(new java.awt.Color(0, 0, 0));
        jButton74.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton74.setForeground(new java.awt.Color(255, 255, 255));
        jButton74.setText("Modify");
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        jButton75.setBackground(new java.awt.Color(0, 0, 0));
        jButton75.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton75.setForeground(new java.awt.Color(255, 255, 255));
        jButton75.setText("View");
        jButton75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton75ActionPerformed(evt);
            }
        });

        jButton76.setBackground(new java.awt.Color(0, 0, 0));
        jButton76.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton76.setForeground(new java.awt.Color(255, 255, 255));
        jButton76.setText("Back");
        jButton76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton76ActionPerformed(evt);
            }
        });

        jButton77.setBackground(new java.awt.Color(0, 0, 0));
        jButton77.setFont(new java.awt.Font("Eras Demi ITC", 1, 18)); // NOI18N
        jButton77.setForeground(new java.awt.Color(255, 255, 255));
        jButton77.setPreferredSize(new java.awt.Dimension(350, 35));
        jButton77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton77ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout HospitalRecordLayout = new javax.swing.GroupLayout(HospitalRecord);
        HospitalRecord.setLayout(HospitalRecordLayout);
        HospitalRecordLayout.setHorizontalGroup(
            HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HospitalRecordLayout.createSequentialGroup()
                .addGap(0, 255, Short.MAX_VALUE)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(HospitalRecordLayout.createSequentialGroup()
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(208, 208, 208))
            .addGroup(HospitalRecordLayout.createSequentialGroup()
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HospitalRecordLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel20))
                    .addGroup(HospitalRecordLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton72)
                        .addGap(18, 18, 18)
                        .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HospitalRecordLayout.setVerticalGroup(
            HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HospitalRecordLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField28))
                .addGap(18, 18, 18)
                .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(HospitalRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton71)
                    .addComponent(jButton72)
                    .addComponent(jButton74)
                    .addComponent(jButton75)
                    .addComponent(jButton73)
                    .addComponent(jButton76))
                .addGap(161, 161, 161))
        );

        CardLayout.add(HospitalRecord, "card5");

        BorderLayout.add(CardLayout, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BorderLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BorderLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Clear the present tab.
    private void CL() {
        CardLayout.removeAll();
        CardLayout.repaint();
        CardLayout.revalidate();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            int i = 1;
            userName = jTextField2.getText();
            passwd = jPasswordField1.getText();
            String admUserName, admPasswd;

            if (userName.isEmpty() || passwd.isEmpty()) {
                throw new EmptyTextField();
            }

            sql = "select * from admin order by admin_id";
            rs = st.executeQuery(sql);
            rs.first();
            do {
                admId = Integer.parseInt(rs.getString(1));
                admUserName = rs.getString(2);
                admPasswd = rs.getString(3);
                if (admUserName.equals(userName) && admPasswd.equals(passwd)) {
                    CL();
                    CardLayout.add(AdminPage);
                    break;
                } else {
                    rs.next();
                }
            } while (i <= 2);
            jLabel2.setText(userName.toUpperCase());
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the details to login.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Either username or password is incorrect. Please try again.");
            jPasswordField1.setText(null);
            jLabel60.setVisible(true);
            passwordHint(userName);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        CL();
        CardLayout.add(ManageAdmin);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        CL();
        CardLayout.add(SearchRecords);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        CL();
        CardLayout.add(HospitalRecord);
        jButton77.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        CL();
        CardLayout.add(Police);
        jButton42.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        CL();
        CardLayout.add(Criminal);
        jButton41.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        CL();
        CardLayout.add(Crime);
        jButton56.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        CL();
        CardLayout.add(Jail);
        jButton63.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        jButton42.setText("Update");
        isCrimIdEntered();
        if (crimIdEntered) {
            int result = JOptionPane.showConfirmDialog(null, "Delete this ID?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                sql = "Delete from criminal where cr_id = " + criminalId;
                clearCrimlTF();
                exeUpdate(sql, rowDelete, crimeSelect);
            }
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        jButton41.setText("Update");
        jButton41.setVisible(true);
        jTextField14.setEditable(false);
        update = true;
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        jButton41.setVisible(false);
        jTextField14.setEditable(false);
        isCrimIdEntered();

        if (crimIdEntered) {
            sql = "select * from criminal where cr_id = " + criminalId;
            criminalView(sql);
        }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        CL();
        CardLayout.add(AdminPage);
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        try {
            int crId = Integer.parseInt(jTextField14.getText());
            String fName = jTextField15.getText();
            String lName = jTextField19.getText();
            int age = Integer.parseInt(jTextField16.getText());
            String gender = jTextField17.getText();
            String phNo = jTextField18.getText();
            String address = jTextArea3.getText();
            
            if(age < 6 || age > 100) {
                throw new InvalidAgeException();
            }
            
            if (fName.isEmpty() || lName.isEmpty() || gender.isEmpty() || phNo.isEmpty() || address.isEmpty()) {
                throw new EmptyTextField();
            }

            if (insert) {
                sql = "insert into criminal values (" + crId + ",'" + fName + "', '" + lName + "', " + age + ", '" + gender + "', '" + phNo + "', '" + address + "')";
                exeUpdate(sql, rowInsert, criminalSelect);
                insert = false;
            } else if (update) {
                sql = "update criminal set f_name =  '" + fName + "', l_name = '" + lName + "', age = " + age + ", gender = '" + gender + "', phone_no = '" + phNo + "', address = '" + address + "' where cr_id = " + crId;
                exeUpdate(sql, rowUpdate, criminalSelect);
                update = false;
            }

            jButton41.setVisible(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please fill ID value(s) as Numbers only.");
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        } catch(InvalidAgeException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Age.");
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        jButton41.setText("Save");
        jButton41.setVisible(true);
        clearCrimlTF();
        insert = true;
        jTextField14.setEditable(true);
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        jButton41.setVisible(false);
        jTextField14.setEditable(true);
        clearCrimlTF();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        clearPoliceTF();
        insert = true;
        jTextField3.setEditable(true);
        jButton42.setText("Save");
        jButton42.setVisible(true);
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        clearPoliceTF();
        jButton42.setVisible(false);
        insert = false;
        jTextField3.setEditable(true);
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        jButton42.setVisible(false);
        isPidEntered();
        if (pidEntered) {
            int result = JOptionPane.showConfirmDialog(null, "Delete this ID?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                sql = "Delete from police where p_id = " + policeId;
                clearPoliceTF();
                exeUpdate(sql, rowDelete, policeSelect);
            }
        }
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        jTextField3.setEditable(false);
        update = true;
        jButton42.setText("Update");
        jButton42.setVisible(true);
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        jButton42.setVisible(false);
        jTextField3.setEditable(false);
        isPidEntered();

        if (pidEntered) {
            sql = "select * from police where p_id = " + policeId;
            Policeview(sql);
        }
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        CL();
        CardLayout.add(AdminPage);
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        try {
            int pId = Integer.parseInt(jTextField3.getText());
            String fName = jTextField4.getText();
            String lName = jTextField5.getText();
            String phNo = jTextField6.getText();
            String pStation = jTextField7.getText();
            String address = jTextArea1.getText();

            if (fName.isEmpty() || lName.isEmpty() || phNo.isEmpty() || pStation.isEmpty() || address.isEmpty()) {
                throw new EmptyTextField();
            }

            if (insert) {
                sql = "insert into police values (" + pId + ",'" + fName + "', '" + lName + "', '" + phNo + "', '" + pStation + "', '" + address + "')";
                exeUpdate(sql, rowInsert, policeSelect);
                insert = false;
            } else if (update) {
                sql = "update police set f_name =  '" + fName + "', l_name = '" + lName + "', phone_no = '" + phNo + "', p_station = '" + pStation + "', address = '" + address + "' where p_id = " + pId;
                exeUpdate(sql, rowUpdate, policeSelect);
                update = false;
            }

            jButton42.setVisible(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please Enter ID values as Numbers only.");
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        clearCrimeTF();
        jTextField12.setEditable(true);
        jButton56.setText("Save");
        jButton56.setVisible(true);
        insert = true;
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        clearCrimeTF();
        jTextField12.setEditable(true);
        jButton56.setVisible(false);
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        jButton56.setVisible(false);
        isCaseIdEntered();
        if (caseIdEntered) {
            int result = JOptionPane.showConfirmDialog(null, "Delete this ID?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                sql = "Delete from crime where case_id = " + caseId;
                exeUpdate(sql, rowDelete, crimeSelect);
                clearCrimeTF();
            }
        }
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        jButton56.setVisible(true);
        jTextField12.setEditable(false);
        jButton56.setText("Update");
        update = true;
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        jButton56.setVisible(false);
        jTextField12.setEditable(false);
        isCaseIdEntered();
        if (caseIdEntered) {
            sql = "select * from crime where case_id = " + caseId;
            crimeView(sql);
        }
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        CL();
        CardLayout.add(AdminPage);
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        try {
            int cId = Integer.parseInt(jTextField12.getText());
            String crimeType = jTextField20.getText();
            int crId = Integer.parseInt(jTextField23.getText());
            int pId = Integer.parseInt(jTextField21.getText());
            String crimeDate = jTextField22.getText();

            if (crimeType.isEmpty() || crimeDate.isEmpty()) {
                throw new EmptyTextField();
            }

            if (insert) {
                sql = "insert into crime values (" + cId + ",'" + crimeType + "', " + crId + ", " + pId + ", '" + crimeDate + "')";
                exeUpdate(sql, rowInsert, crimeSelect);
                insert = false;
            } else if (update) {
                sql = "update crime set crime_type = '" + crimeType + "', cr_id = " + crId + ", police_id = " + pId + ", crime_date = '" + crimeDate + "' where case_id = " + cId;
                exeUpdate(sql, rowUpdate, crimeSelect);
                update = false;
            }
            jButton56.setVisible(false);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please Enter ID values as Numbers only.");
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        }
    }//GEN-LAST:event_jButton56ActionPerformed

    private void jButton77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton77ActionPerformed
        try {
            int hId = Integer.parseInt(jTextField11.getText());
            int criId = Integer.parseInt(jTextField24.getText());
            String height = jTextField26.getText();
            String weight = jTextField25.getText();
            String bloodGrp = jTextField27.getText();
            String identification = jTextField28.getText();

            if (height.isEmpty() || weight.isEmpty() || bloodGrp.isEmpty() || identification.isEmpty()) {
                throw new EmptyTextField();
            }

            if (insert) {
                sql = "insert into hosprecord values (" + hId + "," + criId + ", '" + height + "', '" + weight + "', '" + bloodGrp + "', '" + identification + "')";
                exeUpdate(sql, rowInsert, hospRecSelect);
                insert = false;
            } else if (update) {
                sql = "update hosprecord set cr_id = " + criId + ", height = '" + height + "', weight = '" + weight + "', bloodgrp = '" + bloodGrp + "', identification = '" + identification + "' where hosp_id = " + hId;
                exeUpdate(sql, rowUpdate, hospRecSelect);
                update = false;
            }

            jButton77.setVisible(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please Enter ID values as Numbers only.");
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        }
    }//GEN-LAST:event_jButton77ActionPerformed

    private void jButton76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton76ActionPerformed
        CL();
        CardLayout.add(AdminPage);
    }//GEN-LAST:event_jButton76ActionPerformed

    private void jButton75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton75ActionPerformed
        jButton77.setVisible(false);
        jTextField11.setEditable(false);
        isHospIdEntered();

        if (hospIdEntered) {
            sql = "select * from hosprecord where hosp_id  = " + hospitalId;
            hospitalRecordView(sql);
        }
    }//GEN-LAST:event_jButton75ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        update = true;
        jTextField11.setEditable(false);
        jButton77.setText("Update");
        jButton77.setVisible(true);
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton73ActionPerformed
        isHospIdEntered();
        if (hospIdEntered) {
            int result = JOptionPane.showConfirmDialog(null, "Delete this ID?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                sql = "Delete from hosprecord where hosp_id = " + hospitalId;
                clearHospRecTF();
                exeUpdate(sql, rowDelete, hospRecSelect);
            }
        }
    }//GEN-LAST:event_jButton73ActionPerformed

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
        clearHospRecTF();
        jButton77.setVisible(false);
        jTextField11.setEditable(true);
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed
        clearHospRecTF();
        insert = true;
        jTextField11.setEditable(true);
        jButton77.setText("Save");
        jButton77.setVisible(true);
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        CL();
        CardLayout.add(AdminPage);
    }//GEN-LAST:event_jButton62ActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        jButton63.setVisible(false);
        jTextField8.setEditable(false);
        isJailIdEntered();

        if (jailIdEntered) {
            sql = "select * from jail where jail_id = " + jailId;
            jailView(sql);
        }
    }//GEN-LAST:event_jButton61ActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        jButton63.setText("Update");
        jButton63.setVisible(true);
        jTextField8.setEditable(false);
        update = true;
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        jButton63.setVisible(false);
        isJailIdEntered();
        if (jailIdEntered) {
            int result = JOptionPane.showConfirmDialog(null, "Delete this ID?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                sql = "Delete from jail where jail_id = " + jailId;
                clearJailTF();
                exeUpdate(sql, rowDelete, jailSelect);
            }
        }
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        clearJailTF();
        jTextField8.setEditable(true);
        jButton63.setVisible(false);
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton57ActionPerformed
        clearJailTF();
        insert = true;
        jTextField8.setEditable(true);
        jButton63.setText("Save");
        jButton63.setVisible(true);
    }//GEN-LAST:event_jButton57ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField28ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        try {
            int jId = Integer.parseInt(jTextField8.getText());
            int criId = Integer.parseInt(jTextField9.getText());
            String jailName = jTextField13.getText();
            String sentenceTime = jTextField10.getText();

            if (jailName.isEmpty() || sentenceTime.isEmpty()) {
                throw new EmptyTextField();
            }

            if (insert) {
                sql = "insert into jail values (" + jId + "," + criId + ", '" + jailName + "', '" + sentenceTime + "')";
                exeUpdate(sql, rowInsert, jailSelect);
                insert = false;
            } else if (update) {
                sql = "update jail set cr_id = " + criId + ", jail_name = '" + jailName + "', sentence_time = '" + sentenceTime + "' where jail_id = " + jId;
                exeUpdate(sql, rowUpdate, jailSelect);
                update = false;
            }

            jButton63.setVisible(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please fill ID value(s) as Numbers only.");
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        }
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CL();
        // Add new tab
        CardLayout.add(AdminPage);
        clearAATF();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            admId = Integer.parseInt(jTextField30.getText());
            String uName = jTextField29.getText();
            String pwd = jPasswordField2.getText();
            String cpwd2 = jPasswordField5.getText();
            String pwdHint = jTextField33.getText();

            if (uName.isEmpty() || pwd.isEmpty() || cpwd2.isEmpty()) {
                throw new EmptyTextField();
            }

            if (pwd.equals(cpwd2)) {
                sql = "insert into admin values(" + admId + ", '" + uName + "', '" + pwd + "', '" + pwdHint + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "New admin added successfully.");
                clearAATF();
                jButton10.setVisible(false);
                cn.commit();
                jButton11.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Password didn't match.");
                jPasswordField2.setText(null);
                jPasswordField5.setText(null);
            }
        } catch (NumberFormatException | EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill the Text fields.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please verify Admin Id.");
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            String cPasswd = jPasswordField4.getText();
            String nPasswd = jPasswordField3.getText();
            String confirmPasswd = jPasswordField6.getText();
            passwdHint = jTextField31.getText();

            if (nPasswd.isEmpty() || confirmPasswd.isEmpty() || passwdHint.isEmpty()) {
                throw new EmptyTextField();
            }

            if (cPasswd.equals(passwd)) {
                if (nPasswd.equals(confirmPasswd)) {
                    sql = "update admin set password = '" + nPasswd + "', passwd_hint = '" + passwdHint + "' where admin_id = " + admId;
                    st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "Password changed successfully.");
                    clearCPTF();
                    jButton11.setVisible(false);
                    cn.commit();
                    jButton10.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Password didn't match.");
                    jPasswordField3.setText(null);
                    jPasswordField6.setText(null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Current password is wrong. Please try again.");
                jPasswordField4.setText(null);
            }
        } catch (EmptyTextField ex) {
            JOptionPane.showMessageDialog(null, "Please fill Text Fields.");
        } catch (SQLException ex) {
            System.out.println("Error : " + ex);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jPasswordField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CDMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CDMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CDMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CDMS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new CDMS().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AdminLogin;
    private javax.swing.JPanel AdminPage;
    private javax.swing.JPanel BorderLayout;
    private javax.swing.JPanel CD;
    private javax.swing.JPanel CardLayout;
    private javax.swing.JPanel Crime;
    private javax.swing.JPanel Criminal;
    private javax.swing.JPanel HospitalRecord;
    private javax.swing.JPanel Jail;
    private javax.swing.JPanel ManageAdmin;
    private javax.swing.JPanel Police;
    private javax.swing.JPanel SearchRecords;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JPasswordField jPasswordField4;
    private javax.swing.JPasswordField jPasswordField5;
    private javax.swing.JPasswordField jPasswordField6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
