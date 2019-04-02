import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Manages the display of the Solitaire game.
 * 
 * @author  Julia Biswas
 *          Anu Datar
 * @version October 30, 2018
 */
public class SolitaireDisplay extends JComponent implements MouseListener
{
    private static final int CARD_WIDTH = 73;
    private static final int CARD_HEIGHT = 97;
    private static final int SPACING = 5;  //distance between cards
    private static final int FACE_UP_OFFSET = 15;  //distance for cascading face-up cards
    private static final int FACE_DOWN_OFFSET = 5;  //distance for cascading face-down cards

    private JFrame frame;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Solitaire game;

    /**
     * Creates instances of the SolitaireDisplay class.
     * 
     * @param   game        the Solitaire game that the 
     *                      object displays
     */
    public SolitaireDisplay(Solitaire game)
    {
        this.game = game;

        frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);

        this.setPreferredSize(new Dimension(CARD_WIDTH * 7 + SPACING * 8, 
            CARD_HEIGHT * 2 + SPACING * 3 + FACE_DOWN_OFFSET * 7 + 13 * FACE_UP_OFFSET));
        this.addMouseListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Manages the paint component (graphics)
     * of the display.
     * 
     * @param   g       the graphics of the game
     */
    public void paintComponent(Graphics g)
    {
        //background
        g.setColor(new Color(0, 153, 153));
        g.fillRect(0, 0, getWidth(), getHeight());

        //face down
        drawCard(g, game.getStockCard(), SPACING, SPACING);

        //stock
        drawCard(g, game.getWasteCard(), SPACING * 2 + CARD_WIDTH, SPACING);
        if (selectedRow == 0 && selectedCol == 1)
            drawBorder(g, SPACING * 2 + CARD_WIDTH, SPACING);
            
        //foundation
        if (selectedRow == 0 && selectedCol >= 3)
            drawBorder(g, SPACING * (selectedCol+1) + (CARD_WIDTH*selectedCol), SPACING);

        //aces
        for (int i = 0; i < 4; i++)
            drawCard(g, game.getFoundationCard(i), SPACING * (4 + i) + CARD_WIDTH * (3 + i), 
                SPACING);

        //piles
        for (int i = 0; i < 7; i++)
        {
            Stack<Card> pile = game.getPile(i);
            int offset = 0;
            for (int j = 0; j < pile.size(); j++)
            {
                drawCard(g, pile.get(j), SPACING + (CARD_WIDTH + SPACING) * i, 
                    CARD_HEIGHT + 2 * SPACING + offset);
                if (selectedRow == 1 && selectedCol == i && j == pile.size() - 1)
                    drawBorder(g, SPACING + (CARD_WIDTH + SPACING) * i, 
                        CARD_HEIGHT + 2 * SPACING + offset);

                if (pile.get(j).isFaceUp())
                    offset += FACE_UP_OFFSET;
                else
                    offset += FACE_DOWN_OFFSET;
            }
        }
    }

    /**
     * Draws each of the playing cards.
     * 
     * @param   g       the graphics of the card
     * @param   card    the card that the graphics are for  
     * @param   x       the value of the card's x (horizontal) coordinate
     * @param   y       the value of the card's y (vertical) coordinate
     */
    private void drawCard(Graphics g, Card card, int x, int y)
    {
        if (card == null)
        {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        }
        else
        {
            String fileName = card.getFileName();
            if (!new File(fileName).exists())
                throw new IllegalArgumentException("bad file name:  " + fileName);
            Image image = new ImageIcon(fileName).getImage();
            g.drawImage(image, x, y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /**
     * Invoked when the mouse exits a component.
     * 
     * @param e      the event where the mouse exited a component
     */
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * Invoked when the mouse enters a component.
     * 
     * @param e      the event where the mouse entered a component
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * 
     * @param e      the event where the mouse was released 
     *               on a component
     */
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     * 
     * @param e      the event where the mouse has been pressed 
     *               on a component
     */
    public void mousePressed(MouseEvent e)
    {
    }

    /**
     * Invoked when the mouse button has been clicked on a component.
     * 
     * @param e      the event where the mouse has been clicked 
     *               on a component
     */
    public void mouseClicked(MouseEvent e)
    {
        //none selected previously
        int col = e.getX() / (SPACING + CARD_WIDTH);
        int row = e.getY() / (SPACING + CARD_HEIGHT);
        if (row > 1)
            row = 1;
        if (col > 6)
            col = 6;

        if (row == 0 && col == 0)
            game.stockClicked();
        else if (row == 0 && col == 1)
            game.wasteClicked();
        else if (row == 0 && col >= 3)
            game.foundationClicked(col - 3);
        else if (row == 1)
            game.pileClicked(col);
        repaint();
    }

    /**
     * Draws the yellow border around the selected card.
     */
    private void drawBorder(Graphics g, int x, int y)
    {
        g.setColor(Color.YELLOW);
        g.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);
        g.drawRect(x + 1, y + 1, CARD_WIDTH - 2, CARD_HEIGHT - 2);
        g.drawRect(x + 2, y + 2, CARD_WIDTH - 4, CARD_HEIGHT - 4);
    }

    /**
     * Unselects the selected card.
     */
    public void unselect()
    {
        selectedRow = -1;
        selectedCol = -1;
    }

    /**
     * Determines whether the waste is selected
     * or not.
     * 
     * @return      true if the waste is selected;
     *              otherwise,
     *              false
     */
    public boolean isWasteSelected()
    {
        return selectedRow == 0 && selectedCol == 1;
    }

    /**
     * Selects the waste.
     */
    public void selectWaste()
    {
        selectedRow = 0;
        selectedCol = 1;
    }

    /**
     * Determines whether a pile is selected
     * or not.
     * 
     * @return      true if a pile is selected;
     *              otherwise,
     *              false
     */
    public boolean isPileSelected()
    {
        return selectedRow == 1;
    }

    /**
     * Determines whether a foundation is selected
     * or not.
     * 
     * @return      true if a foundation is selected;
     *              otherwise,
     *              false
     */
    public boolean isFoundationSelected()
    {
        return selectedRow == 0 && selectedCol >= 3;
    }

    /**
     * Retrieves the index of the selected
     * pile.
     * 
     * @return      the index of the selected
     *              pile
     */
    public int selectedPile()
    {
        if (selectedRow == 1)
            return selectedCol;
        else
            return -1;
    }

    /**
     * Retrieves the index of the selected
     * foundation.
     * 
     * @return      the index of the selected
     *              foundation
     */
    public int selectedFoundation()
    {
        if (selectedRow == 0 && selectedCol >= 3)
        {
            return selectedCol-3;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Selects the foundation at the 
     * given index.
     * 
     * @param   index       the index of the foundation
     */
    public void selectFoundation(int index)
    {
        selectedRow = 0;
        selectedCol = index + 3;
    }

    /**
     * Selects the pile at the 
     * given index.
     * 
     * @param   index       the index of the pile
     */
    public void selectPile(int index)
    {
        selectedRow = 1;
        selectedCol = index;
    }
}