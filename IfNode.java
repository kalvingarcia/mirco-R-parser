public class IfNode extends Node {
  public void print () {
    System . out . print ("(if ");
    if (l != null)
      l . print ();
    if (r != null)
      r . print ();
    if (e != null)
      e . print ();
    System . out . print (")");
  }
}
