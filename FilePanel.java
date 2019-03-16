import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * This class represents the panel that allows the user to select the inspection choice aswell as choose a file.
 * @author Luke
 */
public class FilePanel extends JPanel {

	/*
	 * Declaring all variables and components to be used within the GUI
	 */
	private JButton fileChooserButton;
	private static JRadioButton writeToDiskNoWarning;
	private static JRadioButton singleFileRadio, directoryRadio,  directoryMetaDataRadio;
	private ButtonGroup optionGroup;
	private File importedFile;
	private String importedFilePath, importedFileName;
	private JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
	private int inspectionChoice, algorithmChoice, userFileChoice;
	private PublicInterfaces.DialogInterface interForDialogs =  new Dialogs();
	
	/**
	 * FilePanel constructor
	 */
	public FilePanel(String myTitle) {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(280,  180));

		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(myTitle),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		fileChooserButton = new JButton("Click to select a file/directory to inspect");
		add(fileChooserButton);
		optionGroup = new ButtonGroup();
		singleFileRadio = new JRadioButton("Inspect single file (default)");
		singleFileRadio.setSelected(true);
		optionGroup.add(singleFileRadio);
		add(singleFileRadio);
		singleFileRadio.setToolTipText("Generate hash based on the contents of a single file.");
		directoryRadio = new JRadioButton("Inspect directory");
		optionGroup.add(directoryRadio);
		add(directoryRadio);
		directoryRadio.setToolTipText("Generate hash based on the contents of a directory.");
		directoryMetaDataRadio = new JRadioButton("Inspect directory meta-data");
		optionGroup.add(directoryMetaDataRadio);
		add(directoryMetaDataRadio);
		directoryMetaDataRadio.setToolTipText("Generate hash based on the meta-data of a selected directory.");
		writeToDiskNoWarning = new JRadioButton("\nOutput generated output to disk.");
		add(writeToDiskNoWarning);
		writeToDiskNoWarning.setToolTipText("Write data collected about selected file to disk.");

		fileChooserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userFileChoice = fileChooser.showOpenDialog(fileChooserButton);
				if (userFileChoice == JFileChooser.APPROVE_OPTION) {
				
					importedFile = fileChooser.getSelectedFile();
					inspectionChoice = getSelectedOption();
					importedFilePath = (inspectionChoice == 1) ? importedFile.getPath() : importedFile.getParent();
					algorithmChoice = HashPanel.getSelectedButton();
					importedFileName = importedFile.getName();
					
					try {
						new AccessFile(importedFilePath, importedFileName, inspectionChoice, algorithmChoice);
					} catch (IOException e1) {
						interForDialogs.exceptionIO();
					}
					
			    } else {
			    	interForDialogs.selectionCancelled();
					Output.setFileOutput("User has cancelled file selection.");
				}
		    }
	    });

     }

	/**
	 * Returns the current inspection choice
	 * (Single file, Directory or Directory Meta Data)
	 * @return 1, 2 or 3
	 */
	public static int getSelectedOption() {
		return singleFileRadio.isSelected() ? 1 : directoryRadio.isSelected() ? 2 :  directoryMetaDataRadio.isSelected()  ? 3 : 0;
	}
	
	/**
	 * Returns the state of the "Output generated output to disk" button
	 * @return TRUE/FALSE
	 */
	public static boolean getWriteToDisk() {
		return writeToDiskNoWarning.isSelected() ? true : false;
	}
}