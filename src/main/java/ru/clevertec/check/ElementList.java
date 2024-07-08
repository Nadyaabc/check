package main.java.ru.clevertec.check;

import java.util.ArrayList;

public class ElementList {
    CheckElement head = null;
    CheckElement tail = null;
    ArrayList<Integer> uniqueId = new ArrayList<>();
    public ElementList() {
        head = null;
        tail=null;
    }
    boolean isEmpty() {
        return head == null;
    }
    CheckElement add(CheckElement elem) {
        if (isEmpty()) {
            head = elem;
            tail = elem;
            uniqueId.add(elem.id);
            System.out.println("head" + head.quantity + " " + head.id);
        }
        return tail;
    }
}



