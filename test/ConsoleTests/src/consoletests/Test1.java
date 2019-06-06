/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoletests;

/**
 *
 * @author soura
 */
public class Test1 {
    void showMethodName() {
        Exception e = new Exception();
        e.fillInStackTrace();
        for(int i=0; i<e.getStackTrace().length; i++) {
//            System.out.printf("%d: %s \n", i+1, e.getStackTrace()[i].getMethodName());
            System.out.printf("%d: %s \n", i+1, e.getStackTrace()[i]);
        }
    }
}
