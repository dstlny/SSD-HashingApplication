import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Output extends JPanel {

	private static String displayPanelTitle;
	private static JTextArea FileOutput;
	
	public Output(String myTitle) {
	
		setBackground(Color.WHITE);
		setSize(getPreferredSize());
			
		displayPanelTitle = myTitle;
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(displayPanelTitle),
				BorderFactory.createEmptyBorder(1 / 4, 1 / 4, 1 / 4, 1 / 4)));
		
		FileOutput = new JTextArea(14, 68);
		JScrollPane scroll = new JScrollPane(FileOutput);
		FileOutput.setEditable(false);
		add(scroll);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		FileOutput.setFont(new Font("monospaced", Font.BOLD,12));
		FileOutput.setBackground(Color.BLACK);
		FileOutput.setForeground(Color.WHITE);
		FileOutput.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0,0,0,0),
				BorderFactory.createEmptyBorder(10,10,10,10)));
     }

	public static void setFileOutput(String fileName, long hash) {
		FileOutput.setForeground(Color.WHITE);
		FileOutput.setText("Filename: "+fileName+"\n[#] Hash: "+String.format("%016X",hash)+"\n");
	}
	
	public static void setFileOutput(String directoryName, ArrayList<String> fileNames, long hash) throws IOException {
		FileOutput.setForeground(Color.WHITE);
		FileOutput.setText("Dir '" + directoryName + "'\n" +"[-] Filename\n");
		FileOutput.append("=======================================================\n");
		for(int i = 0; i < fileNames.size(); i++) {	
			if(fileNames.get(i) != null) {
				FileOutput.append("["+i+"] '"+fileNames.get(i)+"'\n");
			}
		}
		FileOutput.append("=======================================================\n");
		FileOutput.append("[#] Hash: "+String.format("%016X",hash));
	}
	
	public static void setFileOutputBasedOnMetaData(String directoryName, ArrayList<String> fileNames, ArrayList<Date> dates, long hash) {
		FileOutput.setForeground(Color.WHITE);
		
		FileOutput.setText("Dir '" + directoryName + "' (meta-data)\n" +"[-] Filename - (Date Last Modified)\n");
		FileOutput.append("=======================================================\n");
		for(int i = 0; i < fileNames.size(); i++) {	
			if(fileNames.get(i) != null) {
				FileOutput.append("["+i+"] '"+fileNames.get(i)+"' - ("+dates.get(i)+")\n");
			}
		}
		FileOutput.append("=======================================================\n");
		FileOutput.append("[#] Hash: "+String.format("%016X",hash));
	}
	
	public static void setFileOutput(String error) {
		FileOutput.setForeground(Color.RED);
		FileOutput.append(error);
	}
}
	