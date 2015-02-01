package song_charts_2402;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.Timer;

import song_charts_2402.Bar.BarLine;
import assignment2_2402.ArrayStack;
import assignment2_2402.BoundedCapacityOverFlowException;
import assignment2_2402.StackUnderflowException;

public class ChartView extends JPanel implements MouseListener, MouseMotionListener{
 
	//Represents the area where song data is displayed
	//Also contains listeners for mouse events in that area
	
    final public static BasicStroke stroke = new BasicStroke(3.0f);
    //title and bar number font
    public final static int titlePointSize = 30;
    public final static Font defaultTitleFont = new Font("Times New Roman", Font.BOLD, titlePointSize);
    
    public final static int chartPointSize = 20;
    public final static Font defaultChartFont = new Font("Times New Roman", Font.BOLD, chartPointSize);

	private Song songToView = null; //song being displayed
	private Bar currentBar = null; //bar being played
	private String currentTimeSignature = null;  //time signature currently in effect
	private Timer timer; //provides access to timer for moving playback highlight box
	
	private Bar barBeingEdited = null;  //bar being edited by moving text field
	private Bar popUpBar = null; //bar being edited with popup menu
	
	//highlight box so show over bar being played
	private int boxX = 10, boxY = 10, boxWidth = 100, boxHeight = 50;
	
	private ChartView thisChartView; //just to keep track of the "this" variable
    
	//textfield used to edit bar chords etc.
	private JTextField movingTextField = new JTextField("Hello World");
	
	//popup menu for Bars to edit properties of bars
	private JPopupMenu barPopupMenu = new JPopupMenu(); //context menu for Bars
	
	//sub menues
	private JMenu leftBarLinePopupMenu = new JMenu("Left Bar Lines"); 
	private JMenu rightBarLinePopupMenu = new JMenu("Right Bar Lines"); 
	private JMenu signCodaPopupMenu = new JMenu("Sign, Coda, DS, DC"); 
	private JMenu endingsPopupMenu = new JMenu("Endings 1.), 2.) 3.)"); 
	private JMenu rehearsalLetterPopupMenu = new JMenu("[A],[B],[intro] ..."); 
	private JMenu insertOrDeleteBarsMenu = new JMenu("Insert/Delete Bars"); 
	
	//popup menu items
	private JMenuItem leftNormalBarLineMenuItem = new JMenuItem("Left Normal Bar Line:    |");
	private JMenuItem leftDoubleBarLineMenuItem = new JMenuItem("Left Double Bar Line:    ||");
	private JMenuItem leftRepeatBarLineMenuItem = new JMenuItem("Left Repeat Bar Line:    |:");
	private JMenuItem rightNormalBarLineMenuItem = new JMenuItem("Right Normal Bar Line:    |");
	private JMenuItem rightDoubleBarLineMenuItem = new JMenuItem("Right Double Bar Line:    ||");
	private JMenuItem rightRepeatBarLineMenuItem = new JMenuItem("Right Repeat Bar Line:    :|");
	private JMenuItem finalBarLineMenuItem = new JMenuItem("Right Repeat Bar Line:    |]");

	private JMenuItem signMenuItem = new JMenuItem("Sign $");
	private JMenuItem codaMenuItem = new JMenuItem("Coda @");
	private JMenuItem dsAlCapoMenuItem = new JMenuItem("D.S. al Coda");
	private JMenuItem dcAlCodaMenuItem = new JMenuItem("D.C. al Coda");
	private JMenuItem daCapoMenuItem = new JMenuItem("D.C.");
	private JMenuItem textMenuItem = new JMenuItem("text above bar");
	private JMenuItem signsNoneMenuItem = new JMenuItem("NONE");

	private JMenuItem letterAMenuItem = new JMenuItem("[A]");
	private JMenuItem letterBMenuItem = new JMenuItem("[B]");
	private JMenuItem letterCMenuItem = new JMenuItem("[C]");
	private JMenuItem letterIntroMenuItem = new JMenuItem("intro");
	private JMenuItem letterNoneMenuItem = new JMenuItem("NONE");

