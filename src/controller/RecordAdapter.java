/** @file RecordAdapter.java
 *  @brief Class that allows you to customize the appearance in which the information of the circuit list is displayed
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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class RecordAdapter implements ListCellRenderer<Record> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Record> recordList, Record record, int index,
			boolean selected, boolean focus) {

		JPanel panel = new JPanel(new BorderLayout(10, 10));

		JLabel lTitulo = new JLabel(record.getTitle());
		lTitulo.setFont(new Font("Arial", Font.CENTER_BASELINE, 16));
		lTitulo.setForeground(selected ? Color.WHITE : Color.BLACK);

		panel.add(lTitulo, BorderLayout.CENTER);

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(selected ? new Color(255, 107, 107) : Color.WHITE);
		panel.setOpaque(true);

		return panel;
	}
	
}
