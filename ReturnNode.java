public class ReturnNode extends Node {
  public void print () {
    System . out . print ("(return ");
    if (r != null)
      r . print ();
    System . out . print (")");
  }
}
