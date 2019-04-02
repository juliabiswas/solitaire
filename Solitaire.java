import java.util.*;
/**
 * Instances of the Solitaire class
 * are games that follow the rules of
 * the standard solitaire game. The only
 * exceptions are that cards are drawn
 * from the stock in sets of three and
 * when piles are moved, the entire pile is moved
 * instead of just select cards.
 * 
 * @author Julia Biswas
 * @version October 30, 2018
 */
public class Solitaire
{
    /**
     * Oversees the Solitaire class.
     * 
     * @param args  information from the command line
     */
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    /**
     * Creates instances of the Solitaire class.
     */
    public Solitaire()
    {
        foundations = new Stack[4];
        for (int i = 0; i < 4; i++)
        {
            foundations[i] = new Stack<Card>();
        }
        piles = new Stack[7];
        for (int i = 0; i < 7; i++)
        {
            piles[i] = new Stack<Card>();
        }
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        display = new SolitaireDisplay(this);
        createStock();
        deal();
    }

    /**
     * Retrieves the card on the top of the stock,
     * or null if the stock is empty.
     * 
     * @return the card on the top of the stock,
     * or null if the stock is empty
     */
    public Card getStockCard()
    {
        if (stock.isEmpty())
        {
            return null;
        }
        return stock.peek();
    }

    /**
     * Retrieves the card on top of the waste,
     * or null if the waste is empty.
     * 
     * @return the card on top of the waste,
     * or null if the waste is empty
     */
    public Card getWasteCard()
    {
        if (waste.isEmpty())
        {
            return null;
        }
        return waste.peek();
    }

    /**
     * Retrieves the card on top of the given foundation,
     * or null if the foundation is empty.
     * 
     * @param index     the given foundation
     * 
     * @precondition    0 <= index < 4
     * @postcondition   returns the card on top of the given
     *                  foundation, or null if the foundation
     *                  is empty
     *                  
     * @return  the card on top of the given foundation,
     *          or null if the foundation is empty
     */
    public Card getFoundationCard(int index)
    {
        if (foundations[index] == null ||
                foundations[index].isEmpty())
        {
            return null;
        }
        return foundations[index].peek();
    }

    /**
     * Retrieves a reference to the given pile.
     * 
     * @param index         the index of the pile
     * 
     * @precondition        0 <= index < 7
     * @postcondition       returns a reference to the given pile
     * 
     * @return a reference to the given pile
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    }

    /**
     * Tests if the stock has any cards left.
     * If it does, it transfers three cards
     * to the waste. If not, it resets the stock.
     * This method is called when the stock is 
     * clicked.
     * 
     * @postcondition   if the stock had cards left,
     *                  three cards have been transferred 
     *                  to the waste, but if not, the stock
     *                  has been reset
     */
    public void stockClicked()
    {
        if (!display.isWasteSelected() && !display.isPileSelected())
        {
            if (!stock.isEmpty())
            {
                dealThreeCards();
            }
            else
            {
                resetStock();
            }
        }
    }

    /**
     * Selects the waste if it isn't empty
     * (and neither the waste nor another
     * pile is already selected). Unselects
     * the waste if it is already selected.
     * This method is called when the waste is
     * clicked.
     * 
     * @postcondition   if the waste isn't empty
     *                  and neither the waste nor
     *                  another pile is already selected,
     *                  the waste is selected, but
     *                  if the waste is already selected,
     *                  it is unselected
     */
    public void wasteClicked()
    {
        if (!waste.isEmpty() && 
            !display.isWasteSelected() && 
            !display.isPileSelected())
        {
            display.selectWaste();
        }
        else
        {
            display.unselect();
        }
    }