	private JMenuItem FirstEndingMenuItem = new JMenuItem("1.)");
	private JMenuItem SecondEndingMenuItem = new JMenuItem("2.)");
	private JMenuItem ThirdEndingMenuItem = new JMenuItem("3.)");
	private JMenuItem noneEndingMenuItem = new JMenuItem("NONE");

	private JMenuItem insertBarAfterMenuItem = new JMenuItem("Insert Bar After");
	private JMenuItem insertBarBeforeMenuItem = new JMenuItem("Insert Bar Before");
	private JMenuItem deleteBarMenuItem = new JMenuItem("Delete Bar");
	
	
	private ArrayStack<Bar> barStack = new ArrayStack();
	//This stack tells us which bars have already been repeated, so we're not stuck in the repeat loop
	private ArrayStack<Bar> repeatedBarStack = new ArrayStack();
	public void buildBarPopupMenu(){
		
		//left bar line sub menu
		leftBarLinePopupMenu.add(leftNormalBarLineMenuItem);
		leftBarLinePopupMenu.add(leftDoubleBarLineMenuItem);        
		leftBarLinePopupMenu.add(leftRepeatBarLineMenuItem);

		//right bar line sub menu
		rightBarLinePopupMenu.add(rightNormalBarLineMenuItem);
		rightBarLinePopupMenu.add(rightDoubleBarLineMenuItem);        
		rightBarLinePopupMenu.add(rightRepeatBarLineMenuItem);
		rightBarLinePopupMenu.add(finalBarLineMenuItem);
		
		//insert or delete bars
		insertOrDeleteBarsMenu.add(insertBarBeforeMenuItem);
		insertOrDeleteBarsMenu.add(insertBarAfterMenuItem);
		insertOrDeleteBarsMenu.add(deleteBarMenuItem);

		//insert or delete bars
		signCodaPopupMenu.add(signMenuItem);
		signCodaPopupMenu.add(codaMenuItem);
		signCodaPopupMenu.add(dsAlCapoMenuItem);
		signCodaPopupMenu.add(dcAlCodaMenuItem);
		signCodaPopupMenu.add(daCapoMenuItem);
		signCodaPopupMenu.add(textMenuItem);
		signCodaPopupMenu.add(new JSeparator());
		signCodaPopupMenu.add(signsNoneMenuItem);

		//rehearsal letter sub menu
		rehearsalLetterPopupMenu.add(letterAMenuItem);
		rehearsalLetterPopupMenu.add(letterBMenuItem);
		rehearsalLetterPopupMenu.add(letterCMenuItem);
		rehearsalLetterPopupMenu.add(letterIntroMenuItem);
		rehearsalLetterPopupMenu.add(new JSeparator());
		rehearsalLetterPopupMenu.add(letterNoneMenuItem);

		//rehearsal letter sub menu
		endingsPopupMenu.add(FirstEndingMenuItem);
		endingsPopupMenu.add(SecondEndingMenuItem);
		endingsPopupMenu.add(ThirdEndingMenuItem);
		endingsPopupMenu.add(new JSeparator());
		endingsPopupMenu.add(noneEndingMenuItem);

		
		//bar pop up menu
		barPopupMenu.add(leftBarLinePopupMenu);
		barPopupMenu.add(rightBarLinePopupMenu);
		barPopupMenu.add(new JSeparator());
		barPopupMenu.add(signCodaPopupMenu);
		barPopupMenu.add(endingsPopupMenu);
		barPopupMenu.add(rehearsalLetterPopupMenu);
		barPopupMenu.add(new JSeparator());
		barPopupMenu.add(insertOrDeleteBarsMenu);


        //Build Action Listener for all menu items
		ActionListener barPopUpMenuListener = 	new ActionListener() { 
		  public void actionPerformed(ActionEvent event){
		
		   			
	        	if (event.getSource() == leftNormalBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.NORMAL_BAR_LINE);
	        	else if (event.getSource() == leftDoubleBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.LEFT_DOUBLE_BAR_LINE);
	        	else if (event.getSource() == leftRepeatBarLineMenuItem)
	        		popUpBar.setLeftBarLine(Bar.BarLine.LEFT_REPEAT);
	        	else if (event.getSource() == rightNormalBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.NORMAL_BAR_LINE);
	        	else if (event.getSource() == rightDoubleBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.RIGHT_DOUBLE_BAR_LINE);
	        	else if (event.getSource() == rightRepeatBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.RIGHT_REPEAT);
	        	else if (event.getSource() == finalBarLineMenuItem)
	        		popUpBar.setRightBarLine(Bar.BarLine.FINAL_BAR_LINE);
	        	
	        	else if (event.getSource() == signMenuItem)
	        		popUpBar.setSign(true);
	        	else if (event.getSource() == codaMenuItem)
	        		popUpBar.setCoda(true);
	        	else if (event.getSource() == dsAlCapoMenuItem)
	        		popUpBar.setDalSegno("D.S. al Coda");
	        	else if (event.getSource() == dcAlCodaMenuItem)
	        		popUpBar.setDaCapo("D.C. al Coda");
	        	else if (event.getSource() == daCapoMenuItem)
	        		popUpBar.setDaCapo("D.C.");
	        	else if (event.getSource() == FirstEndingMenuItem)
	        		popUpBar.setEndings("1");
	        	else if (event.getSource() == SecondEndingMenuItem)
	        		popUpBar.setEndings("2");
	        	else if (event.getSource() == ThirdEndingMenuItem)
	        		popUpBar.setEndings("3");
	        	else if (event.getSource() == noneEndingMenuItem)
	        		popUpBar.setEndings(null);
	        	else if (event.getSource() == textMenuItem){
	        		String newText = JOptionPane.showInputDialog(thisChartView, "Text Above Bar", popUpBar.getText());
	        		if(newText != null && newText.length()>0)
	        		     popUpBar.setText(newText);
	        	}
	        	else if (event.getSource() == signsNoneMenuItem){
	        		popUpBar.setSign(false);
	        		popUpBar.setCoda(false);
	        		popUpBar.setDalSegno(null);
	        		popUpBar.setDaCapo(null);
	        		popUpBar.setEndings(null);
       		        popUpBar.setText(null);
	        	}
	        	
	        	else if (event.getSource() == letterAMenuItem)
	        		popUpBar.setRehearsalLetter("A");
	        	else if (event.getSource() == letterBMenuItem)
	        		popUpBar.setRehearsalLetter("B");
	        	else if (event.getSource() == letterCMenuItem)
	        		popUpBar.setRehearsalLetter("C");
	        	else if (event.getSource() == letterIntroMenuItem)
	        		popUpBar.setRehearsalLetter("intro");
	        	else if (event.getSource() == letterNoneMenuItem)
	        		popUpBar.setRehearsalLetter(null);

	        	
	        	else if (event.getSource() == insertBarBeforeMenuItem)
	        		songToView.insertBarBefore(popUpBar);
	        	else if (event.getSource() == insertBarAfterMenuItem)
	        		songToView.insertBarBefore(popUpBar);
	        	else if (event.getSource() == deleteBarMenuItem)
	        		songToView.deleteBar(popUpBar);



        	update(); //update GUI
           		
			} 
		}; 
        
        //add action listener to menu items
		leftNormalBarLineMenuItem.addActionListener(barPopUpMenuListener);
		leftDoubleBarLineMenuItem.addActionListener(barPopUpMenuListener);
		leftRepeatBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightNormalBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightDoubleBarLineMenuItem.addActionListener(barPopUpMenuListener);
		rightRepeatBarLineMenuItem.addActionListener(barPopUpMenuListener);
		finalBarLineMenuItem.addActionListener(barPopUpMenuListener);

		insertBarBeforeMenuItem.addActionListener(barPopUpMenuListener);
		insertBarAfterMenuItem.addActionListener(barPopUpMenuListener);
		deleteBarMenuItem.addActionListener(barPopUpMenuListener);		

		signMenuItem.addActionListener(barPopUpMenuListener);
		codaMenuItem.addActionListener(barPopUpMenuListener);
		dsAlCapoMenuItem.addActionListener(barPopUpMenuListener);
		dcAlCodaMenuItem.addActionListener(barPopUpMenuListener);
		daCapoMenuItem.addActionListener(barPopUpMenuListener);
		textMenuItem.addActionListener(barPopUpMenuListener);
		FirstEndingMenuItem.addActionListener(barPopUpMenuListener);
		SecondEndingMenuItem.addActionListener(barPopUpMenuListener);
		ThirdEndingMenuItem.addActionListener(barPopUpMenuListener);
		noneEndingMenuItem.addActionListener(barPopUpMenuListener);
		signsNoneMenuItem.addActionListener(barPopUpMenuListener);
       
		letterAMenuItem.addActionListener(barPopUpMenuListener);
		letterBMenuItem.addActionListener(barPopUpMenuListener);
		letterCMenuItem.addActionListener(barPopUpMenuListener);
		letterIntroMenuItem.addActionListener(barPopUpMenuListener);
		letterNoneMenuItem.addActionListener(barPopUpMenuListener);

	
	}

	
	public ChartView(Timer aTimer){
		
		timer = aTimer; //give this class access to the timer object to time playback
		thisChartView = this; //just to keep track of the "this" variable
		
		setSize(700, 700);
		setBackground(Color.white);
		
    	//add event listeners
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	
	    movingTextField.setLocation(50,50); //arbitrary default location
	    movingTextField.setSize(70,20); //arbitrary default size
	    movingTextField.setFont(defaultChartFont);
	    movingTextField.setBackground(new Color(186,234,255)); //light blue
	    movingTextField.setBorder(null); //no border around text field
        add(movingTextField); //add moving text edit field to this panel
	    movingTextField.setVisible(false); //hide text edit field
	    
        movingTextField.addActionListener( 
			new ActionListener() { 
				public void actionPerformed(ActionEvent event){ 
					if(barBeingEdited != null){
						barBeingEdited.parseDataString(movingTextField.getText());
						barBeingEdited = songToView.getBarAfter(barBeingEdited);
	
						//Advance text edit field to next bar

						movingTextField.setLocation(barBeingEdited.getOriginX(), barBeingEdited.getOriginY());
					    movingTextField.setText(barBeingEdited.getChords());
					    movingTextField.setSize(barBeingEdited.getWidth(), barBeingEdited.getHeight());			 
					    movingTextField.setVisible(true);
					    movingTextField.requestFocus(); //request keyboard focus

					}
					
					update(); //update GUI

				} 
			} 
		);
        
        buildBarPopupMenu();

		 		
	}
	
