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
public class RabbitMQPublishSubscribeTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQPublishSubscribeTest.class);

    private final static String QUEUE_NAME1="test_subscribe_queue1";

    private final static String QUEUE_NAME2="test_subscribe_queue2";

    private final static String EXCHANGE_NAME="test_exchange";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public  void testSend() throws IOException, TimeoutException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道
        Channel channel = connection.createChannel();

        //声明Exchange
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //消息内容
        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
        log.info("RabbitMQ send {}",message);

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

        //绑定队列到exchange
        channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"");

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

        //绑定队列到exchange
        channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"");

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
