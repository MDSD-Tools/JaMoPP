public class AnnotationsAsAnnotationArguments {
public @interface A1 {
}
public @interface A2 {
public  String property() ;

}
public @interface B1 {
@B1(m = @A1
)
public  A1 m() ;

}
public @interface B2 {
@B2(m = @A2(property = "something")
)
public  A2 m() ;

}
public @interface C0 {
@C0(m = {"", "", ""})
public  String[]  m() ;

}
public @interface C1 {
@C1(m = {@A1
, @A1
, @A1
})
public  A1[]  m() ;

}
public @interface C2 {
@C2(m = {@A2(property = "value1")
, @A2(property = "value2")
, @A2(property = "value3")
})
public  A2[]  m() ;

}
public @interface C3 {
@C3({@A1
, @A1
, @A1
})
public  A1[]  value() ;

}
}
