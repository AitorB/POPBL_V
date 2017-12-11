/** @file Background.java
 *  @brief Class to show an image in a frame
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

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class Background extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Image image;
	
	public Background(Image image) {
		if (image != null) {
			this.image = image;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			setOpaque(false);
		} else {
			setOpaque(true);
		}
		super.paint(g);
	}
	
	public void setImagen(Image image) {
		this.image = image;
		repaint();
	}
	
}