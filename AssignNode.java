public class AssignNode extends Node {
  public void print () {
    System . out . print ("(<- ");
    if (this . l !=  null)
      //System . out . println (this . l .getClass().getName());
      this . l . print ();
    if (this . r != null)
      //System . out . println (this . l .getClass().getName());
      this . r . print ();
    System . out . print (")");
  }
}
