import java.awt.*;
import javax.swing.*;

/**
 * Class which extends JPanel. This is the Hashing panel, explained below.
 */
@SuppressWarnings("serial")
public class HashPanel extends JPanel {
	
	private ButtonGroup algorithmGroup;
	private static JRadioButton addMultiHash, shiftXORHash, oatHash;
	
	/**
	 * This is responsible for setting up the HashJPanel.
	 * -- which is the JPanel which allows the user to choose which hashing algorithm to use.
	 * @param myTitle
	 */
	public HashPanel(String myTitle){
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(230,120));

		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(myTitle),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		
		algorithmGroup = new ButtonGroup();
		addMultiHash = new JRadioButton("AddMultiHash Algorithm (default)");
		addMultiHash.setToolTipText("Algorithm based on the addition and multiplication of some numbers.");
		algorithmGroup.add(addMultiHash);
		addMultiHash.setSelected(true);
		add(addMultiHash);
		
		shiftXORHash = new JRadioButton("ShiftXORHash Algorithm 2");
		shiftXORHash.setToolTipText("Algorithm based on the XOR'ing and shifting of bits/bytes.");
		algorithmGroup.add(shiftXORHash);
		add(shiftXORHash);
		
		oatHash = new JRadioButton("OATHash Algorithm 3");
		oatHash.setToolTipText("Algorithm based on the shifting, addition and multiplication of bits.");
		algorithmGroup.add(oatHash);
		add(oatHash);
	}
	
	/**
	 * Allows access to the state of the hashing algorithm buttons.
	 */
	public static int getSelectedButton() {
		return addMultiHash.isSelected() ? 1 : shiftXORHash.isSelected() ? 2 :  oatHash.isSelected()  ? 3 : 0;
	}

}