package rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.yjs.SpringbootMybaitsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * author : yjs
 * createTime : 2018/8/2
 * description : rabbitMQ简单队列模式
 * version : 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybaitsApplication.class)
public class RabbitMQSimpleQueueTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQSimpleQueueTest.class);

    private final static String QUEUE_NAME="test_queue";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public  void testSend() throws IOException, TimeoutException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //消息内容
        String message = "Hello World!";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

        log.info("RabbitMQ send {}",message);

        //关闭通道和链接
        channel.close();
        connection.close();

    }

    @Test
    public  void testRecive() throws IOException, TimeoutException, InterruptedException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);

        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            log.info("RabbitMQ getMessage: {}",message);
        }

    }
}
