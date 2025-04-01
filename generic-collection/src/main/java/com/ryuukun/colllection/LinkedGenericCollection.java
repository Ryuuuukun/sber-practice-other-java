package com.ryuukun.colllection;

import java.util.Objects;

public class LinkedGenericCollection<T> implements GenericCollection<T> {
    private Node<T> head;
    private Node<T> tail;

    private int size;

    public LinkedGenericCollection() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(T element) {
        if (tail == null) {
            head = new Node<>(element, null, null);
            tail = head;
        } else {
            var ptr = tail;
            while (ptr.getNext() != null) {
                ptr = ptr.getNext();
            }

            ptr.setNext(new Node<>(element, null, ptr));
            tail = ptr.getNext();
        }
        ++size;
    }

    @Override
    public void remove(int index) {
        Objects.checkIndex(index, size);

        if (index == 0) {
            popFront();
        } else if (index == size - 1) {
            popBack();
        } else {
            var ptr = getNode(index);
            ptr.getNext().setPrev(ptr.getPrev());
            ptr.getPrev().setNext(ptr.getNext());
        }
        --size;
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);

        return getNode(index).getValue();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printAll() {
        System.out.print("[");
        for (var node = head; node != null; node = node.getNext()) {
            System.out.print(node.getValue() + (node.getNext() != null ? ", " : ""));
        }
        System.out.println("]");
    }

    public void popFront() {
        if (head.getNext() != null) {
            head.getNext().setPrev(null);
        }
        head = head.getNext();
    }

    public void popBack() {
        if (tail.getPrev() != null) {
            tail.getPrev().setNext(null);
        }
        tail = tail.getPrev();
    }

    private Node<T> getNode(int index) {
        var ptr = head;
        while (index-- > 0) {
            ptr = ptr.getNext();
        }
        return ptr;
    }

    public static class Node<T> {
        private T value;

        private Node<T> next;
        private Node<T> prev;

        public Node(T value, Node<T> next, Node<T> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }
    }
}
