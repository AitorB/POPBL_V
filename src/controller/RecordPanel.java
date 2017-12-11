/** @file RecordPanel.java
 *  @brief Class that shows the panel with the recording controls
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dialog.RecordDialog;
import resources.Chronometer;

public class RecordPanel extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	protected static final String ICON_PLAY = "icon\\play.png";
	protected static final String ICON_PAUSE = "icon\\pause.png";
	protected static final String ICON_STOP = "icon\\stop.png";
	protected static final String ICON_DELETE = "icon\\delete.png";
	protected static final String ICON_STARTREC = "icon\\startRec.png";
	protected static final String ICON_STOPREC = "icon\\stopRec.png";

	private JFrame window;
	private Transmission transmisionPanel;
	private JList<Record> recordJList;
	private DefaultListModel<Record> recordModel;

	private JButton play, pause, stop, delete;
	private JButton record;

	private Record newRecord, selectedRecord;
	private List<Record> recordList;
	private boolean recordStatus;
	private Chronometer chronometer;

	public RecordPanel(JFrame window, List<Record> recordList, Transmission transmisionPanel) {
		super(new BorderLayout(0, 12));
		this.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0));
		this.setOpaque(false);

		this.window = window;
		this.recordList = recordList;
		recordStatus = false;
		this.transmisionPanel = transmisionPanel;

		this.add(topPanel(), BorderLayout.CENTER);
		this.add(bottomPanel(), BorderLayout.SOUTH);
	}

	private Component topPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setOpaque(false);

		panel.add(recordPanel(), BorderLayout.CENTER);
		panel.add(controlPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private Component recordPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setOpaque(false);

		panel.add(titlePanel(), BorderLayout.NORTH);
		panel.add(listPanel(), BorderLayout.CENTER);

		return panel;
	}

	private Component titlePanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setOpaque(false);

		JLabel title = new JLabel("MY RECORDS . . .");
		title.setFont(new Font("Arial", Font.BOLD, 20));

		panel.add(title);

		return panel;
	}

	private Component listPanel() {
		JScrollPane panelScroll = new JScrollPane();

		panelScroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 10, 0, 0)));
		panelScroll.setBackground(new Color(255, 155, 109));
		panelScroll.setOpaque(true);
		panelScroll.setPreferredSize(new Dimension(0, 250));

		recordJList = new JList<>();
		recordJList.addListSelectionListener(this);

		recordModel = new DefaultListModel<>();

		loadRecords();

		recordJList.setModel(recordModel);

		recordJList.setCellRenderer(new RecordAdapter());

		panelScroll.setViewportView(recordJList);
		
		return panelScroll;
	}

	private Component controlPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
		panel.setOpaque(false);

		panel.add(chronometerPanel(), BorderLayout.CENTER);
		panel.add(buttonPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private Component chronometerPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		panel.setOpaque(false);

		chronometer = new Chronometer(Color.BLACK, new Font("Arial", Font.BOLD, 40), Color.WHITE, false);

		panel.add(chronometer);

		return panel;
	}

	private Component buttonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 0, 0));
		panel.setOpaque(false);

		play = new JButton(new ImageIcon(ICON_PLAY));
		play.setActionCommand("play");
		play.addActionListener(this);
		play.setContentAreaFilled(false);
		play.setEnabled(true);
		
		pause = new JButton(new ImageIcon(ICON_PAUSE));
		pause.setActionCommand("pause");
		pause.addActionListener(this);
		pause.setContentAreaFilled(false);
		pause.setEnabled(false);
		
		stop = new JButton(new ImageIcon(ICON_STOP));
		stop.setActionCommand("stop");
		stop.addActionListener(this);
		stop.setContentAreaFilled(false);
		stop.setEnabled(false);
		
		delete = new JButton(new ImageIcon(ICON_DELETE));
		delete.setActionCommand("delete");
		delete.addActionListener(this);
		delete.setContentAreaFilled(false);
		delete.setEnabled(true);
		
		if (recordModel.isEmpty()) {
			play.setEnabled(false);
			delete.setEnabled(false);
		} else {
			recordJList.setSelectedIndex(recordModel.size() - 1);
		}

		panel.add(play);
		panel.add(pause);
		panel.add(stop);
		panel.add(delete);

		return panel;
	}

	private Component bottomPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setOpaque(false);

		record = new JButton("  REC", new ImageIcon(ICON_STARTREC));
		record.setFont(new Font("Arial", Font.BOLD, 60));
		record.setActionCommand("record");
		record.addActionListener(this);
		record.setContentAreaFilled(false);

		panel.add(record);

		return panel;
	}

	private void loadRecords() {
		for (Record record : recordList) {
			recordModel.addElement(record);
		}
	}
	
	public void setButtonStatus(String status) {

		switch (status) {
		case "play":
			play.setEnabled(false);
			pause.setEnabled(true);
			stop.setEnabled(true);
			delete.setEnabled(false);
			record.setEnabled(false);
			
			transmisionPanel.communicationAvailable(false);
			break;

		case "pause":
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(true);
			delete.setEnabled(false);
			record.setEnabled(false);
			
			break;

		case "stop":
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(false);
			delete.setEnabled(true);
			record.setEnabled(true);
			
			transmisionPanel.communicationAvailable(true);
			break;

		case "delete":
			if (recordModel.isEmpty()) {
				play.setEnabled(false);
				delete.setEnabled(false);
			}
			break;

		case "recording":
			play.setEnabled(false);
			pause.setEnabled(false);
			stop.setEnabled(false);
			delete.setEnabled(false);
			break;

		case "stopped":
			if (!recordModel.isEmpty()) {
				play.setEnabled(true);
				delete.setEnabled(true);
			}
			break;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("play")) {
			System.out.println("PLAY SELECTED RECORD");
			setButtonStatus("play");

		} else if (e.getActionCommand().equals("pause")) {
			System.out.println("PAUSE SELECTED RECORD");
			setButtonStatus("pause");

		} else if (e.getActionCommand().equals("stop")) {
			System.out.println("STOP SELECTED RECORD");
			setButtonStatus("stop");

		} else if (e.getActionCommand().equals("delete")) {
			int answer = JOptionPane.showConfirmDialog(window, "Delete current record?", "Alert!",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

			if (answer == 0) {
				recordModel.removeElement(recordJList.getSelectedValue());
				setButtonStatus("delete");
			}

		} else if (e.getActionCommand().equals("record")) {
			if (!recordStatus) {
				recordStatus = true;
				setButtonStatus("recording");
				record.setIcon(new ImageIcon(ICON_STOPREC));
				chronometer.start();
				startRecord();
			} else {
				recordStatus = false;
				record.setIcon(new ImageIcon(ICON_STARTREC));
				chronometer.stop();
				stopRecord();
				setButtonStatus("stopped");
			}
		}

		recordJList.setSelectedIndex(recordModel.size() - 1);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList<?> list = (JList<?>) e.getSource();
			if (!list.isSelectionEmpty()) {
				selectedRecord = (Record) list.getSelectedValue();
				chronometer.setChronometer(String.format("%02d : %02d : %02d", selectedRecord.getMinute(),
						selectedRecord.getSecond(), selectedRecord.getMilisecond()));
			} else {
				chronometer.setChronometer("00 : 00 : 00");
			}
		}

	}

	public List<Record> getRecordList() {
		List<Record> list = new ArrayList<>();

		for (int i = 0; i < recordModel.size(); i++) {
			list.add(recordModel.getElementAt(i));
		}

		return list;
	}


	private void startRecord() {
		System.out.println("START RECORDING");
		// Listen to the microphone
	}

	private void stopRecord() {
		System.out.println("STOP RECORDING");
		// Stop recording
		
		RecordDialog dialog = new RecordDialog(window, 420, 150);

		if (dialog.getSaveRecord()) {
			newRecord = new Record(dialog.getTitle(), chronometer.getMinute(), chronometer.getSecond(),
					chronometer.getMilisecond());
			recordModel.addElement(newRecord);
			setButtonStatus("stop");
		}
		// Save record to disc
	}

}
