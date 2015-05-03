package goeurotest;

public class GoEuroTest
{
  public static void main(String[] args)
  {
    PositionWrapper positionWp = new PositionWrapper(args);
    String result = positionWp.retrieve();

    if (result.equals("OK"))
      System.out.println("File " + positionWp.getFilename() + " has been created with the result.");
    else
      System.out.println("Some error occurred during the retrievement.");
  }
}