package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PrintReceipt extends JFrame implements ActionListener {

    JTextArea area;
    JButton print, back;
    String rollno;

    PrintReceipt(String rollno) {
        this.rollno = rollno;
        setSize(400, 600);
        setLocation(500, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Fee Receipt");
        heading.setBounds(130, 10, 200, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane pane = new JScrollPane(area);
        pane.setBounds(20, 50, 345, 430);
        add(pane);

        print = new JButton("Print");
        print.setBounds(50, 500, 120, 30);
        print.setBackground(Color.BLACK);
        print.setForeground(Color.WHITE);
        print.addActionListener(this);
        add(print);

        back = new JButton("Back");
        back.setBounds(210, 500, 120, 30);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        try {
            Conn c = new Conn();
            // Trim the rollno to ensure no hidden spaces cause a mismatch
            String searchRoll = rollno.trim();

            // 1. Fetch Student Details
            ResultSet rs = c.statement.executeQuery("select * from student where rollno = '"+searchRoll+"'");
            if (rs.next()) {
                area.setText("\t--------------------------------\n");
                area.append("\t   Y.L. TECHNICAL UNIVERSITY \n");
                area.append("\t--------------------------------\n\n");
                area.append(" Roll Number:    " + rs.getString("rollno") + "\n");
                area.append(" Name:           " + rs.getString("name") + "\n");
                area.append(" Course:         " + rs.getString("course") + "\n");
                area.append(" Branch:         " + rs.getString("branch") + "\n");
            }

            // 2. Fetch Fee Details using the exact same trimmed roll number
            ResultSet rs2 = c.statement.executeQuery("select * from feecollege where rollno = '"+searchRoll+"'");

            if (rs2.next()) {
                area.append(" Semester:       " + rs2.getString("semester") + "\n");
                area.append("\n --------------------------------------\n");
                // Using 'total' column as per your feecollege table schema
                area.append(" Total Fee Paid: " + rs2.getString("total") + "\n");
                area.append(" --------------------------------------\n");
            } else {
                area.append("\n [Error: Total Fee Not Found in feecollege]\n");
                area.append(" Searched for: '" + searchRoll + "'\n");
            }

            area.append("\n\n\n\n\t\t Signature: ___________");

        } catch (Exception e) {
            area.append("\n Error: " + e.getMessage());
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == print) {
            try {
                area.print(); // Opens native printer dialog
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new PrintReceipt("");
    }
}