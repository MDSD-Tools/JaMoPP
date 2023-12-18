package resolving_new.methodParameters_4;

public class LocalCalls {
public  void m() {
}

public  void m(int a) {
}

public  void m(int a, int b) {
}

public  void m(Object a) {
}

public  void call() {
m();
m(1);
m(1, 2);
m(new  LocalCalls());
}

}
