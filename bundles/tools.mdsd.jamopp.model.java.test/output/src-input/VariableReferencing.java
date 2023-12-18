public class VariableReferencing {
public VariableReferencing var;

public  void method() {
var = null;
VariableReferencing var;
var = null;
{
var = null;
if (true)
{
var = null;
}
}
var = new  VariableReferencing();
var.method();
int i = new  VariableReferencing(){
public int x = 0;

}
.x;
}

}
