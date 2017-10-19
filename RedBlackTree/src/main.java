
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chawp
 */
public class main {
    public static void main(String[] args)
    {
        RedBlackTree tree = new RedBlackTree();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < 20; i++)
        {
            int num = (int)(Math.random() * (0 - 50) + 50);
            if(!list.contains(num)){
                list.add(num);
                System.out.print(num + " ");
                tree.insert(num);
            }
        }
        System.out.println();
        System.out.println(tree);
    }
    
}
