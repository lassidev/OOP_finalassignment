package lassi.harjoitustyo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.swing.JTable;
import javax.swing.table.TableModel;


public class Database {
    
    public ArrayList<Expense> db = new ArrayList<Expense>();

    public void addExpense(String date, String category, double price, String comment) {
        db.add( new Expense(date, category, price, comment ));
    }

    public void addExpense(String[] metadata) {
        String date = metadata[0];
        String category = metadata[1];
        double price = Double.parseDouble(metadata[2]);
        String comment = metadata[3];
        db.add( new Expense(date, category, price, comment ));
    }

    public void readExpensesFromCSV(String fileName) {
        db.clear(); //Empty database list
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            
            br.readLine(); //read first line to skip columns
            String line = br.readLine(); //second line  
            while (line != null) { // loop until all lines are read
                String[] attributes = line.split(","); //values to array, separated by comma
                addExpense(attributes); //create new expense from array values
                line = br.readLine(); // read next line before looping, if end of file reached, line would be null
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean exportToCSV(JTable tableToExport, String pathToExportTo) {

    try {

        TableModel model = tableToExport.getModel();
        FileWriter csv = new FileWriter(new File(pathToExportTo));

        //write columns
        for (int i = 0; i < model.getColumnCount(); i++) {
            csv.write(model.getColumnName(i));
            if (i != model.getColumnCount() - 1) { //only write comma if not last column
                csv.write(",");
            }
        }
        csv.write("\n");

        //write rows
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                csv.write(model.getValueAt(i, j).toString());
                if (j != model.getColumnCount() - 1) { //only write comma if not last column
                    csv.write(",");
                }
            }
            csv.write("\n");
        }

        csv.close();
        return true;
        } catch (IOException e) {
        e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
	}
}
