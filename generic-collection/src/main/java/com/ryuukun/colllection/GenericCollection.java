package com.ryuukun.colllection;

public interface GenericCollection<T> {
    void add(T element);

    void remove(int index);

    T get(int index);

    int size();

    void printAll();
}
