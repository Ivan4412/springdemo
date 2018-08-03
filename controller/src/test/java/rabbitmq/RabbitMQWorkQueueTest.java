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
 * description : rabbitMQ work队列模式
 * version : 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybaitsApplication.class)
public class RabbitMQWorkQueueTest {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQWorkQueueTest.class);

    private final static String QUEUE_NAME="test_work_queue";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    public  void send() throws IOException, TimeoutException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);


        //发送信息
        for(int i=0;i<100;i++) {
            //消息内容
            String message = "Hello World!" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            log.info("rabbitmq[work queue] Send {}" ,message);

            try {
                Thread.sleep(50);//随着发送的信息越多而间隔越长
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //关闭通道和链接
        channel.close();
        connection.close();

    }


    @Test
    public  void test () throws IOException, TimeoutException, InterruptedException {

        Thread thread0 =  new Thread(){
            public void run(){
                try {
                    send();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

            }
        };
        thread0.start();

        //休眠一段时间后启动第一个消费者

        Thread.sleep(100);

        Thread thread1 =  new Thread(){
            public void run(){
                try {
                    receive1();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();

        //休眠一段时间后启动第二个消费者

        Thread.sleep(20);

        Thread thread2 = new Thread(){
            public void run(){
                try {
                    receive2();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread2.start();

        //主线程不关闭
        while(true){

        }
    }

    @Test
    public  void receive1() throws IOException, TimeoutException, InterruptedException {
        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        int Count = 0;// 统计收到的信息历史条数
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            log.info(" rabbitmq[consumer1] Received :{} now Received MessageSize: {}" , message ,++Count);
            //休眠10ms
            Thread.sleep(200);
            // 返回确认状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    @Test
    public  void receive2() throws IOException, TimeoutException, InterruptedException {

        //获取到连接以及mq通道
        Connection connection = connectionFactory.newConnection();

        //从连接中创建通道R
        Channel channel = connection.createChannel();

        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //创建队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        int Count = 0;// 统计收到的信息历史条数
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            log.info(" rabbitmq[consumer2] Received :{} now Received MessageSize: {}" , message ,++Count);
            //休眠10ms
            Thread.sleep(10);
            // 返回确认状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
