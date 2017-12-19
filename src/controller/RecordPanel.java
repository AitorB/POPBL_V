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
import java.io.File;
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

import main.References;
import resources.Chronometer;

public class RecordPanel extends JPanel implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private JFrame window;
	private JButton play, pause, stop, delete, record;

	private JList<Record> recordJList;
	private DefaultListModel<Record> recordModel;
	private Record selectedRecord;
	private List<Record> recordList;
	private boolean recordON = false;

	public RecordPanel(JFrame window, List<Record> recordList) {
		super(new BorderLayout(0, 12));
		this.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 0));
		this.setOpaque(false);

		this.window = window;
		this.recordList = recordList;

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

		References.CHRONOMETER = new Chronometer(Color.BLACK, new Font("Arial", Font.BOLD, 40), Color.WHITE, false);

		panel.add(References.CHRONOMETER);

		return panel;
	}

	private Component buttonPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 0, 0));
		panel.setOpaque(false);

		play = new JButton(new ImageIcon(References.PLAY_IMAGE));
		play.setActionCommand("play");
		play.addActionListener(this);
		play.setContentAreaFilled(false);
		play.setEnabled(false);

		pause = new JButton(new ImageIcon(References.PAUSE_IMAGE));
		pause.setActionCommand("pause");
		pause.addActionListener(this);
		pause.setContentAreaFilled(false);
		pause.setEnabled(false);

		stop = new JButton(new ImageIcon(References.STOP_IMAGE));
		stop.setActionCommand("stop");
		stop.addActionListener(this);
		stop.setContentAreaFilled(false);
		stop.setEnabled(false);

		delete = new JButton(new ImageIcon(References.DELETE_IMAGE));
		delete.setActionCommand("delete");
		delete.addActionListener(this);
		delete.setContentAreaFilled(false);
		delete.setEnabled(false);

		if (!recordModel.isEmpty()) {
			play.setEnabled(true);
			delete.setEnabled(true);
			recordJList.setSelectedIndex(recordModel.size() - 1);
			selectedRecord = recordJList.getSelectedValue();
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

		record = new JButton("  REC", new ImageIcon(References.STARTREC_IMAGE));
		record.setFont(new Font("Arial", Font.BOLD, 60));
		record.setActionCommand("record");
		record.addActionListener(this);
		record.setContentAreaFilled(false);
		record.setEnabled(false);

		panel.add(record);

		return panel;
	}

	private void loadRecords() {
		for (Record record : recordList) {
			recordModel.addElement(record);
		}
	}

	public void setUIStatus(String status) {
		switch (status) {
		case "play":
			play.setEnabled(false);
			pause.setEnabled(true);
			stop.setEnabled(true);
			delete.setEnabled(false);
			recordJList.setEnabled(false);
			break;

		case "pause":
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(true);
			delete.setEnabled(false);
			break;

		case "stop":
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(false);
			delete.setEnabled(true);
			recordJList.setEnabled(true);
			break;

		case "delete":
			if (recordModel.isEmpty()) {
				play.setEnabled(false);
				delete.setEnabled(false);
			}
			break;

		case "transmissionON":
			play.setEnabled(false);
			delete.setEnabled(false);
			recordJList.setEnabled(false);
			record.setEnabled(true);
			break;

		case "transmissionOFF":
			if (!recordModel.isEmpty()) {
				play.setEnabled(true);
				delete.setEnabled(true);
				recordJList.setEnabled(true);
			}
			record.setEnabled(false);
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("play")) {
			setUIStatus("play");
			References.CLIP_PLAYER.play();

		} else if (e.getActionCommand().equals("pause")) {
			setUIStatus("pause");
			References.CLIP_PLAYER.pause();

		} else if (e.getActionCommand().equals("stop")) {
			setUIStatus("stop");
			References.CLIP_PLAYER.stop();

		} else if (e.getActionCommand().equals("delete")) {
			int answer = JOptionPane.showConfirmDialog(window, "Delete current record?", "Alert!",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer == 0) {
				try {
					Record recordToDelete = recordJList.getSelectedValue();
					new File(recordToDelete.getRelativePath()).delete();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				recordModel.removeElement(recordJList.getSelectedValue());
				setUIStatus("delete");
				recordJList.setSelectedIndex(recordModel.size() - 1);
			}
		} else if (e.getActionCommand().equals("record")) {
			if (!recordON) {
				startRecord();
			} else {
				References.KEYLISTENER_PANEL.stopTransmission();
				stopRecord();
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList<?> list = (JList<?>) e.getSource();
			if (!list.isSelectionEmpty()) {
				selectedRecord = (Record) list.getSelectedValue();
				References.CHRONOMETER.setChronometerValue(String.format("%02d : %02d . %02d",
						selectedRecord.getMinutes(), selectedRecord.getSeconds(), selectedRecord.getHundreths()));
			} else {
				References.CHRONOMETER.setChronometerValue("00 : 00 . 00");
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

	public void startRecord() {
		recordON = true;
		record.setIcon(new ImageIcon(References.STOPREC_IMAGE));
		References.CHRONOMETER.start();

		References.COMMUNICATION_HANDLER.startRecord();
	}

	public void stopRecord() {
		References.CHRONOMETER.pause();
		recordON = false;
		record.setIcon(new ImageIcon(References.STARTREC_IMAGE));
		setUIStatus("transmissionOFF");
		References.COMMUNICATION_HANDLER.stopRecord();
		recordJList.setSelectedIndex(recordModel.size() - 1);
	}

	public DefaultListModel<Record> getRecordModel() {
		return recordModel;
	}

	public Record getSelectedRecord() {
		return this.selectedRecord;
	}

	public boolean getRecordON() {
		return this.recordON;
	}
}