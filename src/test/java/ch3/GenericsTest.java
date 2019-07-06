package ch3;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GenericsTest {

    class A {}
    class B extends A{}
    class C extends B{}

    <X, Y> X doX(List<? extends Y> list) { // but no List<B extends Y> list
        return null;
    }

    <Z extends A> Z doA(List<? extends Z> list) { // but no List<? extends A> list
        return list.get(0);
    }

    public static void main(String ...args) {
        new GenericsTest();
        Helper2.<RuntimeException>printException(new NullPointerException ("D"));
    }

    void cantAddCouldAdd(List<? extends A> list, List<? super C> add) { // but no List<? extends A> list
        // list.add(new A()); can not ADD
        add.add(new C());
        // add.add(new A()); // It is greater than lower bound C
    }

    {
        doA(new ArrayList<C>(Arrays.asList(new C())));
        LinkedList<Object> objects = new LinkedList<>();
        cantAddCouldAdd(new ArrayList<C>(), objects);
        System.out.printf("No importa que agreges C %s para mi eres eres object\n", objects.get(0));
    }

    {
        System.out.println(new GenericaMinuscula<String>("Erik").saludame().toUpperCase());
    }

    @AllArgsConstructor
    public static class GenericaMinuscula<t>{
        t valor;

        t saludame() { return  valor;}

    }

    public static  class Helper2 {

        public static <U extends Exception> void printException(U u) {
            System.out.println(u.getMessage());
        }
    }
}
