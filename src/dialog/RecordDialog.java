/** @file RecordDialog.java
 *  @brief Class that shows a dialog to enter the recorded audio name
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

package dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RecordDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextField textField;
	private JButton accept;

	protected String name;
	protected boolean acceptRecord;

	public RecordDialog(JFrame window, int width, int height) {
		super(window);

		this.setContentPane(dialogPanel());
		this.setPreferredSize(new Dimension(width, height));
		this.setResizable(false);
		this.setUndecorated(false);
		this.pack();
		this.setLocationRelativeTo(window);
		this.setModal(true);
		this.setVisible(true);
	}

	private Container dialogPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		panel.add(dataPanel(), BorderLayout.CENTER);
		panel.add(buttonPanel(), BorderLayout.SOUTH);

		return panel;
	}

	protected Component dataPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 0, 8));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 30));

		JLabel label = new JLabel("Record tittle:");
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setHorizontalAlignment(JTextField.CENTER);

		textField = new JTextField();
		textField.setFont(new Font("Arial", Font.BOLD, 20));
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accept.requestFocusInWindow();
			}
		});

		panel.add(label);
		panel.add(textField);

		return panel;
	}

	protected boolean emptyField() {
		boolean emptyField = false;

		if (textField.getText().isEmpty()) {
			emptyField = true;
		}

		return emptyField;
	}

	private boolean checkData() {
		boolean validData = true;

		if (!textField.getText().matches("[a-zA-Z0-9_-]{1,15}")) {
			validData = false;

			JOptionPane.showConfirmDialog(this, "Max length of 15, abailable characters: [a-z] [A-Z] [0-9] [_-]",
					"Error!", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

			textField.setText(null);
			textField.requestFocusInWindow();
		}

		return validData;
	}

	private Component buttonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		accept = new JButton("Accept");
		accept.setFont(new Font("Arial", Font.PLAIN, 14));
		accept.setPreferredSize(new Dimension(110, 30));
		accept.setMnemonic(KeyEvent.VK_A);
		accept.setActionCommand("accept");
		accept.addActionListener(this);

		JButton cancel = new JButton("Cancel");
		cancel.setFont(new Font("Arial", Font.PLAIN, 14));
		cancel.setPreferredSize(new Dimension(110, 30));
		cancel.setMnemonic(KeyEvent.VK_C);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);

		panel.add(accept);
		panel.add(Box.createRigidArea(new Dimension(50, 0)));
		panel.add(cancel);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("accept")) {
			if (!emptyField()) {
				if (checkData()) {
					name = textField.getText();
					acceptRecord = true;
					this.dispose();
				}
			} else {
				JOptionPane.showConfirmDialog(this, "Fill in all the data!", "Error!", JOptionPane.CLOSED_OPTION,
						JOptionPane.ERROR_MESSAGE);
				textField.selectAll();
				textField.requestFocusInWindow();
			}
		}
		if (e.getActionCommand().equals("cancel")) {
			acceptRecord = false;
			this.dispose();
		}
	}

	public String getName() {
		return this.name;
	}

	public boolean getAcceptRecord() {
		return this.acceptRecord;
	}
}