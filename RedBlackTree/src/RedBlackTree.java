/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chawp
 * @param <E>
 */
public class RedBlackTree<E extends Comparable<E>>
{
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private Node<E> root;
    
    private static class Node<E extends Comparable<E>>
    {
        E element;
        Node<E> leftChild;
        Node<E> rightChild;
        Node<E> parent;
        boolean color;
        public Node(E data, boolean color)
        {
            this(data, color, null, null, null);
        }
        public Node(E data, boolean clr, Node<E> lc, Node<E> rc, Node<E> par)
        {
            element = data;
            color = clr;
            leftChild = lc;
            rightChild = rc;
            parent = par;
        }
    }
    
    public boolean insert(E element) throws NullPointerException
    {
        if(element == null)
            throw new NullPointerException("Error: Element is null");
        if(root == null)
        {
            root = new Node(element, BLACK);
            return true;
        }
        Node<E> current = root;
        Node<E> beforeCurrent = current;
        Node<E> add = new Node(element, RED);
        while(current != null)
        {
            beforeCurrent = current;
            switch (current.element.compareTo(add.element)) 
            {
                //current is the same as element being added, returns false
                case 0:
                    return false;
                //current is greater than element being added
                case 1:
                    current = current.leftChild;
                    break;
                //current is less than element being added
                case -1:
                    current = current.rightChild;
                    break;
                default:
                    break;
            }
        }
        //if the current before the null is greater
        if(beforeCurrent.element.compareTo(add.element) == 1)
            beforeCurrent.leftChild = add;
        else
            beforeCurrent.rightChild = add;
        //rebalance tree
        add.parent = beforeCurrent;
        rebalance(add);
        return true;
    }
    
    private void rebalance(Node<E> node)
    {
        node.color = RED;
        //Check to see if the insertion created a double red
        if(node != root && node.parent.color == RED)
        {
            //Check if uncle is black
            if(isBlack(sibling(node.parent)))
            {
                //Case: Restructure left left
                if(node.parent.parent.leftChild == node.parent && node.parent.leftChild == node)
                {
                    node = rotateWithLeftChild(node.parent.parent);
                }
                //restructure left right
                else if(node.parent.parent.leftChild == node.parent && node.parent.rightChild == node)
                {
                    node = doubleWithLeftChild(node.parent.parent);
                }
                else if(node.parent.parent.rightChild == node.parent && node.parent.rightChild == node)
                {
                    node = rotateWithRightChild(node.parent.parent);
                }
                else if(node.parent.parent.rightChild == node.parent && node.parent.leftChild == node)
                {
                    node = doubleWithRightChild(node.parent.parent);
                }
            }
            //Check if sibling is red
            else if(isRed(sibling(node.parent)))
            {
                //Case: double red with red uncle: recolor
                setBlack(node.parent);
                setBlack(sibling(node.parent));
                setRed(node.parent.parent);
                //check to see if double red has propogated to grandparent
                rebalance(node.parent.parent);
            }
        }
        if(node == root)
            root.color = BLACK;
    }
    
    public boolean contains(E object)
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        String s = makePreorder("", root);
        return s;
    }
    
    private String makePreorder(String s, Node<E> node)
    {
        //if the node exists
        String print = s;
        if(node != null )
        {
            print = makePreorder(print, node.leftChild);
            if(node.color == BLACK)
                print += node.element + "\t";
            else
                print += "*" + node.element + "\t";
            print = makePreorder(print, node.rightChild);
        }
        return print;
    }
    
    private Node<E> sibling(Node<E> node)
    {
        if(node != null && node != root)
        {
            if(node.parent.leftChild != null)
            {
                if(node.parent.leftChild.element.compareTo(node.element) == 0)
                {
                    if(node.parent.rightChild != null)
                        return node.parent.rightChild;
                }
                else
                {
                    if(node.parent.leftChild != null)
                        return node.parent.leftChild;
                }
            }
        }
        return null;
    }
    
    private Node<E> rotateWithLeftChild(Node<E> node)
    {
        Node<E> node2 = node.leftChild;
        node.leftChild = node2.rightChild;
        if(node2.rightChild != null)
            node2.rightChild.parent = node;
        if(node.parent == null)
            root = node2;
        else if(node.parent.rightChild == node)
            node.parent.rightChild = node2;
        else
            node.parent.leftChild = node2;
        node2.rightChild = node;
        node2.parent = node.parent;
        node.parent = node2;
        node2.color = node2.rightChild.color;
        setRed(node2.rightChild);
        return node2;
    }
    
    private Node<E> rotateWithRightChild(Node<E> node)
    {
        Node<E> node2 = node.rightChild;
        node.rightChild = node2.leftChild;
        if(node2.leftChild != null)
            node2.leftChild.parent = node;
        if(node.parent == null)
            root = node2;
        else if(node.parent.rightChild == node)
            node.parent.rightChild = node2;
        else
            node.parent.leftChild = node2;
        node2.leftChild = node;
        node2.parent = node.parent;
        node.parent = node2;
        node2.color = node2.leftChild.color;
        setRed(node2.leftChild);
        return node2;
    }
    
    private Node<E> doubleWithLeftChild(Node<E> node)
    {
        node.leftChild = rotateWithRightChild(node.leftChild);
        return rotateWithLeftChild(node);
    }
    
    private Node<E> doubleWithRightChild(Node<E> node)
    {
        node.rightChild = rotateWithLeftChild(node.rightChild);
        return rotateWithRightChild(node);
    }
    
    private boolean isBlack(Node <E> node)
    {
        return node == null || node.color == BLACK;
    }
    private boolean isRed(Node <E> node)
    {
        return node != null && node.color == RED;
    }
    private void setRed(Node<E> node)
    {
        if(node != null)
            node.color = RED;
            
    }
    private void setBlack(Node<E> node)
    {
        if(node != null)
            node.color = BLACK;
    }
}
