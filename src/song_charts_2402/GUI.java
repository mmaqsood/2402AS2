package song_charts_2402;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class GUI extends JFrame implements ActionListener{
		
	private SongList songList; //collection of songs displayed in GUI list
	private SongList masterSongList; //collection of all the songs imported
	
	private Song    selectedSong; //song currently selected in the GUI list
	
    private Timer timer; //can used for animation 

	private	JMenuBar		aMenuBar = new JMenuBar();
	private	JMenu			fileMenu = new JMenu("File");
	private	JMenu			playMenu = new JMenu("Play");
	private	JMenu			tempoMenu = new JMenu("Tempo");
    //FILE MENU ITEMS
	private JMenuItem		openFileItem = new JMenuItem("Open XML File");    
	private JMenuItem		exportXMLItem = new JMenuItem("Export XML");    
	private JMenuItem		newSongItem = new JMenuItem("New Blank Song");    
	
	private JMenuItem		playItem = new JMenuItem("Play");    
	private JMenuItem		pauseItem = new JMenuItem("Pause");    
	private JMenuItem		stopItem = new JMenuItem("Stop");    
	
	private JMenuItem		t66Item = new JMenuItem("66 bpm");    
	private JMenuItem		t88Item = new JMenuItem("88 bpm");    
	private JMenuItem		t120Item = new JMenuItem("120 bpm");    
	private JMenuItem		t240Item = new JMenuItem("240 bpm");    


	

	// Store the view that contains the components
	ListPanel 		view; //panel of GUI components for the main window
	ChartView chartView; //panel to view PDF charts
	
	GUI thisFrame;

	// Here are the component listeners
	ActionListener			theSearchButtonListener;
	ActionListener			timerListener;
	ListSelectionListener	songListSelectionListener;
	KeyListener             keyListener;

	// Here is the default constructor
	public GUI(String title) {
		super(title);

        songList = new SongList();
        masterSongList = new SongList();
        
		int millisecondsBetweenEvents = 1000; 
		timer = new Timer(millisecondsBetweenEvents, this); 
		timer.setDelay(millisecondsBetweenEvents);

        
        //add some sample songs for now
        //songList.add(new Song("All The Things You Are"));
        //songList.add(new Song("The Girl From Ipanema"));
        //songList.add(new Song("My One And Only Love"));
        //songList.add(new Song("Footprints"));
      
        
 		selectedSong = null;
		thisFrame = this;
		
		setJMenuBar(aMenuBar);
		//FILE MENU
		aMenuBar.add(fileMenu);
		fileMenu.add(openFileItem);
		fileMenu.add(exportXMLItem);
		fileMenu.add(newSongItem);
		
		openFileItem.addActionListener(this);
		exportXMLItem.addActionListener(this);
		newSongItem.addActionListener(this);

		//PLAY MENU
		aMenuBar.add(playMenu);
		playMenu.add(playItem);
		playMenu.add(pauseItem);
		playMenu.add(stopItem);

		playItem.addActionListener(this);
		pauseItem.addActionListener(this);
		stopItem.addActionListener(this);
		
		//PLAY MENU
		aMenuBar.add(tempoMenu);
		tempoMenu.add(t66Item);
		tempoMenu.add(t88Item);
		tempoMenu.add(t120Item);
		tempoMenu.add(t240Item);

		t66Item.addActionListener(this);
		t88Item.addActionListener(this);
		t120Item.addActionListener(this);
		t240Item.addActionListener(this);


		addWindowListener(
				new WindowAdapter() {
	 				public void windowClosing(WindowEvent e) {
	 					if(timer != null) timer.stop(); //stop animation
						System.exit(0);
					}
				}
			);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints layoutConstraints = new GridBagConstraints();
		setLayout(layout);


		// Make the main window view panel
		view = new ListPanel(songList);
		layoutConstraints.gridx = 0;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
		layoutConstraints.weightx = 1.0;
		layoutConstraints.weighty = 1.0;
		layout.setConstraints(view, layoutConstraints);
		add(view);
		
		// Make the main window view panel
		chartView = new ChartView(timer);
		layoutConstraints.gridx = 1;
		layoutConstraints.gridy = 0;
		layoutConstraints.gridwidth = 1;
		layoutConstraints.gridheight = 1;
		layoutConstraints.fill = GridBagConstraints.BOTH;
		layoutConstraints.insets = new Insets(10, 10, 10, 10);
		layoutConstraints.anchor = GridBagConstraints.NORTHWEST;
		layoutConstraints.weightx = 5.0;
		layoutConstraints.weighty = 1.0;
		layout.setConstraints(chartView, layoutConstraints);
		add(chartView);


			
		theSearchButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					search();
				}};



		// Add a listener to allow selection of buddies from the list
		songListSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				selectSong();
			}};


			
		keyListener = new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) {
						
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {

					int keyChar = arg0.getKeyChar();

			        if (keyChar == KeyEvent.VK_ENTER)  search();
				
				}};


        setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900,700);
		
		
		//timer.start(); //start the timer


		// Start off with everything updated properly to reflect the model state
		update();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == timer){
		    tick();
		}
		
		else if(e.getSource() == openFileItem){
			getSongDataFromFile();		    
		}
		else if(e.getSource() == exportXMLItem){
			exportXMLSongDataToFile();		    
		}
		else if(e.getSource() == newSongItem){
			int numberOfEmptyBars = 12;
			Song newSong = new Song(numberOfEmptyBars);
			masterSongList.add(newSong);
			songList.add(newSong);
			view.setSongListData(songList);
			
		}
		else if(e.getSource() == playItem){
			chartView.startPlayback();		    
		}
		else if(e.getSource() == pauseItem){
			chartView.pausePlayback();		    
		}
		else if(e.getSource() == stopItem){
			chartView.stopPlayback();		    
		}
		//metronome tempo settings
		else if(e.getSource() == t66Item){
			chartView.setTempo(66);		    
		}
		else if(e.getSource() == t88Item){
			chartView.setTempo(88);		    
		}
		else if(e.getSource() == t120Item){
			chartView.setTempo(120);		    
		}
		else if(e.getSource() == t240Item){
			chartView.setTempo(240);		    
		}
		
		update();

	}

	
	private void tick(){
		//Handle a timer event
		chartView.advanceCurrentBar();
		//System.out.println("TICK");
		update();
	}
	
	private void getSongDataFromFile(){
		
	    System.out.println("OPEN FILE");
	    File dataFile = getInputFile();
	    SongList theSongs = SongList.parseFromFile(dataFile);
	    if(theSongs != null){
	   
	      songList = theSongs;
	      
	      masterSongList = new SongList();
	      masterSongList.addAll(songList);
	      view.setSongListData(songList);
	      selectedSong = null;
	    }
	    update();
	    
		
	}
	
	private  File getInputFile(){
		
		File dataFile =null;
		
		//Open file dialog to find the data file
   	    String currentDirectoryProperty = System.getProperty("user.dir");
   	    //System.out.println("ChartMaker::openFile: currentDirectoryProperty is: " + currentDirectoryProperty);
   	    
        JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty);         
        
        chooser.setCurrentDirectory(currentDirectory);
                
        int returnVal = chooser.showOpenDialog(this);
         
        if (returnVal == JFileChooser.APPROVE_OPTION) { 
    
        	dataFile = chooser.getSelectedFile();
        }
        return dataFile;
	}
	
	private  void exportXMLSongDataToFile(){
		
  	    String currentDirectoryProperty = System.getProperty("user.dir");
	    JFileChooser chooser = new  JFileChooser();
        File currentDirectory = new File(currentDirectoryProperty); 

        chooser.setCurrentDirectory(currentDirectory);
        
        int returnVal = chooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) { 
            
        	File file = chooser.getSelectedFile();
        	
            try{ 
                PrintWriter    outputFile = new PrintWriter(new FileWriter(file));

                String indent = "";
                        	
                String XMLDocTypeHeader = "<?xml version = \"1.0\"?>";
                String fakeBookXMLStartTag = "<fakebookXML>";
                String fakeBookXMLEndTag = "</fakebookXML>";

               	outputFile.println(XMLDocTypeHeader);
               	outputFile.println(indent + fakeBookXMLStartTag);

               	songList.exportXMLToFile(indent+"  ", outputFile);

            	outputFile.println(indent + fakeBookXMLEndTag);

                outputFile.close();
                
                } catch (FileNotFoundException e) { 
                    System.out.println("Error: Cannot open file" + file.getName() + " for writing.");
                    
                } catch (IOException e) { 
                    System.out.println("Error: Cannot write to file: " + file.getName());
                    
                }
            }

	}

	// Enable all listeners
	private void enableListeners() {
		view.getSearchButton().addActionListener(theSearchButtonListener);
		view.getSongJList().addListSelectionListener(songListSelectionListener);
		view.getSearchText().addKeyListener(keyListener);
	}

	// Disable all listeners
	private void disableListeners() {
		view.getSearchButton().removeActionListener(theSearchButtonListener);
		view.getSongJList().removeListSelectionListener(songListSelectionListener);
		view.getSearchText().removeKeyListener(keyListener);
	}


	// This is called when the user clicks the add button
	private void search() {
		
		String searchPrototype = view.getSearchText().getText().trim();
      	//HOOK FOR SEARCH, NOT IMPLEMENTED YET	
		
		String [] wordsToSearchFor = searchPrototype.split(" ");
		
		songList.clear(); //empty the songlist
		selectedSong = null; //deselect any selected songs.
		
		for(Song s : masterSongList.getSongs()){
			boolean keep = true;
			for(String word: wordsToSearchFor){
				if(word.trim().length()>0)
				   if(!s.getTitle().toLowerCase().contains(word.trim().toLowerCase())) keep = false;
			}
			if(keep) songList.add(s);
			
		}
		
		view.setSongListData(songList); //make the list panel  use the new songList contents

		System.out.println("Search clicked");
		update(); //update  GUI
	}



	// This is called when the user selects a song from the list
	private void selectSong() {
		
		//select songs or toggle it off
		selectedSong = (Song)(view.getSongJList().getSelectedValue());
		
		System.out.println("Song Selected: " + selectedSong);				
		
		chartView.showSong(selectedSong);
	
		update();
	}


	// Update the remove button
	private void updateSearchButton() {
		view.getSearchButton().setEnabled(true);
	}



	// Update the list
	private void updateList() {        
	      

		if (selectedSong != null)
			view.getSongJList().setSelectedValue(selectedSong, true);
	}
	// Update chart view
	private void updateChartView() {
        
		chartView.update();
	}


	// Update the components
	private void update() {
		disableListeners();
		updateList();
		updateChartView();
		updateSearchButton();
		enableListeners();
	}


}