    /**
     * Adds the selected card to the given
     * foundation if the operation is legal.
     * This method is called when the
     * given foundation is clicked.
     * 
     * @param index     the index of the foundation
     * 
     * @precondition    0 <= index < 4
     * @postcondtion    if possible, the selected card
     *                  has been added to the given foundation
     */
    public void foundationClicked(int index)
    {
        boolean wasteSel = display.isWasteSelected();
        boolean pileSel = display.isPileSelected();
        boolean foundationSel = display.isFoundationSelected();
        
        if (!pileSel && !foundationSel && !wasteSel && !foundations[index].isEmpty())
        {
            display.selectFoundation(index);
        }
        else if (wasteSel)
        {
            if(canAddToFoundation(waste.peek(), index))
            {
                foundations[index].push(waste.pop());
            }
            display.unselect();
        }
        else if (foundationSel && 
                    (foundations[display.selectedFoundation()].isEmpty() 
                            || display.selectedFoundation() == index))
        {
            display.unselect();
        }
        else if (wasteSel && canAddToFoundation(waste.peek(), index))
        {
            foundations[index].push(waste.pop());
        }
        else if (pileSel && canAddToFoundation(piles[display.selectedPile()].peek(), index))
        {
            foundations[index].push(piles[display.selectedPile()].pop());
        }
    }

    /**
     * If the waste is selected, moves the 
     * top card from the waste onto the top of 
     * the given pile. Then, the waste is 
     * unselected. This method is called when 
     * the given pile is clicked.
     * 
     * @param index     the index of the given pile
     * 
     * @precondition    0 <= index < 7
     * @postcondition   if the waste is selected,
     *                  the top card from the waste
     *                  has been moved onto the top of 
     *                  the given pile, and waste has 
     *                  been unselected
     */
    public void pileClicked(int index)
    {
        boolean wasteSel = display.isWasteSelected();
        boolean pileSel = display.isPileSelected();
        boolean foundationSel = display.isFoundationSelected();
        
        if (wasteSel)
        {
            if(canAddToPile(waste.peek(), index))
            {
                piles[index].push(waste.pop());
            }
            display.unselect();
        }
        else if (foundationSel)
        {
            Stack<Card> curr = foundations[display.selectedFoundation()];
            if(!curr.isEmpty() && canAddToPile(curr.peek(), index))
            {
                piles[index].push(curr.pop());
            }
            display.unselect();
        }
        else if (!pileSel && !wasteSel)
        {
            if (!piles[index].isEmpty() && piles[index].peek().isFaceUp())
            {
                display.selectPile(index);
            }
            else if (!piles[index].isEmpty())
            {
                piles[index].peek().turnUp();
            }
        }
        else if (pileSel && display.selectedPile() == index)
        {
            display.unselect();
        }
        else if (pileSel && display.selectedPile() != index)
        {
            Stack<Card> removed = removeFaceUpCards(display.selectedPile());
            if (!removed.isEmpty() && canAddToPile(removed.peek(), index))
            {
                addToPile(removed, index);
            }
            else if (!removed.isEmpty())
            {
                while (!removed.isEmpty())
                {
                    piles[display.selectedPile()].push(removed.pop());
                }
            }
            display.unselect();
        }
        else if (foundationSel)
        {
            if (!foundations[display.selectedFoundation()].isEmpty())
            {
                Stack<Card> sel = new Stack<Card>();
                sel.push(foundations[display.selectedFoundation()].peek());
                if (canAddToPile(sel.peek(), index))
                {
                    addToPile(sel, index);
                }
            }
        }   
    }

    /**
     * Creates a shuffled deck of cards 
     * that is used as the stock.
     */
    private void createStock()
    {
        ArrayList<Card> cards = new ArrayList<Card>();
        int num = 1;
        for(int i = 1; i <= 52; i++)
        {
            String type = "";
            int rem = i%4;
            if (rem == 0)
            {
                type = "h";
            }
            else if (rem == 1)
            {
                type = "d";
            }
            else if (rem == 2)
            {
                type = "s";
            }
            else
            {
                type = "c";
            }
            cards.add(new Card(num, type));
            if (num == 13)
            {
                num = 1;
            }
            else
            {
                num++;
            }
        }
        while(!cards.isEmpty())
        {
            int rand = (int)(Math.random()*cards.size());
            stock.push(cards.get(rand));
            cards.remove(rand);
        }
    }

