package something.TicTacToe;

import something.TicTacToe.Gui.StartGui;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world!");
        //javafx.application.Application.launch(StartGui.class);
        System.out.println(new TicTacToeBoard().toString());
	}
}
