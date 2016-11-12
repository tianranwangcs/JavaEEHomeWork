package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import dev.factory.BeanFactory;
import dev.factory.XMLBeanFactory;
import dev.resource.LocalFileResource;

public class BeanFactoryTest {
	@Test
	public void testBeanCreateAndGet() {
		
		LocalFileResource resource = new LocalFileResource("beans.xml");
		
		BeanFactory beanFactory = new XMLBeanFactory(resource);

		Boss boss =(Boss)beanFactory.getBean("Boss");
		assertEquals(boss.tostring(), "This Boss has a red car with 001, now he is in Office 824.");
	}

}