	public void update(){
		repaint(); //repaint the GUI
		           //this will call paintComponent()
	}
	
	public void showSong(Song aSong){ 
		
		//display the song aSong on view area
		
		if(aSong == null) return;
		
		songToView = aSong;

		if(songToView.getBars().size()>0){
		   currentBar = aSong.getBars().get(0); //get first bar
		   if(currentBar.getTimeSignature()!= null && currentBar.getTimeSignature().length() > 0)
			   currentTimeSignature = currentBar.getTimeSignature();
		   
		   //highlight box
	       boxX = currentBar.getOriginX();
	       boxY = currentBar.getOriginY();     
	       boxWidth = currentBar.getWidth();
		   boxHeight = currentBar.getHeight();

		}
		
	}
	
	private void setTimerDelay(){
	 
	    boolean wasRunning = timer.isRunning(); //remember whether timer was running
		
		timer.stop(); 
		
		String tempoString = songToView.getTempo();
		int tempo = Integer.parseInt(tempoString);		
		int barsPerMinute =  tempo / 4; //default assume 4/4 time
		if(currentTimeSignature != null && currentTimeSignature.length()>0){
			int indexOfSlash = currentTimeSignature.indexOf('/');
			int beatsPerBar = Integer.parseInt(currentTimeSignature.substring(0,indexOfSlash));
			barsPerMinute = tempo/beatsPerBar;
		}
		int millsecondsPerBar = 1000*60/barsPerMinute;
		timer.setInitialDelay(millsecondsPerBar); //delay from first start
		timer.setDelay(millsecondsPerBar); //delay between events
		if(wasRunning) timer.start();
		
	}
	
