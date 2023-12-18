public class ConditionalStatements {
public  void conditions() {
if (true)
{
}
if (false)
{
}
else
{
}
}

public  void conditionsWithSingleStatements() {
if (true)
return;
if (false)
return;
else
return;
}

public  void ConditionsWithCascades() {
if (false)
{
}
else
{
if (false)
{
}
else
{
if (true)
{
return;
}
}
}
}

public  void conditionsWithElseIf() {
if (false)
{
}
else
if (false)
{
}
else
if (true)
return;
}

}
