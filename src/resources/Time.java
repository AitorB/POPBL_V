/** @file Time.java
 *  @brief Class to show current time in a label
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

package resources;

import java.awt.BorderLayout;
/** @brief Librerías
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Time extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel timeText;

	public Time(Color textColor, Font font, Color backgroundColor, boolean setOpaque) {
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setOpaque(setOpaque);

		timeText = new JLabel();
		timeText.setForeground(textColor);
		timeText.setFont(font);
		timeText.setHorizontalAlignment(JLabel.CENTER);

		this.add(timeText, BorderLayout.CENTER);

		start();
	}

	private void start() {
		Timer timer = new Timer(1000, this);

		timer.setInitialDelay(0);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalTime localeTime = LocalTime.now();

		int hour = localeTime.getHour();
		int minute = localeTime.getMinute();
		int second = localeTime.getSecond();

		timeText.setText(String.format("%02d : %02d : %02d", hour, minute, second));
	}
}