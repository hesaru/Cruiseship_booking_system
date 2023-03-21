import java.util.Scanner;

public class Util {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";


    public static String getInputFromUser(String displayCommand) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(displayCommand);
        return scanner.nextLine();
    }

    public static void printWarning(String warningMessage) {
        System.out.println(ANSI_RED + warningMessage + ANSI_RESET);
    }
}
