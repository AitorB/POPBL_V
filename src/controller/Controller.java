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

import main.Main;
import resources.Background;
import resources.ClipPlayer;
import resources.Date;
import resources.Time;

public class Controller extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String BACKGROUND_IMAGE = "image\\background.jpg";

	private ClipPlayer clipPlayer;
	private CommunicationHandler communicationHandler;
	
	private KeyListenerPanel keyListenerPanel;
	private StatusPanel statusPanel;
	private RecordPanel recordPanel;
	private List<Record> recordList;

	public Controller() {
		clipPlayer = new ClipPlayer(this);
		communicationHandler = new CommunicationHandler();
		loadData();
		mainProgram();
	}

	private void mainProgram() {
		this.setTitle("Walkie-Talkie");
		this.setIconImage(new ImageIcon("icon\\mu.png").getImage());
		this.setContentPane(mainWindow());
		this.setPreferredSize(new Dimension(Main.getWidthWindow(), Main.getHeightWindow()));
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
		keyListenerPanel = new KeyListenerPanel(this);

		keyListenerPanel.add(backgroundPanel());

		return keyListenerPanel;
	}

	private Container backgroundPanel() {
		Image backgroundImage;
		backgroundImage = Toolkit.getDefaultToolkit().createImage(BACKGROUND_IMAGE);

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

		statusPanel = new StatusPanel();
		recordPanel = new RecordPanel(this, recordList);

		panel.add(statusPanel, BorderLayout.CENTER);
		panel.add(recordPanel, BorderLayout.WEST);

		panel.setOpaque(false);

		return panel;
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		recordList = new ArrayList<>();

		try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(Main.RECORD_DATA))) {
			recordList = (List<Record>) reader.readObject();
		} catch (FileNotFoundException e) {
			File archive = new File(Main.RECORD_DATA);
			try {
				archive.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void saveData() {
		try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(Main.RECORD_DATA))) {
			recordList = recordPanel.getRecordList();
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
		return recordList;
	}

	public StatusPanel getStatusPanel() { 
		return this.statusPanel;
	}
	
	public RecordPanel getRecordPanel() {
		return this.recordPanel;
	}

	public KeyListenerPanel getKeyListenerPanel() {
		return this.keyListenerPanel;
	}

	public ClipPlayer getClipPlayer() {
		return clipPlayer;
	}

	public CommunicationHandler getCommunicationHandler() {
		return communicationHandler;
	}

}