	public void startPlayback(){
		//start playback using the supplied timer
		timer.start();
		//System.out.println("start playback");
		
	}
	
	public void pausePlayback(){
		//pause playback using the supplied timer
		timer.stop();
		//System.out.println("pause playback");
		
	}
	
	public void stopPlayback(){
		//stop playback using the supplied timer
		timer.stop();
		showSong(songToView); //reset the song to show to force current bar to beginning
		update(); //update GUI since box has likely moved
		//System.out.println("stop playback");
		
	}
	
	public void setTempo(int bpm){
		System.out.println("tempo: " + bpm);
		if(songToView != null) songToView.setTempo(bpm);
		setTimerDelay(); //change timer delay since tempo has changed
	}
	
	
	public void advanceCurrentBar() {
		//called in response to time event
		//advance the current bar and the highlight box
		
		if(songToView ==null)return;
		if(currentBar == null) return;
		
		
		//TO DO only advance bar if we are playing, not
		//when paused or stopped.
		
		currentBar = songToView.getBarAfter(currentBar);
		
		try {
			// Check for a right repeat. 
			if (barStack.size() > 0 && barStack.top().getRightBarLine() == BarLine.RIGHT_REPEAT){
				// We now need to find the left repeat and go back to that line.
				Bar leftRepeatBar = findLeftPlayBarInStack();
				
				if (leftRepeatBar != null){
					currentBar = leftRepeatBar;
				}
				
			}
		} catch (StackUnderflowException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Add to our stack
		try {
			barStack.push(currentBar);
		} catch (BoundedCapacityOverFlowException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(currentBar.getTimeSignature()!= null && currentBar.getTimeSignature().length() > 0){
			currentTimeSignature = currentBar.getTimeSignature();
			setTimerDelay();
			
		}
				
		//set highlight box to cover current bar
        boxX = currentBar.getOriginX();
        boxY = currentBar.getOriginY();     
        boxWidth = currentBar.getWidth();
		boxHeight = currentBar.getHeight();
		
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		
	     movingTextField.setVisible(false); //hide editing text field
		
		if(event.getClickCount() == 2){
		   System.out.println("Mouse Double Clicked");
		   //handle double click
		   if(songToView != null){
			 barBeingEdited = songToView.getBarAtLocation(event.getX(), event.getY());
			 if(barBeingEdited != null){
				 
		   	     //movingTextField.setFont(...);
			     movingTextField.setLocation(barBeingEdited.getOriginX(), barBeingEdited.getOriginY());
			     movingTextField.setText(barBeingEdited.getChords());
			     movingTextField.setSize(barBeingEdited.getWidth(), barBeingEdited.getHeight());			 
			     movingTextField.setVisible(true);
			     movingTextField.requestFocus(); //request keyboard focus
			 }
			 
			 if(songToView.hasTitleAtLocation(event.getX(), event.getY())){
				 String newTitle = JOptionPane.showInputDialog(this, "Enter Song Title", songToView.getTitle());
				 if(newTitle != null && newTitle.length()>0) songToView.setTitle(newTitle.trim());
			 }
			 if(songToView.hasComposerAtLocation(event.getX(), event.getY())){
				 String newComposer = JOptionPane.showInputDialog(this, "Enter Composer", songToView.getComposer());
				 if(newComposer != null && newComposer.length()>0) songToView.setComposer(newComposer.trim());
			 }
			 if(songToView.hasStyleAtLocation(event.getX(), event.getY())){
				 String newStyle = JOptionPane.showInputDialog(this, "Musical Style", songToView.getMusicalStyle());
				 if(newStyle != null && newStyle.length()>0) songToView.setMusicalStyle(newStyle.trim());
			 }
			 if(songToView.hasKeyAtLocation(event.getX(), event.getY())){
				 String newKey = JOptionPane.showInputDialog(this, "Enter Song Key", songToView.getKey());
				 if(newKey != null && newKey.length()>0) songToView.setKey(newKey.trim());
			 }
			 if(songToView.hasTempoAtLocation(event.getX(), event.getY())){
				 String newTempo = JOptionPane.showInputDialog(this, "Enter Song Tempo", songToView.getTempo());
				 try{
					 Integer.parseInt(newTempo);
					 if(newTempo != null && newTempo.length()>0) songToView.setTempo(newTempo.trim());
			     }
				 catch(Exception e){				 
				 }
			 }

		   }
		   
		   
		}
		else{
			System.out.println("Mouse Clicked");
		}
		
		update();  //update GUI

	}

	//MouseListener and MouseMotionListner Event handlers
	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
        if (event.isPopupTrigger()){
        	
        	System.out.println("PopUp  Click");
        
            
            if(songToView == null) return;
 
            popUpBar = songToView.getBarAtLocation(event.getX(), event.getY());
           	System.out.println("Pop Up Bar: " + popUpBar);

            if(popUpBar == null) return;

            barPopupMenu.show(event.getComponent(), event.getX(), event.getY());
		       
      }    
		
	}

	
	private void drawInArea(Graphics aPen, int areaX, int areaY, int areaWidth, int areaHeight){
		
		//switch to Graphics2D pen so we can control stroke widths better etc.	    
		Graphics2D aPen2D = (Graphics2D) aPen; //cast Graphics to get more operations available.
        aPen2D.setStroke(stroke); 
        
        if(songToView != null){
        	
        	//Draw the song
        	int width = areaWidth - 20; //leave some width at right hand side of display panel
        	songToView.drawInArea(aPen, areaX, areaY, width, areaHeight);       	
        }
        
        //draw moving highlight box 
        
        if (true){ //for now always draw
        	
        	 Color cacheColor = aPen.getColor();
        	 //aPen.setColor(Color.red);   //opaque colour    	 
        	 aPen.setColor(new Color(1,0,0,0.3f)); //semi transparent colour   	 
          	 aPen.fillRect(boxX, boxY, boxWidth, boxHeight);
          	 //aPen.drawRect(boxX, boxY, boxWidth, boxHeight);
        	 aPen.setColor(cacheColor);      	 

        }
        


	}
	
    public void paintComponent(Graphics aPen) {
		super.paintComponent(aPen);
		
		//draw chart in entire panel area
		drawInArea(aPen, 0,0, getWidth(), getHeight());
		
    }
    
    /*
     * Helper function goes through our barStack to find a matching
     * left repeat for an occuring right repeat.
     */
    private Bar findLeftPlayBarInStack(){
    	//We move back to our left repeat bar.
		Bar leftRepeatBar = null;
		while (barStack.size() > 0){
			try {
				if (barStack.top().getLeftBarLine() == Bar.BarLine.LEFT_REPEAT){
					leftRepeatBar = barStack.pop();
				}
				else {
					barStack.pop();
				}
			} catch (StackUnderflowException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (leftRepeatBar != null){
			
			try {
				if (repeatedBarStack.size() > 0 && repeatedBarStack.top().toString().equals(leftRepeatBar.toString())){
					//Ignore this repeat
					leftRepeatBar = null;
					repeatedBarStack.pop();
				}
				else{
					//Add the current bar to the repeat list, we don't want to loop endlessly
					repeatedBarStack.push(leftRepeatBar);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return leftRepeatBar;
    }


}
