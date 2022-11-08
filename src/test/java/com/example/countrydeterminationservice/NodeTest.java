package com.example.countrydeterminationservice;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NodeTest {

    @Test
    void test() {
        Node<Integer> tree = Node.createTree(
                List.of(1, 1, 2, 12, 13),
                Object::toString
        );

        Assertions.assertThat(tree).extracting("key").isEqualTo("");
        Assertions.assertThat(tree).extracting("value").isNull();
        ListAssert<Object> childs = Assertions.assertThat(tree)
                .extracting("childs", InstanceOfAssertFactories.LIST);

        ObjectAssert<Object> node1 = childs.element(0);
        node1.extracting("key").isEqualTo("1");
        node1.extracting("value").isEqualTo(1);
        ListAssert<Object> node1childs = node1.extracting("childs", InstanceOfAssertFactories.LIST).hasSize(2);

        ObjectAssert<Object> node1child1 = node1childs.element(0);
        node1child1.extracting("key").isEqualTo("12");
        node1child1.extracting("value").isEqualTo(12);
        node1child1.extracting("childs", InstanceOfAssertFactories.LIST).isEmpty();

        ObjectAssert<Object> node1child2 = node1childs.element(1);
        node1child2.extracting("key").isEqualTo("13");
        node1child2.extracting("value").isEqualTo(13);
        node1child2.extracting("childs", InstanceOfAssertFactories.LIST).isEmpty();

        ObjectAssert<Object> otherNode1 = childs.element(1);
        otherNode1.extracting("key").isEqualTo("1");
        otherNode1.extracting("value").isEqualTo(1);
        otherNode1.extracting("childs", InstanceOfAssertFactories.LIST).isEmpty();

        ObjectAssert<Object> node2 = childs.element(2);
        node2.extracting("key").isEqualTo("2");
        node2.extracting("value").isEqualTo(2);
        node2.extracting("childs", InstanceOfAssertFactories.LIST).isEmpty();
    }

}
