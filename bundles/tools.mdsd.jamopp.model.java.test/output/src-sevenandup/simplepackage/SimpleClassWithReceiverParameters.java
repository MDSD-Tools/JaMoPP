package simplepackage;

public class SimpleClassWithReceiverParameters {
public  SimpleClassWithReceiverParameters(){
}
public  SimpleClassWithReceiverParameters(int i){
}
public  void someMethod(SimpleClassWithReceiverParameters this) {
this.someMethod();
}

public class SomeInnerClass {
public  SomeInnerClass(SimpleClassWithReceiverParameters SimpleClassWithReceiverParameters.this){
}
public  boolean anotherMethod(SimpleClassWithReceiverParameters.SomeInnerClass this) {
return true;
}

public  boolean anotherMethod2(SomeInnerClass this) {
return false;
}

}
 SomeInnerClass get(SimpleClassWithReceiverParameters this) {
return this.new  SomeInnerClass();
}

 void method1(SimpleClassWithReceiverParameters this, int i, String k) {
}

 void method2(SimpleClassWithReceiverParameters this, SomeInnerClass f, byte  ...b) {
}

}
