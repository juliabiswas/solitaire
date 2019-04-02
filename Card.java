
/**
 * Instances of the Card class
 * are cards in a standard deck of cards.
 * Each instance of the Card class
 * has its own suit, rank, and can be
 * face up or face down.
 * 
 * @author Julia Biswas
 * @version October 30, 2018
 */
public class Card
{
    private int rank;
    private String suit;
    private boolean isFaceUp;
    
    /**
     * Constructs instances of the class Card.
     * 
     * @param rank  the rank of the card (between 1 and 13
     *              where 1 = ace and 13 = king)
     * @param suit  the suit of the card (c = club, d = diamond,
     *              h = heart, and s = spade)
     */
    public Card(int rank, String suit)
    {
        this.rank = rank;
        this.suit = suit;
        isFaceUp = false;
    }
    
    /**
     * Retrieves the rank of the card.
     * 
     * @return      the rank of the card
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * Retrieves the suit of the card.
     * 
     * @return      the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Determines if the card is a diamond or heart (red suit).
     * 
     * @return true if the card is a red suit (diamond/heart); otherwise,
     *          false
     */
    public boolean isRed()
    {
        return suit.equals("d") || suit.equals("h");
    }
    
    /**
     * Determines whether the card is face up or not.
     * 
     * @return true if the card is face up; otherwise,
     *          false
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    
    /**
     * Turns the card up (by setting the 
     * isFaceUp variable to true).
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns the card down (by setting the
     * isFaceUp variable to false).
     */
    public void turnDown()
    {
        isFaceUp = false;
    }
    
    /**
     * Retrieves the file name of the card's image.
     * 
     * @return the file name of the card's image
     */
    public String getFileName()
    {
        if (!isFaceUp())
        {
            return "cards/back.gif";
        }
        if (rank == 1)
        {
            return "cards/a" + suit + ".gif";
        }
        if (rank >= 2 && rank <= 9)
        {
            return "cards/" + rank + suit + ".gif";
        }
        if (rank == 10)
        {
            return "cards/t" + suit + ".gif";
        }
        if (rank == 11)
        {
            return "cards/j" + suit + ".gif";
        }
        if (rank == 12)
        {
            return "cards/q" + suit + ".gif";
        }
        return "cards/k" + suit + ".gif";
    }
} 
