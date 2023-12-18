package resolving_new.methodOverloading_2;

import java.util.ArrayList;
public class MethodOverloading {
public  Object[]  getAnnotations(int lineNumber) {
return null;
}

private  ArrayList<Object> getAnnotations(Object lineAnnotations) {
return null;
}

public  void m() {
getAnnotations(0);
ArrayList lineAnnotationsArray = new  ArrayList();
getAnnotations(lineAnnotationsArray.get(0));
ArrayList<Object> result = new  ArrayList<Object>();
result.addAll(getAnnotations(lineAnnotationsArray.get(0)));
}

}
