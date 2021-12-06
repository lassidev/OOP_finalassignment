package lassi.harjoitustyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args); //Why is this a spring app? Good question.
		System.setProperty("java.awt.headless", "false"); //needed for GUI, otherwise crashes
		GUI gui = new GUI(); //Start GUI

	}

}
