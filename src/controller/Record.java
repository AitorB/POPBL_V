/** @file Record.java
 *  @brief Class to store information about the records done with the Walkie-Talkie
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

package controller;

import java.io.Serializable;

public class Record  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String rute;
	private int minute;
	private int second;
	private int milisecond;
	
	public Record(String title, int minute, int second, int milisecond) {
		this.minute = minute;
		this.second = second; 
		this.milisecond = milisecond;
		this.title = title + ".wav";
		buildRute(title);
	}
	
	private void buildRute(String title) {
		this.rute = "record\\" + title + ".wav";
	}

	public String getTitle() {
		return title;
	}
	
	public String getRute() {
		return rute;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getMilisecond() {
		return milisecond;
	}
	
}
