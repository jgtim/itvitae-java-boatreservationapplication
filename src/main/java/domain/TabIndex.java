package domain;

public enum TabIndex {
    AGENDA(0),
    CATALOGUS(1),
    RESERVERINGEN(2);

    private int index;

    TabIndex(int i) {
        index = i;
    }

    public int getIndex() {return index;}
}
