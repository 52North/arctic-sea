/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.janmayen;

import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Tree implementation of {@link IntervalMap}.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Christian Autermann
 */
public class IntervalTree<K, V> implements IntervalMap<K, V> {
    private IntervalNode root;
    private final Comparator<? super K> comparator;

    /**
     * Creates a new {@code IntervalTree}.
     *
     * @param comparator the comparator to compare keys
     */
    public IntervalTree(Comparator<? super K> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    /**
     * Creates a new {@code IntervalTree} that uses the natural order of the keys.
     */
    @SuppressWarnings("unchecked")
    public IntervalTree() {
        this((k1, k2) -> ((Comparable<? super K>) k1).compareTo(k2));
    }

    /**
     * Adds a new element to the interval tree
     *
     * @param min   the lower bound of the interval
     * @param max   the upper bound of the interval
     * @param value the associated value
     *
     * @throws IllegalArgumentException if {@code min} &gt; {@code max}
     */
    public void add(K min, K max, V value) throws IllegalArgumentException {
        add(new Interval(min, max), value);
    }

    /**
     * Adds a new element to the interval tree
     *
     * @param key   the lower and upper bound of the interval
     * @param value the associated value
     */
    public void add(K key, V value) {
        add(key, key, value);
    }

    private void add(Interval element, V value) {
        IntervalNode x = root;
        IntervalNode node = new IntervalNode(element, value);

        if (root == null) {
            root = node;
            return;
        }

        while (true) {
            if (this.comparator.compare(node.getMax(), x.getMax()) > 0) {
                x.setMax(node.getMax());
            }

            int compare = element.compareTo(x.getKey());
            if (0 == compare) {
                return;
            } else if (compare < 0) {
                if (x.getLeft() == null) {
                    node.setParent(x);
                    x.setLeft(node);
                    x.decrementBalanceFactor();
                    break;
                }
                x = x.getLeft();
            } else {
                if (x.getRight() == null) {
                    node.setParent(x);
                    x.setRight(node);
                    x.incrementBalanceFactor();
                    break;
                }
                x = x.getRight();
            }
        }

        rebalance(x);
    }

    @Override
    public Set<V> search(K lower, K upper) {
        Interval interval = new Interval(lower, upper);
        List<IntervalNode> found = new LinkedList<>();
        search1(interval, this.root, found);
        return found.stream().map(IntervalNode::getValue).collect(toSet());
    }

    @Override
    public Optional<V> get(K lower, K upper) {
        Interval interval = new Interval(lower, upper);
        return getNearest(interval).map(IntervalNode::getValue);
    }

    /**
     * Get the bigger value of {@code a} and {@code b}
     *
     * @param a the first value
     * @param b the second value
     *
     * @return the maximum of {@code a} and {@code b}
     */
    private K max(K a, K b) {
        return this.comparator.compare(a, b) > 0 ? a : b;
    }

    private void recalculateMax(IntervalNode node) {
        Stream.of(node.getLeft(), node.getRight(), node)
                .filter(Objects::nonNull)
                .forEachOrdered(n -> n.setMax(max(n.getKey().getUpper(),
                                                  Stream.of(n.getLeft(), n.getRight())
                                                          .filter(Objects::nonNull)
                                                          .map(IntervalNode::getMax)
                                                          .reduce(this::max)
                                                          .orElseGet(n.getKey()::getUpper))));
    }

    private void rotateRight(IntervalNode node) {
        IntervalNode y = node.getLeft();
        node.setLeft(y.getRight());
        if (node.getLeft() != null) {
            node.getLeft().setParent(node);
        }
        y.setParent(node.getParent());
        if (y.getParent() == null) {
            this.root = y;
        } else if (node.getParent().getLeft() == node) {
            node.getParent().setLeft(y);
        } else {
            node.getParent().setRight(y);
        }
        y.setRight(node);
        node.setParent(y);
    }

    private void rotateLeft(IntervalNode node) {
        IntervalNode y = node.getRight();
        node.setRight(y.getLeft());
        if (node.getRight() != null) {
            node.getRight().setParent(node);
        }
        y.setParent(node.getParent());
        if (y.getParent() == null) {
            this.root = y;
        } else if (node.getParent().getLeft() == node) {
            y.getParent().setLeft(y);
        } else {
            y.getParent().setRight(y);
        }
        y.setLeft(node);
        node.setParent(y);
    }

    private void rotateRightLeft(IntervalNode node) {
        rotateRight(node.getRight());
        rotateLeft(node);
    }

    private void rotateLeftRight(IntervalNode node) {
        rotateLeft(node.getLeft());
        rotateRight(node);
    }

    private IntervalNode minimumNode(IntervalNode node) {
        IntervalNode n = node;
        while (n.getLeft() != null) {
            n = n.getLeft();
        }
        return n;
    }

    private IntervalNode maxiumNode(IntervalNode node) {
        IntervalNode n = node;
        while (n.getRight() != null) {
            n = n.getRight();
        }
        return n;
    }

    private IntervalNode successor(IntervalNode node) {
        if (node.getRight() != null) {
            return minimumNode(node.getRight());
        }
        IntervalNode n = node;
        while (n.getParent() != null && n == n.getParent().getRight()) {
            n = n.getParent();
        }
        return n.getParent();
    }

    private IntervalNode predecessor(IntervalNode node) {
        if (node.getLeft() != null) {
            return maxiumNode(node.getLeft());
        }
        IntervalNode n = node;
        while (n.getParent() != null && n == n.getParent().getLeft()) {
            n = n.getParent();
        }
        return n.getParent();
    }

    private void search1(Interval interval, IntervalNode node, List<IntervalNode> result) {
        if (node == null) {
            return;
        }

        if (this.comparator.compare(node.getMax(), interval.getLower()) < 0) {
            return;
        }

        search1(interval, node.getLeft(), result);

        if (this.comparator.compare(interval.getLower(), node.getKey().getUpper()) < 0 &&
            this.comparator.compare(node.getKey().getLower(), interval.getUpper()) < 0) {
            result.add(node);
        }

        if (this.comparator.compare(interval.getUpper(), node.getKey().getLower()) < 0) {
            return;
        }

        search1(interval, node.getRight(), result);
    }

    private Optional<IntervalNode> getNearest(Interval key) {
        IntervalNode x = root;
        if (x == null) {
            return Optional.empty();
        }
        IntervalNode previous = x;
        int compare = 0;
        while (x != null) {
            previous = x;
            compare = key.compareTo(x.getKey());
            if (0 == compare) {
                return Optional.of(x);
            } else if (compare < 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        x = (compare < 0) ? predecessor(previous) : successor(previous);
        if (x == null) {
            return Optional.of(previous);
        }
        int otherCompare = key.compareTo(x.getKey());
        if (compare < 0) {
            return Optional.of(Math.abs(compare) < otherCompare ? previous : x);
        } else {
            return Optional.of(Math.abs(otherCompare) < compare ? x : previous);
        }

    }

    private void rebalance(IntervalNode x) {
        IntervalNode node = x;
        while (node.getBalanceFactor() != 0 && node.getParent() != null) {
            if (node.getParent().getLeft() == node) {
                node.getParent().decrementBalanceFactor();
            } else {
                node.getParent().incrementBalanceFactor();
            }

            node = node.getParent();
            if (node.getBalanceFactor() == 2) {
                if (node.getRight().getBalanceFactor() == 1) {
                    rotateLeft(node);
                    node.setBalanceFactor(0);
                    node.getParent().setBalanceFactor(0);
                    node = node.getParent();
                    recalculateMax(node);
                } else {
                    rotateRightLeft(node);
                    node = node.getParent();
                    recalculateMax(node);
                    switch (node.getBalanceFactor()) {
                        case 1:
                            node.getRight().setBalanceFactor(0);
                            node.getLeft().setBalanceFactor(-1);
                            break;
                        case 0:
                            node.getRight().setBalanceFactor(0);
                            node.getLeft().setBalanceFactor(0);
                            break;
                        default:
                            node.getRight().setBalanceFactor(1);
                            node.getLeft().setBalanceFactor(0);
                            break;
                    }
                    node.setBalanceFactor(0);
                }
                break;
            } else if (node.getBalanceFactor() == -2) {
                if (node.getLeft().getBalanceFactor() == -1) {
                    rotateRight(node);
                    node.setBalanceFactor(0);
                    node.getParent().setBalanceFactor(0);
                    node = node.getParent();
                    recalculateMax(node);
                } else {
                    rotateLeftRight(node);
                    node = node.getParent();
                    recalculateMax(node);
                    switch (node.getBalanceFactor()) {
                        case -1:
                            node.getRight().setBalanceFactor(1);
                            node.getLeft().setBalanceFactor(0);
                            break;
                        case 0:
                            node.getRight().setBalanceFactor(0);
                            node.getLeft().setBalanceFactor(0);
                            break;
                        default:
                            node.getRight().setBalanceFactor(0);
                            node.getLeft().setBalanceFactor(-1);
                            break;
                    }
                    node.setBalanceFactor(0);
                }
                break;
            }
        }
    }

    /**
     * Simple interval implementation.
     */
    private class Interval implements Comparable<Interval> {

        private final K upper;
        private final K lower;

        /**
         * Creates a new {@code Interval}.
         *
         * @param lower the lower bound
         * @param upper the upper bound
         *
         * @throws IllegalArgumentException if {@code lower} &gt; {@code upper}
         */
        Interval(K lower, K upper) {
            if (comparator.compare(lower, upper) > 0) {
                throw new IllegalArgumentException(String.format("Illegal interval: [%s,%s]", lower, upper));
            }
            this.upper = upper;
            this.lower = lower;
        }

        @Override
        public int compareTo(Interval other) {
            int comp = comparator.compare(getLower(), getLower());
            if (comp == 0) {
                comp = comparator.compare(getUpper(), getUpper());
            }
            return comp;
        }

        /**
         * @return the upper
         */
        K getUpper() {
            return upper;
        }

        /**
         * @return the lower
         */
        K getLower() {
            return lower;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.lower, this.upper);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            final Interval other = (Interval) obj;
            return Objects.equals(this.lower, other.lower) &&
                   Objects.equals(this.upper, other.upper);
        }

    }

    /**
     * Interval node implementation.
     */
    private class IntervalNode {
        private final Interval key;
        private final V value;
        private IntervalNode parent;
        private IntervalNode left;
        private IntervalNode right;
        private int balanceFactor;
        private K max;

        /**
         * Creates a new {@code IntervalNode}.
         *
         * @param interval the interval
         * @param value    the value
         */
        IntervalNode(Interval interval, V value) {
            this.key = interval;
            this.max = interval.getUpper();
            this.value = value;
        }

        /**
         * @return the key
         */
        Interval getKey() {
            return key;
        }

        /**
         * @return the value
         */
        V getValue() {
            return value;
        }

        /**
         * @return the parent
         */
        IntervalNode getParent() {
            return parent;
        }

        /**
         * @param parent the parent to set
         */
        void setParent(IntervalNode parent) {
            this.parent = parent;
        }

        /**
         * @return the left
         */
        IntervalNode getLeft() {
            return left;
        }

        /**
         * @param left the left to set
         */
        void setLeft(IntervalNode left) {
            this.left = left;
        }

        /**
         * @return the right
         */
        IntervalNode getRight() {
            return right;
        }

        /**
         * @param right the right to set
         */
        void setRight(IntervalNode right) {
            this.right = right;
        }

        /**
         * @return the balanceFactor
         */
        int getBalanceFactor() {
            return balanceFactor;
        }

        /**
         * @param balanceFactor the balanceFactor to set
         */
        void setBalanceFactor(int balanceFactor) {
            this.balanceFactor = balanceFactor;
        }

        /**
         * Decrements the balance factor.
         */
        void decrementBalanceFactor() {
            this.balanceFactor--;
        }

        /**
         * Increments the balance factor.
         */
        void incrementBalanceFactor() {
            this.balanceFactor++;
        }

        /**
         * @return the max
         */
        K getMax() {
            return max;
        }

        /**
         * @param max the max to set
         */
        void setMax(K max) {
            this.max = max;
        }
    }
}
