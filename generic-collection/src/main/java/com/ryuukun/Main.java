package com.ryuukun;

import com.ryuukun.colllection.ArrayGenericCollection;
import com.ryuukun.colllection.GenericCollection;
import com.ryuukun.colllection.LinkedGenericCollection;

public class Main {
    public static void main(String[] args) {
        GenericCollection<Integer> arrayColl = new ArrayGenericCollection<>();
        GenericCollection<Integer> linkedColl = new LinkedGenericCollection<>();

        linkedColl.add(1);
        linkedColl.add(2);
        linkedColl.add(3);

        System.out.print("get 0 element: " + linkedColl.get(0));

        linkedColl.remove(1);

        linkedColl.printAll();

        arrayColl.add(1);
        arrayColl.add(2);
        arrayColl.add(3);

        System.out.println("get 0 element: " + arrayColl.get(0));

        arrayColl.remove(1);
        arrayColl.remove(1);

        arrayColl.printAll();
    }
}