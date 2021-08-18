package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Data {

    public int n;

    public int d;

    public int dica[][];

    public void init(String directory) {
        try {
            Scanner read = new Scanner(new File(directory));
            read.useLocale(Locale.ENGLISH);

            // quantidade de produtos
            n =  read.nextInt();

            // quantidade de dicas
            d = read.nextInt();

            //caracteristicas dos produtos
            dica = new int[n][n];

            for(int i = 0; i < d; i++){
                int r = read.nextInt();
                int c = read.nextInt();
                int v = read.nextInt();
                dica[r-1][c-1]= v;
                read.nextLine();
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n ; j++) {
                    int valor = dica[i][j];
                    System.out.print(dica[i][j]+" ");
                }
                System.out.println();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error in file");
        }
    }

}
