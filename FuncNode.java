public class FuncNode extends Node {
  public void print () {
    System . out . print ("(apply " + token . lexeme());
    if (l != null)
      l . print ();
    if (r != null)
      r . print ();
    System . out . print (")");
  }
}
