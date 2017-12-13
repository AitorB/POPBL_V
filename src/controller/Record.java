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

public class Record implements Serializable {
	private static final long serialVersionUID = 1L;

	private String title;
	private String relativePath;
	private int minutes;
	private int seconds;
	private int hundredths;

	public Record(String title, int minutes, int seconds, int hundredths) {
		this.minutes = minutes;
		this.seconds = seconds;
		this.hundredths = hundredths;
		this.title = title + ".wav";
		buildPath(this.title);
	}

	private void buildPath(String title) {
		this.relativePath = "record\\" + title;
	}

	public String getTitle() {
		return this.title;
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public int getMinutes() {
		return this.minutes;
	}

	public int getSeconds() {
		return this.seconds;
	}
	
	public int getHundreths() {
		return this.hundredths;
	}
}