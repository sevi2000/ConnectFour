package fr.formiko.connectfour;

public enum Player {
    RED,BLUE,BLANK;
    Player switchPlayer(){
        return switch (this){
            case BLANK,RED -> BLUE;
            case BLUE -> RED;


        };
    }
}
