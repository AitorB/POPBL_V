/** @file StatusPanel.java
 *  @brief Class that shows the panel with information on the status of the walkie talkie 
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.References;

public class StatusPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	JLabel statusImage, statusText;

	public StatusPanel() {
		super(new BorderLayout(0, 12));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.add(topPanel(), BorderLayout.CENTER);
		this.add(bottomPanel(), BorderLayout.SOUTH);

		this.setOpaque(false);
	}

	private Component topPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
		panel.setOpaque(false);

		statusImage = new JLabel(new ImageIcon(References.STANDBY_IMAGE));

		panel.add(statusImage);

		return panel;
	}

	private Component bottomPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 150, 10, 150));
		panel.setOpaque(false);

		panel.add(statusPanel());

		return panel;
	}

	private Component statusPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());

		statusText = new JLabel("STAND BY");
		statusText.setHorizontalAlignment(JLabel.CENTER);
		statusText.setFont(new Font("Arial", Font.BOLD, 50));
		statusText.setPreferredSize(new Dimension(600, 60));

		panel.add(statusText);

		return panel;
	}

	public void setStatus(String status) {
		switch (status) {
		case "standBy":
			statusImage.setIcon(new ImageIcon(References.STANDBY_IMAGE));
			statusText.setText(References.STANDBY_TEXT);
			break;
			
		case "transmitting":
			statusImage.setIcon(new ImageIcon(References.TRANSMITTING_IMAGE));
			statusText.setText(References.TRANSMITTING_TEXT);
			break;

		case "waiting":
			statusImage.setIcon(new ImageIcon(References.WAITTING_IMAGE));
			statusText.setText(References.WAITTING_TEXT);
			break;
			
		case "receiving":
			statusImage.setIcon(new ImageIcon(References.RECEIVING_IMAGE));
			statusText.setText(References.RECEIVING_TEXT);
			break;
		}
	}
}