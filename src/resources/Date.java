/** @file Date.java
 *  @brief Class to show current date in a label
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
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Date extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Locale locale;
	private JLabel dateText;

	public Date(Color textColor, Font font, Color backgroundColor, boolean setOpaque) {
		this.setLayout(new BorderLayout());
		this.setBackground(backgroundColor);
		this.setOpaque(setOpaque);

		locale = Locale.getDefault();

		dateText = new JLabel();
		dateText.setHorizontalAlignment(JLabel.CENTER);
		dateText.setVerticalAlignment(JLabel.CENTER);
		dateText.setForeground(textColor);
		dateText.setFont(font);

		this.add(dateText, BorderLayout.CENTER);

		iniciar();
	}

	private void iniciar() {
		Timer timer = new Timer(1000, this);

		timer.setInitialDelay(0);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LocalDate localeDate = LocalDate.now();

		String dayOfWeek = localeDate.getDayOfWeek().getDisplayName(TextStyle.FULL, locale).substring(0, 1)
				.toUpperCase() + localeDate.getDayOfWeek().getDisplayName(TextStyle.FULL, locale).substring(1);
		int dayOfMonth = localeDate.getDayOfMonth();
		String month = localeDate.getMonth().getDisplayName(TextStyle.FULL, locale).substring(0, 1).toUpperCase()
				+ localeDate.getMonth().getDisplayName(TextStyle.FULL, locale).substring(1);
		int year = localeDate.getYear();

		dateText.setText(dayOfWeek + ", " + dayOfMonth + " de " + month + " de " + year);
	}
}