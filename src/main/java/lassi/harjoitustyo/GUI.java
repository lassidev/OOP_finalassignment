package lassi.harjoitustyo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class GUI extends Database
{
     private static JFrame frame; 
     private static JTable table;
     private static DefaultTableModel tableModel;
     private static JPanel myPanel; //The panel is used to place button components
     private static JMenuBar menuBar;
     private JButton saveButton; 
     private JTextField date; //fix this later with date picker
     private JComboBox<String> category;
     private JTextField price;
     private JTextField comment;
     private Database database = new Database();

    public JMenuBar menuBar() {
        menuBar = new JMenuBar();
        menuBar.add(fileMenu());
        menuBar.add(editMenu());
        menuBar.add(viewMenu());
        menuBar.add(helpMenu());
        return menuBar;
    }

    public JMenu fileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open file");
        openFile.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    String filePath = fileChooser();
                    database.readExpensesFromCSV(filePath);
                    populateTable();
                }
            }
        );
        JMenuItem saveFile = new JMenuItem("Save as");
        saveFile.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    String filePath = fileChooser();
                    exportToCSV(table, filePath);
                }
            }
        );
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        return fileMenu;
    }

    public JMenu editMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem filterExpensesByDate = new JMenuItem("Filter expenses by date");
        filterExpensesByDate.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    tableToDatabase();
                    String date = JOptionPane.showInputDialog(frame,"Enter date");
                    if (date != null) {
                        filterTableByDate(date);
                    }
                }
            }
        );
        editMenu.add(filterExpensesByDate);
        return editMenu;
    }

    public JMenu viewMenu() {
        JMenu viewMenu = new JMenu("View");
        JMenuItem exportGraph = new JMenuItem("Bar graph summary");
        exportGraph.addActionListener(
            new ActionListener()
            {
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            tableToDatabase();
                            BarGraph bg = new BarGraph();
                            bg.createAndShowGraph(database);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            });
            viewMenu.add(exportGraph);
        return viewMenu;
    }

    public JMenu helpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem getHelp = new JMenuItem("Get help");
        getHelp.addActionListener(
            new ActionListener()
            {
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            String url = "https://betterprogramming.pub/we-need-to-talk-about-mental-health-for-software-developers-65bfa00e2356"; // capture the URL when the user presses the button.
                            Desktop desktop = java.awt.Desktop.getDesktop();
                            URI oURL = new URI(url);
                            desktop.browse(oURL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            });
        JMenuItem debug = new JMenuItem("Debug");
        debug.addActionListener(
            new ActionListener()
            {
                    public void actionPerformed(ActionEvent e) {
                        tableToDatabase();
                    }
            });
        helpMenu.add(debug);
        helpMenu.add(getHelp);
        return helpMenu;
    }

    public JScrollPane table() {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("Date");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Comment");
        JScrollPane sp = new JScrollPane(table);
        return sp;

    }
    
    public void populateTable() {
        tableModel.setRowCount(0); //prevent duplicate adding
        for (Expense e : database.db) {
            tableModel.addRow(new Object[]{e.getDate(), e.getCategory(), e.getPrice(), e.getComment()});
		}
    }

    public void tableToDatabase() {
        String tempDate = "";
        String tempCategory = "";
        double tempPrice = 0;
        String tempComment = "";

        //System.out.println(tableModel.getValueAt(3, 2));
        database.db.clear(); //clear database to prevent duplicate adding
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                //System.out.println( tableModel.getValueAt(i, j).toString());
                if (j==0) {
                    //System.out.println("Date: " + tableModel.getValueAt(i, j).toString());
                    tempDate = tableModel.getValueAt(i, j).toString();
                } if (j==1) {
                    //System.out.println("Category: " + tableModel.getValueAt(i, j).toString());
                    tempCategory = tableModel.getValueAt(i, j).toString();
                } if (j==2) {
                    //System.out.println("Price: " + tableModel.getValueAt(i, j).toString());
                    tempPrice = Double.parseDouble(tableModel.getValueAt(i, j).toString());
                } if (j==3) {
                    //System.out.println("Comment: " + tableModel.getValueAt(i, j).toString());
                    tempComment = tableModel.getValueAt(i, j).toString();
                }
                }
            database.addExpense(tempDate, tempCategory, tempPrice, tempComment);
            }
        }
    
    public void filterTableByDate(String date) {
        tableModel.setRowCount(0); //prevent duplicate adding
        for (Expense e : database.db) {
            if (date.equals(e.getDateString())) {
                tableModel.addRow(new Object[]{e.getDate(), e.getCategory(), e.getPrice(), e.getComment()});
                //System.out.println(e.getDateString());
            }
		}
    }


    public JPanel panel() {
        myPanel = new JPanel();
         //Save expense button
         saveButton = new JButton("Save expense");
         //Each event source needs a listener
         saveButton.addActionListener(
             new ActionListener()
             {
                 public void actionPerformed(ActionEvent e)
                 {
                     //JOptionPane.showMessageDialog(frame,category.getText());
                     database.addExpense(date.getText(), (String)category.getSelectedItem(), Double.parseDouble(price.getText()), comment.getText());
                    //output.setText(db.dumpDatabase());
                    populateTable();
                 }
             }
         );


         date = new JTextField(10);
         category = new JComboBox<>();
         category.setEditable(true);
         category.addPopupMenuListener(
            new PopupMenuListener()
            {
                public void popupMenuCanceled(PopupMenuEvent e) {
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuWillBecomeVisible(PopupMenuEvent e)
                {
                    category.removeAllItems();
                    //tableModel 
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        category.addItem(table.getValueAt(i,1).toString());
                    }
                }
            }
         );
         price = new JTextField(10);
         comment = new JTextField(20);

         myPanel.add(new JLabel("Date:"));
         myPanel.add(date);
         myPanel.add(new JLabel("Category:"));
         myPanel.add(category);
         myPanel.add(new JLabel("Price:"));
         myPanel.add(price);
         myPanel.add(new JLabel("Comment:"));
         myPanel.add(comment);
         myPanel.add(saveButton);
         return myPanel;


    }

    public String fileChooser() {
        String filePath = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePath = selectedFile.getAbsolutePath();
        }
        return filePath;
        
    }

     public GUI() { //Constructor, graphical interface

         //New panel
         

         // building the GUI
         frame = new JFrame("Expense Manager"); //New JFrame
         //Common methods for handling closure events
         frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e)
         {System.exit(0);} });
         frame.getContentPane().add(BorderLayout.NORTH, menuBar());
         frame.getContentPane().add(table());
         //frame.getContentPane().add(output);
         frame.getContentPane().add(BorderLayout.SOUTH, panel());
         frame.pack(); 
         //frame.setSize(600, 400);
         frame.setVisible(true);

        }
        
        public static void main(String s[]) {
     
            
            GUI gui = new GUI(); //New Simple2 components
                             

        }
}