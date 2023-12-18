public class CommentsInArrayInitializers {
protected  Integer[]  m1() {
return new Integer[]  {new  Integer(1)};
}

protected  Integer[] []  m2() {
return new Integer [1] [2] ;
}

protected  String[] []  m3() {
return new String[] []  {{"s1"}, {"s2", "s3"}};
}

protected static String[]  f1 = {"variable", "value", "description"};

}
