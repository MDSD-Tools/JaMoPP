public class EqualityExpression {
 void foo(int i) {
double x = 30000.0D;
double y = 300.0F;
double z = 33242.3D;
double a = 3;
double b = 9;
double c = 9.0F;
switch (i) {
case (("1" == "2" == true != false == false != false == true == false == false != true) ? 1 : 0): case (("1" != "2" == true) ? 2 : 0): case (("1" == "2" != true) ? 3 : 0): case (("1" != "2" != false) ? 4 : 0): }
}

}
