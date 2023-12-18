package pkgJava14;

public class IntersectionTypeWithTypeArguments {
 void someMethod() {
var kl = (D1<R> & D2<R>) (new  R());
kl.r();
}

class R implements D2<R> {
@Override
public  void r() {
}

@Override
public  void t() {
}

}
interface D1<T>  {
 void r() ;

}
interface D2<S extends D1<S>>  extends D1<S> {
 void t() ;

}
}
