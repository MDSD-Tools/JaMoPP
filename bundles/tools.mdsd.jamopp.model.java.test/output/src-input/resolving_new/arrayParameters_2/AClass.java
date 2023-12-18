package resolving_new.arrayParameters_2;

public class AClass {
public  void m(String string) {
System.out.println("m(String)");
}

public  void m(String[]  strings) {
System.out.println("m(String...)");
}

public static  void main(String[]  args) {
AClass a = new  AClass();
a.m("");
a.m(new String[]  {"", ""});
String[]  aStringArray = {"1", "3"};
a.m(aStringArray);
a.m(aStringArray[1]);
}

}
