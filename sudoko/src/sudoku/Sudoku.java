package sudoku;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Sudoku {

    public static void main(String[] args) {

        try {

            IloCplex model = new IloCplex();
            model.setParam(IloCplex.DoubleParam.TimeLimit, 300);
            //model.setParam(IloCplex.Param.TimeLimit, 120);
            Data data = new Data();
            data.init("src/sudoku/dicas.txt");

            //variáveis de decisão
            IloIntVar [][][] s = new IloIntVar[data.n][data.n][data.n +1];
            for (int i = 0; i < data.n; i++) {
                for (int j = 0; j < data.n; j++) {
                    for (int k = 0; k < data.n; k++) {
                        s[i][j][k] = model.boolVar();
                    }
                }
            }

            // adicionando dica
            for (int i = 0; i < data.n; i++) {
                for (int j = 0; j < data.n; j++) {
                    if(data.dica[i][j] > 0) {
                        int k = data.dica[i][j];
                        s[i][j][k-1].setLB(1.0);
                    }
                }
            }

            // restricao so pode ter 1 numero em profundidade
            for (int i = 0; i < data.n; i++) {
                for (int j = 0; j < data.n; j++) {
                    IloLinearIntExpr profundidade = model.linearIntExpr();
                    for (int k = 0; k < data.n; k++) {
                        profundidade.addTerm( 1, s[i][j][k]);
                    }
                    model.addEq(1, profundidade);
                }
            }

            // restricao coluna
            for (int j = 0; j < data.n; j++) {
                for (int k = 0; k < data.n; k++) {
                    IloLinearIntExpr linha = model.linearIntExpr();
                    for (int i = 0; i < data.n; i++) {
                        linha.addTerm(1,s[i][j][k]);
                    }
                    model.addEq(1, linha);
                }
            }

            // restricao linha
            for (int k = 0; k < data.n; k++) {
                for (int i = 0; i < data.n; i++) {
                    IloLinearIntExpr coluna = model.linearIntExpr();
                    for (int j = 0; j < data.n; j++) {
                        coluna.addTerm(1,s[i][j][k]);

                    }
                    model.addEq(1, coluna);
                }
            }

            //restricao de blocos 3x3
            for (int l = 0; l < 3; l++){
                for(int m = 0; m < 3; m++){
                    for(int k = 1; k < data.n; k++){
                        IloLinearIntExpr blocos=model.linearIntExpr();
                        for(int i = 0; i < 3; i++){
                            for(int j = 0; j < 3; j++){
                                blocos.addTerm( s[i+(l*3)][j+(m*3)][k], 1);
                            }
                        }
                        model.addEq(blocos, 1);
                    }
                }
            }


            //resolverndo o problema
            if (model.solve()) {

                for (int i = 0; i < data.n; i++) {
                    for (int j = 0; j < data.n; j++) {
                        boolean check = false;
                        for (int k = 0; k < data.n; k++) {
                            if(model.getValue(s[i][j][k]) != 0) {
                                check = true;
                                System.out.print((k+1)  + "\t" );
                            }
                        }
                        if (!check)
                            System.out.print(0 + "\t");
                    }
                    System.out.println("");
                }

            } else {
                System.out.println("Deu Merda !!");
            }

        } catch (IloException ex) {
            Logger.getLogger(Sudoku.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
