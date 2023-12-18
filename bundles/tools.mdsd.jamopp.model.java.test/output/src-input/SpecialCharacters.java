import java.util.HashMap;
import java.util.Map;
public class SpecialCharacters {
 void m1() {
"xyz\5abc".toString();
char aChar = ' ';
aChar = '\"';
aChar = '"';
aChar = '\5';
int x = 0;
StringBuffer outBuffer = new  StringBuffer();
boolean escapeSpace = true;
switch (aChar) {
case ' ': if (x == 0 || escapeSpace)
outBuffer.append('\\');
outBuffer.append(' ');
break;
case '\t': outBuffer.append('\\');
outBuffer.append('t');
aChar = '	';
break;
case '\n': outBuffer.append('\\');
outBuffer.append('n');
break;
case '\r': outBuffer.append('\\');
outBuffer.append('r');
break;
case '\f': outBuffer.append('\\');
outBuffer.append('f');
break;
case '\37': break;
case '\010': break;
case '=': case ':': case '#': case '!': outBuffer.append('\\');
outBuffer.append(aChar);
break;
default: if (((aChar < 0x20) || (aChar > 0x7e)))
{
outBuffer.append('\\');
outBuffer.append('u');
}
else
{
outBuffer.append(aChar);
}

}
}

private static Map<String, Character> entities;

static {
entities = new  HashMap<String, Character>();
entities.put("&lt;", '<');
entities.put("&gt;", '>');
entities.put("&amp;", '&');
entities.put("&quot;", '"');
entities.put("&acute;", (char) 180);
entities.put("&sect;", (char) 167);
entities.put("&#39;", '\'');
}
}
