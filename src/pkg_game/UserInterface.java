package pkg_game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class implements a simple graphical user interface with a text entry
 * area, a text output area and an optional image.
 */
public class UserInterface implements ActionListener {
    private GameEngine engine;

    private JFrame frame;
    private JTextField entryField;
    private JTextArea log;
    private JLabel image;

    private JButton buttonLook;
    private JButton buttonEat;
    private JButton buttonQuit;

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuSave;
    private JMenuItem menuLoad;

    private boolean inputEnabled;

    /**
     * Construct a pkg_game.UserInterface. As a parameter, a pkg_game.Game Engine
     * (an object processing and executing the game commands) is
     * needed.
     *
     * @param gameEngine The pkg_game.GameEngine object implementing the game logic.
     */
    public UserInterface(final GameEngine gameEngine) {
        this.engine = gameEngine;
        this.inputEnabled = true;
        this.createGUI();
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    /**
     * Print out some text into the text area.
     */
    public void print(final String text) {
        this.log.append(text);
        this.log.setCaretPosition(this.log.getDocument().getLength());
    }

    /**
     * Print out some text into the text area, followed by a line break.
     */
    public void println(final String text) {
        this.print(text + "\n");
    } // println(.)

    /**
     * Show an image file in the interface.
     */
    public void showImage(final String imageName) {
        URL vImageURL = this.getClass().getClassLoader().getResource(imageName);
        if (vImageURL == null)
            System.out.println("image not found");
        else {
            ImageIcon vIcon = new ImageIcon(vImageURL);
            this.image.setIcon(vIcon);
            this.frame.pack();
        }
    }

    /**
     * Enable or disable input in the input field.
     */
    public void setInputEnabled(final boolean enabled) {
        this.inputEnabled = enabled;
        this.entryField.setEditable(enabled);
        if (!enabled)
            this.entryField.getCaret().setBlinkRate(0);
    }

    /**
     * Set up graphical user interface.
     */
    private void createGUI() {
        Font font = new Font("Comic Sans MS", Font.PLAIN,20);

        this.frame = new JFrame("Lost in space");
        this.frame.setPreferredSize(new Dimension(1000,800));

        /*
            MENU BAR
         */
        this.menuBar = new JMenuBar();
        this.menuFile = new JMenu("Game");
        this.menuFile.setFont(font);
        this.menuBar.add(this.menuFile);

        this.menuSave = new JMenuItem("Save");
        this.menuSave.setFont(font);
        this.menuLoad = new JMenuItem("Load");
        this.menuLoad.setFont(font);

        this.menuFile.add(menuSave);
        this.menuFile.add(menuLoad);

        this.frame.setJMenuBar(this.menuBar);
        /*
            ENTRY
         */
        this.entryField = new JTextField(34);
        this.entryField.setFont(font);

        /*
            LOG
         */
        this.log = new JTextArea();
        this.log.setEditable(false);
        this.log.setFont(font);
        this.log.setLineWrap(true);
        JScrollPane vListScroller = new JScrollPane(this.log);
        vListScroller.setPreferredSize(new Dimension(200, 200));
        vListScroller.setMinimumSize(new Dimension(100, 100));

        /*
            IMAGE
         */
        this.image = new JLabel();


        /*
            BUTTONS
         */
        this.buttonLook = new JButton("Look");
        this.buttonEat = new JButton("Eat");
        this.buttonQuit = new JButton("Quit");

        this.buttonLook.setFont(font);
        this.buttonEat.setFont(font);
        this.buttonQuit.setFont(font);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(this.buttonLook);
        buttonPanel.add(this.buttonEat);
        buttonPanel.add(this.buttonQuit);

        /*
            PANEL
         */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(this.image, BorderLayout.NORTH);
        mainPanel.add(vListScroller, BorderLayout.CENTER);
        mainPanel.add(this.entryField, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        this.frame.getContentPane().add(mainPanel, BorderLayout.CENTER);


        /*
            LISTENERS
         */
        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                System.exit(0);
            }
        });

        this.buttonLook.addActionListener(this);
        this.entryField.addActionListener(this);
        this.buttonQuit.addActionListener(this);
        this.buttonEat.addActionListener(this);
        this.menuLoad.addActionListener(this);
        this.menuSave.addActionListener(this);

        /*
            SHOW
         */
        this.frame.pack();
        this.frame.setVisible(true);
        this.entryField.requestFocus();
    }

    /**
     * Actionlistener interface for entry textfield.
     */
    public void actionPerformed(final ActionEvent event) {
        if(!inputEnabled)return;

        if (event.getSource() == this.buttonQuit) {
            engine.interpretCommand("quit");
        } else if (event.getSource() == this.entryField) {
            this.processCommand();
            this.entryField.requestFocus();
        }else if(event.getSource() == this.buttonLook){
            engine.interpretCommand("look");
        }else if(event.getSource() == this.buttonEat){
            engine.interpretCommand("eat");
        }else if(event.getSource().equals(this.menuLoad)){
            loadGame();
        }else if(event.getSource().equals(this.menuSave)){
            String name = JOptionPane.showInputDialog("What save do you want to Save:");
            this.engine.interpretCommand("save " + name);
        }
    }

    private void loadGame() {

        String[] saves = Arrays.stream(Game.SAVE_FOLDER.list((file,filename) -> filename.endsWith(".save")))
                .filter(filename -> filename != null && !filename.isEmpty())
                .map(filename -> filename.substring(0,filename.lastIndexOf('.')))
                .toArray(String[]::new);

        String selectedSave = (String) JOptionPane.showInputDialog(null, "Select you save:", "Load", JOptionPane.DEFAULT_OPTION, null, saves, "0");

        if ( selectedSave != null ){
            this.engine.interpretCommand("load " + selectedSave);
        }
    }

    /**
     * A command has been entered. Read the command and do whatever is
     * necessary to process it.
     */
    private void processCommand() {
        String vInput = this.entryField.getText();
        clearText();

        this.engine.interpretCommand(vInput);
    }

    public void clearText() {
        this.log.setText("");
    }
}
