public class WhileNode extends Node {
  public void print () {
    System . out . print ("(while ");
    if (l !=  null)
      l . print ();
    if (r != null)
      r . print ();
    System . out . print (")");
  }
}