    /**
     * Deals the cards from the stock to the
     * 7 piles. The top card of each pile is turned
     * face up.
     * 
     * @postcondition   cards in the stock have been dealt
     *                  to each of the 7 piles, and the top 
     *                  card of each pile is face up
     */
    private void deal()
    {
        for (int p = 0; p < piles.length; p++)
        {
            for (int c = p; c < piles.length; c++)
            {
                piles[c].add(stock.pop());
            }
            piles[p].get(p).turnUp();
        }
    }

    /**
     * Moves the top three cards from the stock
     * to the waste. If there are less than three
     * cards on the stock, those remaining cards
     * are transferred to the waste. Cards in the waste
     * should be face up.
     * 
     * @postcondition   the top three cards on the stock 
     *                  have been moved to the waste
     *                  (and are face up), but if there
     *                  are less than three cards on 
     *                  the stock, however many cards are left
     *                  are moved to the waste
     */
    private void dealThreeCards()
    {
        for (int i = 0; i < 3; i++)
        {
            if (!stock.isEmpty())
            {
                waste.push(stock.pop());
                waste.peek().turnUp();
            }
        }
    }

    /**
     * Repeatedly moves the top card from
     * the waste to the top of the stock
     * until the waste is empty (has
     * no cards). All the cards in the
     * stock should be face down.
     * 
     * @postcondition   the waste is empty, and the
     *                  stock contains the cards that
     *                  were previously in the waste
     *                  (and the cards are face up)
     */
    private void resetStock()
    {
        while (!waste.isEmpty())
        {
            stock.push(waste.pop());
            stock.peek().turnDown();
        }
    }

    /**
     * Checks if a given card can be moved
     * to the given pile (following the rules
     * of solitaire).
     * 
     * @precondition    0 <= index < 7
     * @postcondition   Returns true if the given card can be 
     *                  legally moved to the top of the given
     *                  pile
     */
    private boolean canAddToPile(Card card, int index)
    {
        if (piles[index].isEmpty())
        {
            return card.getRank() == 13;
        }
        Card curr = piles[index].peek();
        if (curr.isFaceUp())
        {
            if (card.isRed() != curr.isRed()
                && card.getRank() + 1 == curr.getRank())
            {
                return true;
            }       
        }
        return false;
    }

    /**
     * Removes all the face-up cards in a given pile
     * and returns a stack of those cards.
     * 
     * @precondition     0 <= index < 7
     * @postcondition    Removes all face-up cards on the top of
     *                   the given pile; returns a stack
     *                   containing these cards
     *                   
     * @return          a stack that contains all the face-up cards
     *                  that have been removed
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> faceUp = new Stack<Card>();
        while (!piles[index].isEmpty() && piles[index].peek().isFaceUp())
        {
            faceUp.push(piles[index].pop());
        }
        return faceUp;
    }
    
    /**
     * Adds the cards in the given stack to the pile
     * with the given index.
     * 
     * @precondition    0 <= index < 7
     * @postcondition   Removes elements from cards, and adds 
     *                  them to the given pile.
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while (!cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }
    
    /**
     * @precondition     0 <= index < 4
     * @postcondition    Returns true if the given card can be
     *                   legally moved to the top of the given
     *                   foundation
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        Stack<Card> curr = foundations[index];
        if (curr.isEmpty() && card.getRank() == 1)
        {
            return true;
        }
        else if (curr.isEmpty())
        {
            return false;
        }
        else if (curr.peek().getRank() + 1 == card.getRank() 
                    && curr.peek().getSuit().equals(card.getSuit()))
        {
            return true;
        }
        return false;
    }
}