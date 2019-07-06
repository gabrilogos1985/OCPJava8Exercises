package ch3;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class QueueListTest
{
    @Test
    public void linkListQueue() {
        LinkedList<Object> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        Assert.assertEquals(linkedList.poll(), 1);
        Assert.assertEquals(linkedList.size(), 1);
        Assert.assertEquals(linkedList.peek(), 2);
        Assert.assertEquals(linkedList.size(), 1);
        Assert.assertEquals(linkedList.pop(), 2);
        Assert.assertEquals(linkedList.size(), 0);
        Assert.assertEquals(linkedList.poll(), null);
    }


    @Test
    public void linkDeQueue() {
        ArrayDeque<Object> deque = new ArrayDeque<>();
        deque.add(1);
        deque.add(2);
        Assert.assertEquals(deque.poll(), 1);
        Assert.assertEquals(deque.size(), 1);
        Assert.assertEquals(deque.peek(), 2);
        Assert.assertEquals(deque.size(), 1);
        Assert.assertEquals(deque.pop(), 2);
        Assert.assertEquals(deque.size(), 0);
        Assert.assertEquals(deque.poll(), null);
    }
}
