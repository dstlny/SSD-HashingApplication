import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * MainPanel class.
 * Sets up the program.
 * Has a driver.
 * Sets up menu options.
 * @author Luke
 */
@SuppressWarnings("serial")
public class MainPanel extends JFrame {
	
	////////////////////////////////////////////////////////////
	private JPanel mainPanel;
	private HashPanel hashingPanel;
	private FilePanel filePanel;
	private Output outputPanel;
	private JMenuBar menuBar;
	private JMenu menuBarSubMenu1, menuBarSubMenu2;
	@SuppressWarnings("unused")
	private JMenuItem menuBarSubMenu1Item1, menuBarSubMenu2Item1,menuBarSubMenu1Item2, menuBarSubMenu1Item3;
	private File importedFile;
	private JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
	private String importedFilePath, importedFileName;
	private int optionChoice, algorithmChoice, userFileChoice;
	private PublicInterfaces.DialogInterface interForDialogs =  new Dialogs();
	////////////////////////////////////////////////////////////
	
	/**
	 * Driver
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupAndDrawGUI();
			}
		});
		
	}
	
	/**
	 * MainPanel constructor. Here we'll add all the JPanels we need, as well as create borders etc.
	 */
	public MainPanel() {
		filePanel = new FilePanel("File/Directory Selection");
		outputPanel = new Output("Generated Output");
		hashingPanel = new HashPanel("Algorithms");
		mainPanel = new JPanel();
		mainPanel.add(filePanel);
		mainPanel.add(hashingPanel);
		mainPanel.add(outputPanel);
		mainPanel.setPreferredSize(new Dimension(550, 510));
		mainPanel.setBackground(Color.WHITE);
	}
	
	/**
	 * Sets up and draws the GUI. Pretty self explanatory.
	 * Private because it doesn't need to be accessed, only instantiated.
	 */
	private final static void setupAndDrawGUI() {
		JFrame progFrame = new JFrame("Hash Generator");
		progFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainPanel HashGenerator  = new MainPanel();
		progFrame.setContentPane(HashGenerator.mainPanel);
		progFrame.setJMenuBar(HashGenerator.setupMenu());
		progFrame.pack();
		progFrame.setVisible(true);
	}
	
	/**
	 * Sets up the Menu system.
	 * Private because it doesn't need to be accessed, only instantiated.
	 */
	private JMenuBar setupMenu() {

		menuBar = new JMenuBar();
		menuBarSubMenu1 = new JMenu("<html><u>F</u>ile</html>");
		menuBarSubMenu1.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuBarSubMenu1);

		menuBarSubMenu1Item1 = new JMenuItem("<html><u>E</u>xit</html>");
		menuBarSubMenu1.add(menuBarSubMenu1Item1);
		menuBarSubMenu1Item1.setToolTipText("Press this button to exit the application.");
		menuBarSubMenu1Item1.setMnemonic(KeyEvent.VK_E);
		menuBarSubMenu1Item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menuBarSubMenu1Item2 = new JMenuItem("<html><u>S</u>elect File/Directory</html>");
		menuBarSubMenu1.add(menuBarSubMenu1Item2);
		menuBarSubMenu1Item2.setToolTipText("Press this button if you would like to select a new file/directory.");
		menuBarSubMenu1Item2.setMnemonic(KeyEvent.VK_I);
		menuBarSubMenu1Item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userFileChoice = fileChooser.showOpenDialog(menuBarSubMenu1Item2);
				if (userFileChoice == JFileChooser.APPROVE_OPTION) {
						
					    importedFile = fileChooser.getSelectedFile();
						optionChoice = FilePanel.getSelectedOption();
						importedFilePath = (optionChoice == 1) ? importedFile.getPath() : importedFile.getParent();
						algorithmChoice = HashPanel.getSelectedButton();
						importedFileName = importedFile.getName();
						
						try {
							new AccessFile(importedFilePath, importedFileName, optionChoice, algorithmChoice);
						} catch (IOException e1) {
							interForDialogs.exceptionIO();
						}
				} else {
					interForDialogs.selectionCancelled();
					Output.setFileOutput("User has cancelled file selection.");
				}
			}
		});
		
		menuBarSubMenu2 = new JMenu("<html><u>H</u>elp</html>");
		menuBarSubMenu2.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuBarSubMenu2);

		menuBarSubMenu2Item1 = new JMenuItem("<html><u>A</u>bout</html>");
		menuBarSubMenu2.add(menuBarSubMenu2Item1);
		menuBarSubMenu2Item1.setToolTipText("Press this button to find out more about this application.");
		menuBarSubMenu2Item1.setMnemonic(KeyEvent.VK_A);
		menuBarSubMenu2Item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interForDialogs.aboutProgramDialog();
			}
		});

		return menuBar;
	}
}