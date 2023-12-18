public class AnonymousInner {
public class Inner {
}
public  void createInner1() {
AnonymousInner ai = new  AnonymousInner(){
public AnonymousInner field;

}
;
}

public  void createInner2() {
this.new  Inner(){
}
;
}

}
