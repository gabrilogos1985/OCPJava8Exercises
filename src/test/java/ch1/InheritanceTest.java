package ch1;

import lombok.Data;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class InheritanceTest {
    @Data
    public static class JavaBeanParent {
        private Boolean aged;
        private boolean old;
        protected String protegido;
    }

    @Test
    public void isAged() {
        JavaBeanParent trueBean = new JavaBeanParent();
        trueBean.setAged(true);
        Assert.assertThat(trueBean.getAged(), CoreMatchers.is(true));
        BeanSon falseBean = new BeanSon();
        falseBean.setAged(true);
        Assert.assertThat(((JavaBeanParent) falseBean).aged, CoreMatchers.is(true));
        Assert.assertThat(((JavaBeanParent) falseBean).getAged(), CoreMatchers.is(false));
        Assert.assertThat(falseBean.getAged(), CoreMatchers.is(false));
    }
}


class BeanSon extends InheritanceTest.JavaBeanParent {
//Does not compile
    //    @Override
//    public Boolean isAged() {
//        return super.aged;
//    }

    @Override
    public Boolean getAged() {
        return false;
    }
}
