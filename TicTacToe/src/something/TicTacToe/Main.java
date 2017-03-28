package something.TicTacToe;

import something.TicTacToe.Gui.StartGui;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world!");
//        System.out.println(new TicTacToeBoard().toString());
        javafx.application.Application.launch(StartGui.class);
	}
}
