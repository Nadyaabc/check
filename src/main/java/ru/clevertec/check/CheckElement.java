package main.java.ru.clevertec.check;

public class CheckElement {
    int id;
    int quantity;
    CheckElement next;
    public CheckElement() {
        id = 0;
        quantity=0;
        next=null;
    }

    public CheckElement(int id, int qty) {
        this.id = id;
        this.quantity = qty;
        next=null;
    }
}
