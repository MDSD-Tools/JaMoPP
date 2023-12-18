package resolving_new.fields_2;

public class AClass {
private AClass field1;

private AClass field2;

public  void m() {
field1.m();
field1.field1.m();
field2.m();
field2.field2.m();
field1.field2.m();
field2.field1.m();
}

}
