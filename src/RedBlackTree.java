/*
 * Name of the Student: Tiffany Do
 * Class: CS 3345
 * Section: 001
 * Semester: Fall 2017
 * Project 3: RedBlackTree that performs operations on a tree with a comparable element. Follows the structure of a red black tree.
 */

/**
 *
 * @author Tiffany Do
 * @param <E>
 */
public class RedBlackTree <E extends Comparable<E>>
{
    /**
    * Colors of nodes are either red or black
    */
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    /**
    * Node that is the root of the tree
    */
    private Node<E> root;
    
    /** 
    * Inserts a new node into the tree given a key and readjusts tree as needed.
    * 
    * @param element must be a comparable generic
    * @return true if a node has been inserted into the tree
    * 
    * @exception NullPointerException if the given element is null
    */
    public boolean insert(E element) throws NullPointerException
    {
        if(element == null)
            throw new NullPointerException("Error: Element is null");
        //tree is empty, make a new root
        if(root == null)
        {
            root = new Node(element, BLACK);
            return true;
        }
        //tree is not empty, create two nodes to traverse the tree and a node to add
        Node<E> current = root;
        Node<E> beforeCurrent = current;
        Node<E> add = new Node(element, RED);
        //continue traversing tree until a null element (leaf node) is reached
        while(current != null)
        {
            beforeCurrent = current;
            //compare element to the current node
            switch (current.element.compareTo(add.element)) 
            {
                //current is the same as element being added (duplicate), returns false
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
        //if the node before the null is greater
        if(beforeCurrent.element.compareTo(add.element) == 1)
            beforeCurrent.leftChild = add;
        else
            beforeCurrent.rightChild = add;
        //rebalance tree
        add.parent = beforeCurrent;
        rebalance(add);
        return true;
    }
    
    //private helper method to rebalance the tree
    private void rebalance(Node<E> node)
    {
        //set the added node to red
        node.color = RED;
        //Check to see if the insertion created a double red
        if(node != root && node.parent.color == RED)
        {
            //Check if uncle is black and rotates with grandparent if needed
            if(isBlack(sibling(node.parent)))
            {
                //Case: Restructure left left, single rotation
                if(node.parent.parent.leftChild == node.parent && node.parent.leftChild == node)
                {
                    node = rotateWithLeftChild(node.parent.parent);
                }
                //Case: Restructure left right, double rotation
                else if(node.parent.parent.leftChild == node.parent && node.parent.rightChild == node)
                {
                    node = doubleWithLeftChild(node.parent.parent);
                }
                //Case: Restructure right right, single rotation
                else if(node.parent.parent.rightChild == node.parent && node.parent.rightChild == node)
                {
                    node = rotateWithRightChild(node.parent.parent);
                }
                //Case: Restructure right left, double rotation
                else if(node.parent.parent.rightChild == node.parent && node.parent.leftChild == node)
                {
                    node = doubleWithRightChild(node.parent.parent);
                }
            }
            //Case:  //Case: double red with red uncle: recolor
            else if(isRed(sibling(node.parent)))
            {
                setBlack(node.parent);
                setBlack(sibling(node.parent));
                setRed(node.parent.parent);
                //check to see if double red has propogated to grandparent
                rebalance(node.parent.parent);
            }
        }
        //set the root to black
        if(node == root)
            root.color = BLACK;
    }
    
    /** 
    * Determines if a given element is in the tree.
    * 
    * @param object must be comparable generic that is comparable to elements within the tree
    * @return true if a node matching the key is found in the tree.
    */
    public boolean contains(Comparable<E> object)
    {
        //given object is null
        if(object == null)
            return false;
        //tree is empty, return false
        if(root == null)
            return false;
        else
        {
            //node to traverse the tree
            Node<E> current = root;
            while(current != null)
            {
                //checks key against node and also checks if the node is deleted
                if(object.compareTo(current.element) == 0)
                    return true;
                //otherwise continue traversing the tree by comparing key
                else if(object.compareTo(current.element) < 1)
                    current = current.leftChild;
                else
                    current = current.rightChild;
            }
        }
        return false;
    }
    
    /** 
    * Prints the tree in preorder by creating a string recursively
    */
    @Override
    public String toString()
    {
        String s = makePreorder("", root);
        return s;
    }
    
    //private helper method that creates a string of the tree in pre-order
    private String makePreorder(String s, Node<E> node)
    {
        String print = s;
        //if the node exists
        if(node != null )
        {
            //recurisvely add onto s
            print = makePreorder(print, node.leftChild);
            if(node.color == BLACK)
                print += node.element + "\t";
            else
                print += "*" + node.element + "\t";
            print = makePreorder(print, node.rightChild);
        }
        return print;
    }
    
    //private nested TreeNode class
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
    
    //private helper method to find the sibling of given node
    private Node<E> sibling(Node<E> node)
    {
        //error checks for null nodes
        if(node != null && node != root)
        {
            if(node.parent.leftChild != null)
            {
                //the node is the left child of the parent
                if(node.parent.leftChild.element.compareTo(node.element) == 0)
                {
                    //returns the right child of the parent if it exists
                    if(node.parent.rightChild != null)
                        return node.parent.rightChild;
                }
                //the node is the right child of the parent
                else
                {
                    //return the left child of the parent if it exists
                    if(node.parent.leftChild != null)
                        return node.parent.leftChild;
                }
            }
        }
        return null;
    }
    
    //private helper method to rotate node with left child (single rotation)
    private Node<E> rotateWithLeftChild(Node<E> node)
    {
        //single rotate nodes and update parent links
        Node<E> node2 = node.leftChild;
        node.leftChild = node2.rightChild;
        if(node2.rightChild != null)
            node2.rightChild.parent = node;
        if(node.parent == null)
            root = node2;
        //connect rotated root back to the tree
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
    
    //private helper method to rotate node with right child (single rotation)
    private Node<E> rotateWithRightChild(Node<E> node)
    {
        //single rotate nodes and update parent links
        Node<E> node2 = node.rightChild;
        node.rightChild = node2.leftChild;
        if(node2.leftChild != null)
            node2.leftChild.parent = node;
        if(node.parent == null)
            root = node2;
        //connect rotated root back to the tree
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
    
    //double rotate left child with its right child
    private Node<E> doubleWithLeftChild(Node<E> node)
    {
        node.leftChild = rotateWithRightChild(node.leftChild);
        return rotateWithLeftChild(node);
    }
    
    //double rotate right child with its left child
    private Node<E> doubleWithRightChild(Node<E> node)
    {
        node.rightChild = rotateWithLeftChild(node.rightChild);
        return rotateWithRightChild(node);
    }
    
    //methods to return if the Node is black or red. These are made to ensure that null nodes return as black (in the case of leaf nodes)
    private boolean isBlack(Node <E> node)
    {
        return node == null || node.color == BLACK;
    }
    private boolean isRed(Node <E> node)
    {
        return node != null && node.color == RED;
    }
    
    //methods to set colors of a Node. Error checks that the node is not null
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
