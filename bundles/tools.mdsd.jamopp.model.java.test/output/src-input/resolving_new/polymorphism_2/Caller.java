package resolving_new.polymorphism_2;

public class Caller {
public  void m(SuperClass sc) {
}

public  void m(Caller c) {
}

public  void polymorphicCalls() {
SubClassA a = new  SubClassA();
SubClassB b = new  SubClassB();
m(a);
m(b);
m(this);
}

}
