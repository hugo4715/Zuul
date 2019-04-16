import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

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

    /**
     * Construct a UserInterface. As a parameter, a Game Engine
     * (an object processing and executing the game commands) is
     * needed.
     *
     * @param gameEngine The GameEngine object implementing the game logic.
     */
    public UserInterface(final GameEngine gameEngine) {
        this.engine = gameEngine;
        this.createGUI();
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
        this.entryField.setEditable(enabled);
        if (!enabled)
            this.entryField.getCaret().setBlinkRate(0);
    }

    /**
     * Set up graphical user interface.
     */
    private void createGUI() {
        Font font = new Font("Comic Sans MS", 0,20);

        this.frame = new JFrame("Lost in space");
        this.frame.setPreferredSize(new Dimension(1000,800));
        this.entryField = new JTextField(34);
        this.entryField.setFont(font);

        this.log = new JTextArea();
        this.log.setEditable(false);
        this.log.setFont(font);
        this.log.setLineWrap(true);
        JScrollPane vListScroller = new JScrollPane(this.log);
        vListScroller.setPreferredSize(new Dimension(200, 200));
        vListScroller.setMinimumSize(new Dimension(100, 100));

        JPanel vPanel = new JPanel();
        this.image = new JLabel();

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

        vPanel.setLayout(new BorderLayout());
        vPanel.add(this.image, BorderLayout.NORTH);
        vPanel.add(vListScroller, BorderLayout.CENTER);
        vPanel.add(this.entryField, BorderLayout.SOUTH);
        vPanel.add(buttonPanel, BorderLayout.EAST);

        this.frame.getContentPane().add(vPanel, BorderLayout.CENTER);

        // add some event listeners to some components
        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                System.exit(0);
            }
        });

        this.buttonLook.addActionListener(this);
        this.entryField.addActionListener(this);
        this.buttonQuit.addActionListener(this);
        this.buttonEat.addActionListener(this);

        this.frame.pack();
        this.frame.setVisible(true);
        this.entryField.requestFocus();
    }

    /**
     * Actionlistener interface for entry textfield.
     */
    public void actionPerformed(final ActionEvent event) {

        if (event.getSource() == this.buttonQuit) {
            engine.interpretCommand("quit");
        } else if (event.getSource() == this.entryField) {
            this.processCommand();
            this.entryField.requestFocus();
        }else if(event.getSource() == this.buttonLook){
            engine.interpretCommand("look");
        }else if(event.getSource() == this.buttonEat){
            engine.interpretCommand("eat");
        }
    }

    /**
     * A command has been entered. Read the command and do whatever is
     * necessary to process it.
     */
    private void processCommand() {
        String vInput = this.entryField.getText();
        this.entryField.setText("");

        this.engine.interpretCommand(vInput);
    }
}
