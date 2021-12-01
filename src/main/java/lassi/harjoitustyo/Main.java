package lassi.harjoitustyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		System.setProperty("java.awt.headless", "false"); //needed for GUI, otherwise crashes
		//Expense test1 = new Expense("2021-09-09", "testcategory", 69.0, "testcomment");
		//test1.setDate(2021, 9, 9);
		//test1.setCategory("testcat");
		//test1.setPrice(69.420);
		//test1.setComment("testcomment");
		//System.out.println(test1.toString());

		Database db1 = new Database();
		db1.addExpense("2021-10-10", "category1", 32, "comment1");
		db1.addExpense("2021-10-10", "category2", 32, "comment2");
		db1.addExpense("2021-10-10", "category3", 32, "comment3");
		db1.addExpense("2021-10-10", "category4", 32, "comment4");
		//db1.dumpDatabase();
		//System.out.println(db1.pipeToTable());
		for (Expense e : db1.db) {
			System.out.println(e.getComment());
		}

		GUI gui = new GUI(); //New Simple2 components
		BarGraph hp = new BarGraph();
		hp.createAndShowGraph(db1);

	}

}
