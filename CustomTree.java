package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {


    private Entry<String> root;



    public CustomTree() {
        root = new Entry<>("0");
    }



    public static void main(String[] args) {
        List<String> list = new CustomTree();
        for (int i = 1; i < 16; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println("Size: Expected 15, actual is " + list.size());
        System.out.println("Parent: Expected NOT PARENT, actual is " + ((CustomTree) list).getParent("0"));
        System.out.println("Parent: Expected 0, actual is " + ((CustomTree) list).getParent("1"));
        System.out.println("Parent: Expected 1, actual is " + ((CustomTree) list).getParent("3"));
        System.out.println("Parent: Expected 1, actual is " + ((CustomTree) list).getParent("4"));
        System.out.println("Parent: Expected 2, actual is " + ((CustomTree) list).getParent("5"));
        System.out.println("Parent: Expected 3, actual is " + ((CustomTree) list).getParent("7"));
        System.out.println("Parent: Expected 4, actual is " + ((CustomTree) list).getParent("9"));
        System.out.println("Parent: Expected 5, actual is " + ((CustomTree) list).getParent("11"));
        System.out.println("Parent: Expected 6, actual is " + ((CustomTree) list).getParent("13"));
        System.out.println("Parent: Expected 7, actual is " + ((CustomTree) list).getParent("15"));
        System.out.println("Parent: Expected null, actual is " + ((CustomTree) list).getParent("20"));
        System.out.println("Remove: element 15: " + list.remove("15"));
        //System.out.println("Remove: element 3: " + list.remove("3"));
        System.out.println("Size: Expected 14, actual is " + list.size());
        //list.add("16");

        System.out.println();
        System.out.println("Общий вид дерева:");
        System.out.println(list);
    }



    @Override
    public boolean add(String s) {
        if (s == null)
            return false;
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {

            // Тут было решение, если вкратце, то нужно брать элемент из очереди, проверять его потомков,
            // и если какого-то нет, то ставить на его место новый элемент.
            // Если все потомки есть, то добавлять их в очередь и делать тоже самое с ними
            Entry<String> rootElement = queue.poll();
            if(rootElement!=null) {
                if (rootElement.isAvailableToAddChildren()) {
                    if (rootElement.availableToAddLeftChildren) {
                        rootElement.leftChild = new Entry<>(s);
                        rootElement.leftChild.parent = rootElement;
                    } else if (rootElement.availableToAddRightChildren) {
                        rootElement.rightChild = new Entry<>(s);
                        rootElement.rightChild.parent = rootElement;
                    }
                    return true;
                } else {
                    queue.offer(rootElement.leftChild);
                    queue.offer(rootElement.rightChild);
                }
            }
        }
        return false;
    }



    @Override
    public boolean remove(Object o) {
        if (o == null)
            return false;
        String s = (String) o;
        if (s.equals(root.elementName)) {
            root = new Entry<>("0");
            return true;
        }
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            //Удаление элемента похоже на добавление,
            // надо проверять потомков у текущего элемента из очереди и обнулять потомка с искомым именем
            Entry<String> rootElement = queue.poll();
            if (rootElement.leftChild != null && rootElement.leftChild.elementName.equals(s)) {
                //rootElement.leftChild.parent = null;
                rootElement.leftChild = null;
                return true;
            } else if (rootElement.rightChild != null && rootElement.rightChild.elementName.equals(s)) {
                //rootElement.rightChild.parent = null;
                rootElement.rightChild = null;
                return true;
            } else {
                if (rootElement.leftChild != null) {
                    queue.offer(rootElement.leftChild);
                }
                if (rootElement.rightChild != null) {
                    queue.offer(rootElement.rightChild);
                }
            }
        }
        return false;
    }



    public String getParent(String s) {
        if (root == null || s == null)
            return null;

        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            //тоже берём первый элемент из очереди, если это искомый элемент, то возвращаем имя его родителя,
            //иначе проверяем его детей, и если они есть, то добавляем их в очередь. И всё по новой
            Entry<String> rootElement = queue.poll();
            if (rootElement.elementName.equals(s)) {
                if (rootElement.parent == null)
                    return "NOT PARENT";
                else
                    return rootElement.parent.elementName;
            } else {
                if (rootElement.leftChild != null) {
                    queue.offer(rootElement.leftChild);
                }
                if (rootElement.rightChild != null) {
                    queue.offer(rootElement.rightChild);
                }
            }
        }
        return null;
    }



    @Override
    public int size() {
        Queue<Entry<String>> queue = new LinkedList<>();
        int count = 0;
        if (root != null) {
            if (root.leftChild != null)
                queue.offer(root.leftChild);
            if (root.rightChild != null)
                queue.offer(root.rightChild);
        }
        while (!queue.isEmpty()) {
            //берём элемент из очереди, считаем его,
            // потом добавляем в очередь существующих потомков, и всё по новой
            Entry<String> rootElement = queue.poll();
            count++;
            if (rootElement.leftChild != null) {
                queue.offer(rootElement.leftChild);
            }
            if (rootElement.rightChild != null) {
                queue.offer(rootElement.rightChild);
            }
        }
        return count;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Queue<Entry<String>> queue = new LinkedList<>();

        if (root != null) {
            if (root.leftChild != null)
                queue.offer(root.leftChild);
            if (root.rightChild != null)
                queue.offer(root.rightChild);
        }
        while (!queue.isEmpty()) {
            //берём элемент из очереди, добавляем значение его метода toString() в sb,
            // потом добавляем в очередь существующих потомков, и всё по новой
            Entry<String> rootElement = queue.poll();
            sb.append(rootElement.toString());
            if (rootElement.leftChild != null) {
                queue.offer(rootElement.leftChild);
            }
            if (rootElement.rightChild != null) {
                queue.offer(rootElement.rightChild);
            }
        }
        return sb.toString();
    }



    static class Entry<T> implements Serializable {


        String elementName;
        int lineNumber;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;



        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }



        void checkChildren() {
            if (leftChild != null)
                availableToAddLeftChildren = false;

            if (rightChild != null)
                availableToAddRightChildren = false;
        }



        boolean isAvailableToAddChildren() {
            checkChildren();
            return availableToAddLeftChildren | availableToAddRightChildren;
        }



        @Override
        public String toString() {

            String parent = elementName;

            String left = "null";
            if (leftChild != null) {
                left = leftChild.elementName;
            }

            String right = "null";
            if (rightChild != null) {
                right = rightChild.elementName;
            }

            return String.format("%s {%s %s}%n", parent, left, right);
        }
    }



    @Override
    public String get(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    public void add(int index, String element) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    public String set(int index, String element) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    public String remove(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    public boolean addAll(int index, Collection<? extends String> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    public List<String> subList(int fromIndex, int toIndex) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    protected void removeRange(int fromIndex, int toIndex) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
