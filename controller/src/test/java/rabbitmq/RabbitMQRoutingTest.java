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
 * description : Publish/Subscribe订阅模式
 * version : 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybaitsApplication.class)
public class RabbitMQRoutingTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQRoutingTest.class);

    private final static String QUEUE_NAME1="test_routing_queue1";

    private final static String QUEUE_NAME2="test_routing_queue2";

    private final static String EXCHANGE_NAME="test_exchange_routing";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public  void testSend() throws IOException, TimeoutException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道
        Channel channel = connection.createChannel();

        //声明Exchange,指定交换机的类型为direct
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        //info类型消息
        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes());
        log.info("RabbitMQ send info {}",message);

        //error类型消息
        message = "Has Error!";
        channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes());
        log.info("RabbitMQ send error {}",message);

        //warning类型消息
        message = "Has Warning!";
        channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes());
        log.info("RabbitMQ send warning {}",message);

        //关闭通道和链接
        channel.close();
        connection.close();

    }

    /**
     * 第一个消费者
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @Test
    public  void testRecive1() throws IOException, TimeoutException, InterruptedException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME1,false,false,false,null);

        //绑定队列到exchange，并指定路由key
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"info");
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"warning");

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //监听队列,手动返回完成
        channel.basicConsume(QUEUE_NAME1,false,consumer);

        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            log.info("RabbitMQ[consumer1] getMessage: {}",message);

            Thread.sleep(10);
            //返回确认状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }

    /**
     * 第二个消费者
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    @Test
    public  void testRecive2() throws IOException, TimeoutException, InterruptedException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME2,false,false,false,null);

        //绑定队列到exchange，并指定路由key
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"error");

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //监听队列,手动返回完成
        channel.basicConsume(QUEUE_NAME2,false,consumer);

        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            log.info("RabbitMQ[consumer2] getMessage: {}",message);

            Thread.sleep(10);
            //返回确认状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }
}
