/** @file Main.java
 *  @brief Main class of the program to manage the control of a Walkie-Talkie
 *  @authors
 *  Name          | Surname        | Email                                |
 *  ------------- | -------------- | ------------------------------------ |
 *  Aitor         | Barreiro       | aitor.barreirom@alumni.mondragon.edu |
 *  Mikel         | Hernandez      | mikel.hernandez@alumni.mondragon.edu |
 *  Unai          | Iraeta         | unai.iraeta@alumni.mondragon.edu     |
 *  Iker	      | Mendi          | iker.mendi@alumni.mondragon.edu      |
 *  Julen	      | Uribarren	   | julen.uribarren@alumni.mondragon.edu |
 *  @date 20/01/2018
 */

package main;

import javax.swing.UIManager;

import controller.Controller;

public class Main {
	public static final int WIDTH_WINDOW = 1280;
	public static final int HEIGHT_WINDOW = 720;
	public static Controller controller;
	
	public static final String RECORD_DATA = "data\\records.dat";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		controller = new Controller();
	}
}