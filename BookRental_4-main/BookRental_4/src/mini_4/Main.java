package mini_4;

import java.util.Scanner;

public class Main {

   public static void main(String[] args) {
      TotalController ctrl = new TotalController();
      Scanner sc = new Scanner(System.in);
      
      ctrl.menu_start(sc);
      
      sc.close();
      System.out.println("\n~~~~~~ 프로그램종료 ~~~~~");
   }

}