package cn.henu.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试spring和activemq整合的consumer
 * @author syw
 *
 */
public class MessageConsumer {

	
	@Test
	public void testConsumer()throws Exception {
		
		//初始化一个spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待
		System.in.read();
	}
	
}