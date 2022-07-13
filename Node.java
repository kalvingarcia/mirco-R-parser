public class Node {
  public Token token;

  public Node l = null;
  public Node r = null;
  public Node e = null;

  public void print () {
    System . out . print ("(: ");
    if (this . l !=  null)
      //System . out . println (this . l .getClass().getName());
      this . l . print ();
    if (this . r != null)
      this . r . print ();
    System . out . print (")");
  }
}
