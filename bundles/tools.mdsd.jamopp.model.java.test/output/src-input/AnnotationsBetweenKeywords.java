public class AnnotationsBetweenKeywords {
public @interface A {
}
public @A
final int x = 0;

public @A
static  void m() {
}

public @A
static class InnerClass {
}
public @A
static enum InnerEnum {
;

}
public @A
static interface InnerInterface {
}
public @A
static @interface InnerAnnotation {
}
}
