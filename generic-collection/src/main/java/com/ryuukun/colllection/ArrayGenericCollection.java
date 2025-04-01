package com.ryuukun.colllection;

import java.util.Objects;

public class ArrayGenericCollection<T> implements GenericCollection<T> {
    public static final int DEFAULT_SIZE = 2;

    private Object[] data;

    private int size;

    public ArrayGenericCollection() {
        this.data = new Object[DEFAULT_SIZE];
        this.size = 0;
    }

    @Override
    public void add(T element) {
        if (size >= data.length) {
            realloc();
        }

        data[size++] = element;
    }

    @Override
    public void remove(int index) {
        Objects.checkIndex(index, size);

        System.arraycopy(data, index + 1, data, index, size - index);
        data[size--] = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        Objects.checkIndex(index, size);

        return (T) data[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printAll() {
        System.out.print("[");
        for (int i = 0; i < size; ++i) {
            System.out.print(data[i] + (i + 1 < size ? ", " : ""));
        }
        System.out.println("]");
    }

    private void realloc() {
        Object[] tmp = new Object[data.length * 2];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
    }
}
