import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Dialogs implements PublicInterfaces.DialogInterface {

	public void aboutProgramDialog() {
		JOptionPane.showMessageDialog(null,
				"This program allows a user to find out whether or not file-system contents have been potentially tampered with or not.",
				"ABOUT APPLICATION.", JOptionPane.INFORMATION_MESSAGE);
	}

	public void fileSystemError() {
		JOptionPane.showMessageDialog(null,
				"The path you are trying to access cannot be accessed due to a file in the directory being in use by the filesystem at this current time.",
				"ERROR ACCESSING PATH", JOptionPane.ERROR_MESSAGE);
	}
	
	public void exceptionIO() {
		JOptionPane.showMessageDialog(null,
				"An error occured trying to read from this directory",
				"A IOException HAS OCCURED", JOptionPane.ERROR_MESSAGE);
	}

	public  void selectionCancelled() {
		JOptionPane.showMessageDialog(null,
				"Selection has been cancelled by the user.",
				"File / Directory selection cancelled.", JOptionPane.WARNING_MESSAGE);
	}

	public void FilePossiblyTampered(Set<String> string, Set<String> name) {
		UIManager.put("OptionPane.minimumSize",new Dimension(300,100)); 
			for(String s : string) {
				for(String t : name ) {
					JOptionPane.showMessageDialog(null, s,  t + " HAS BEEN TAMPERED", JOptionPane.WARNING_MESSAGE);
			    }
			}
		}


	public void fileNotFound(String filename) {
		JOptionPane.showMessageDialog(null,
				"File " + filename + " was not found.",
				null, JOptionPane.WARNING_MESSAGE);
	}
}