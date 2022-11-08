package com.example.countrydeterminationservice;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Node<V> {

    private String key;

    @Setter
    private V value;

    private List<Node<V>> childs;

    public static <V> Node<V> createTree(Iterable<V> iterable, Function<V, String> keyMapper) {
        Node<V> tree = new Node<>("", null);
        for (V entry : iterable) {
            tree.add(keyMapper.apply(entry), entry);
        }
        return tree;
    }

    public static <I, V> Node<V> createTree(Iterable<I> iterable, Function<I, String> keyMapper, Function<I, V> valueMapper) {
        Node<V> tree = new Node<>("", null);
        for (I entry : iterable) {
            tree.add(keyMapper.apply(entry), valueMapper.apply(entry));
        }
        return tree;
    }

    private Node(String key, V value) {
        this.key = key;
        this.value = value;
        this.childs = new LinkedList<>();
    }

    private boolean add(Node<V> child) {
        return this.addInternal(child);
    }

    private boolean add(String key, V value) {
        return this.addInternal(new Node<>(key, value));
    }

    private boolean addInternal(Node<V> child) {
        if (StringUtils.isEmpty(child.key)) {
            throw new RuntimeException("Empty key: %s".formatted(child.key));
        }

        if (this.key.equals(child.key)) {
            throw new RuntimeException("Equal key: %s".formatted(child.key));
        }

        for (int i = 0; i < this.childs.size(); i++) {
            Node<V> node = this.childs.get(i);

            if (node.key.equals(child.key)) {
                //this = '',    node = 1,     child = 1
                break;
            }

            if (child.key.startsWith(node.key)) {
                //this = '',    node = 1,     child = 1242 =>
                if (node.childs.isEmpty()) {
                    for (int j = node.key.length() + 1; j < child.key.length(); j++) {
                        String key = child.key.substring(0, j); //12
                        Node<V> n = new Node<>(key, null);
                        node.add(n);
                        node = n;
                    }
                }
                return node.add(child);
            } else {
                //this = 124,   node = 1242,  child = 1246, but 124 already => out
                //this = 12,    node = 124,   child = 1264, no  126         => create and continue
                //this = 12,    node = 124,   child = 1268, but 126 already => out
                if (i == this.childs.size() - 1 && child.key.replace(this.key, StringUtils.EMPTY).length() > 1) { //last and distant
                    String key = child.key.substring(0, this.key.length() + 1); //126
                    Node<V> n = new Node<>(key, null);
                    this.childs.add(n);
                }
            }
        }
        return this.childs.add(child);
    }

    public List<V> findEqualTo(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("Invalid key: %s".formatted(key));
        }

        List<V> nodes = new ArrayList<>();

        this.findEqualToInternal(key, nodes);

        return nodes;
    }

    private List<V> findEqualToInternal(String key, List<V> nodes) {
        for (Node<V> node : this.childs) {
            if (key.startsWith(node.key)) {
                if (node.key.equals(key)) {
                    nodes.add(node.value);
                } else {
                    node.findEqualToInternal(key, nodes);
                }
            }
        }
        return nodes;
    }

    public List<V> findClosestTo(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("Invalid key: %s".formatted(key));
        }

        List<V> nodes = new ArrayList<>();

        this.findClosestToInternal(key, nodes);

        return nodes;
    }

    private List<V> findClosestToInternal(String key, List<V> nodes) {
        for (Node<V> node : this.childs) {
            if (key.startsWith(node.key)) {
                if (node.childs.isEmpty()) {
                    nodes.add(node.value);
                } else {
                    node.findClosestToInternal(key, nodes);
                }

            }
        }

        return nodes;
    }

}
