package fr.formiko.connectfour;

public class Win {
    enum Type {
        HORIZONTAL,
        VERTICAL,
        SWDIAG,
        NWDIAG;
    }
    Type type;
    int col;
    int row;

     Win(Type type, int col, int row) {
        this.type = type;
        this.col = col;
        this.row = row;
    }
}
