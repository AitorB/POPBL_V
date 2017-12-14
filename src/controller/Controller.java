/** @file Controller.java
 *  @brief Class to manage the control of a Walkie-Talkie
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import communication.CommunicationHandler;
import communication.SerialManagement;
import main.References;
import resources.Background;
import resources.ClipPlayer;
import resources.Countdown;
import resources.Date;
import resources.Time;

public class Controller extends JFrame {
	private static final long serialVersionUID = 1L;

	private List<Record> recordList;

	public Controller() {
		References.CLIP_PLAYER = new ClipPlayer(this);
		References.SERIAL_MANAGEMENT = new SerialManagement();
		References.COMMUNICATION_HANDLER = new CommunicationHandler(this);
		
		loadData();
		mainProgram();
	}

	private void mainProgram() {
		this.setTitle("Walkie-Talkie");
		this.setIconImage(new ImageIcon(References.LOGO_IMAGE).getImage());
		this.setContentPane(mainWindow());
		this.setPreferredSize(new Dimension(References.WIDTH_WINDOW, References.HEIGHT_WINDOW));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeApplication();
			}
		});
		this.setVisible(true);
	}

	private Container mainWindow() {
		References.KEYLISTENER_PANEL = new KeyListenerPanel(this);

		References.KEYLISTENER_PANEL.add(backgroundPanel());

		return References.KEYLISTENER_PANEL;
	}

	private Container backgroundPanel() {
		Image backgroundImage;
		backgroundImage = Toolkit.getDefaultToolkit().createImage(References.BACKGROUND_IMAGE);

		JPanel panel = new Background(backgroundImage);
		panel.setLayout(new BorderLayout());

		panel.add(infoPanel(), BorderLayout.NORTH);
		panel.add(dataPanel(), BorderLayout.CENTER);

		return panel;
	}

	private Component infoPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 0, 0));
		panel.setBorder(BorderFactory.createMatteBorder(3, 0, 3, 0, Color.BLACK));
		panel.setPreferredSize(new Dimension(0, 30));

		panel.add(new Date(Color.BLACK, new Font("Arial", Font.BOLD, 15), Color.WHITE, true));
		panel.add(new Time(Color.BLACK, new Font("Arial", Font.BOLD, 15), Color.WHITE, true));

		return panel;
	}

	private Component dataPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		References.STATUS_PANEL = new StatusPanel();
		References.RECORD_PANEL = new RecordPanel(this, recordList);

		panel.add(References.STATUS_PANEL, BorderLayout.CENTER);
		panel.add(References.RECORD_PANEL, BorderLayout.WEST);

		panel.setOpaque(false);

		return panel;
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		recordList = new ArrayList<>();

		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(References.RECORD_PATH))) {
			recordList = (List<Record>) reader.readObject();
		} catch (FileNotFoundException e) {
			File archive = new File(References.RECORD_PATH);
			try {
				archive.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveData() {
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(References.RECORD_PATH))) {
			recordList = References.RECORD_PANEL.getRecordList();
			writer.writeObject(recordList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeApplication() {
		int answer = JOptionPane.showConfirmDialog(this, "Close application?", "Alert!", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (answer == 0) {
			saveData();
			System.exit(0);
		}
	}

	public List<Record> getRecordList() {
		return this.recordList;
	}
}