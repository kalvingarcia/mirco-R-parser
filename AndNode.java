public class AndNode extends Node {
  public void print () {
    System . out . print ("(&& ");
    if (l !=  null)
      l . print ();
    if (r != null)
      r . print ();
    System . out . print (")");
  }
}
