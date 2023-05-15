# 一、组件介绍

Rabbitmq消息中间件，主要用于服务之间的消息通信，SpringBoot为开发者提供了spring-boot-starter-amqp依赖包，发送端主要使用    RabbitTemplate进行消息的发送，消费端则使用了@RabbitListener来监听队列中消息的接收，用起来非常简单，但存在如下问题：

1. 在复杂的消息发送中，需要操作到信道（channel）级别，由于RabbitTemplate高度封装，反而使用起来不是很方便
2. rabbit主流的四种通信模式，如简单模式（simple）、广播模式（fanout）、路由模式（router）、主题模式（topic）在使用时，无论是发送端还是接收端的API的参数冗余过多，使用时需要进行填写默认值，一旦填错，不同的模式之间通信，会出现无法收到消息甚至抛出异常的情况
3. 由于spring-boot-starter-amqp提供的API高度封装，不利于进行编程设计，例如接收端如使用注解，很多信息是写死的

综上所述，有必要开发一款基于Rabbitmq基本使用的组件：bsy-cloud-common-rabbit

# 二、组件各模式使用
1、simple模式，不会重复消费。<br>
2、fanout模式，当队列名称与交换机名称不一致时，多消费者会同时消费。当队列名称与交换机名称一致时，则多个消费者监听同一条消息时，只有一个消费者进行消费该消息，不会重复消费。<br>
3、routing模式，无论队列名称与交换机名称是否一致，多消费者都会同时消费。<br>
4、topic模式，当队列名称与交换机名称不一致时，多消费者会同时消费。当队列名称与交换机名称一致时，则多个消费者监听同一条消息时，只有一个消费者进行消费该消息，不会重复消费。


# 三、组件使用介绍

bsy-cloud-common-rabbit使用时总结分为三块：

## 1. 配置yml文件

在yml中配置rabbitmq的链接信息，注意，由于没有使用spring-boot-starter-amqp依赖包，因此不要配置在spring下，要单独配置

```yml
# 消息队列 配置
rabbitmq:
  host: 127.0.0.1
  port: 5672
  username: guest
  password: guest
  #virtualHost代表虚拟空间，
  virtualHost: /bsy-dev-mq
```

## 2. 使用发送端

需要注入两个bean，分别为IProviderService和IRabbitChannelService，其实IRabbitChannelService没有必要注入使用，但由于后续有可能为信道进行命名，为统一起见，还是需要注入，使用时，按照四种模式调用接口即可，分别为

1. 简单模式：providerService.publishSimpleMessage
2. 广播模式：providerService.publishFanoutMessage
3. 路由模式：providerService.publishRoutingMessage
4. 主题模式：providerService.publishTopicMessage

注意每个方法最后一个参数传的是对象（只不过实例中传的是String对象），不用传byte[],方法中已经实现了序列化，实例代码如下：

```java
@Service
public class Provider {

    @Resource
    IProviderService providerService;

    @Resource
    IRabbitChannelService rabbitChannelService;

    public void sendSimpleMessage() {
        for (int i = 0; i <= 20; i++) {
            providerService.publishSimpleMessage(rabbitChannelService.getRabbitMqChannel(), "gaoheng-q1", "xxxxx");
        }

    }

    public void sendFanoutMessage() {
        for (int i = 0; i <= 20; i++) {
            providerService.publishFanoutMessage(rabbitChannelService.getRabbitMqChannel(), "exchange-gaoh", "xxxxx");
        }

    }

    public void sendRoutingMessage() {
        providerService.publishRoutingMessage(rabbitChannelService.getRabbitMqChannel(), "exchange-router", "user.info", "xxxx");
        providerService.publishRoutingMessage(rabbitChannelService.getRabbitMqChannel(), "exchange-router", "sys.info", "xxxxxx");
    }

    public void sendTopicMessage() {
        providerService.publishTopicMessage(rabbitChannelService.getRabbitMqChannel(), "exchange-topic", "user.info", "xxxxx");
        providerService.publishTopicMessage(rabbitChannelService.getRabbitMqChannel(), "exchange-topic", "sys.info", "xxxxxx");
    }

}
```

## 3. 使用接收端

由于接收端需要监听对应的消息，spring-boot-starter-amqp使用了监听器，本质上就是新起了一个线程进行监听，组件没有使用spring-boot-starter-amqp，因此需要单独起一个线程监听消息，步骤如下：

1. 在springBoot的启动类上开始异步线程@EnableAsync

   ```java
   @EnableAsync
   @SpringBootApplication
   public class NettyApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(NettyApplication.class, args);
       }
   }
   ```
   
     
2. 写一个消费者bean，实现CommandLineRunner接口并注入IConsumerService和IRabbitChannelService，并实现run方法，并在run方法上添加@Async，在run中调用IConsumerService的接收方法，springBoot主线程启动后，该bean会自动启动

   ```java
   @Slf4j
   @Component
   public class Customer implements CommandLineRunner {
   
       @Resource
       IConsumerService consumerService;
       @Resource
       IRabbitChannelService rabbitChannelService;
       @Async
       @Override
       public void run(String... args) {
           consumerService.consumTopicMessage(new AbstractConsumer() {
               @Override
               public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                   // 该处只能使用com.alibaba.fastjson进行解析byte数据, 否则可能会出现异常
                   log.info("hahahhahah==" + JSON.parseObject(bytes, String.class) + "----" + s+"==="+rabbitChannelService.getRabbitMqChannel("exchange-gaoh"));
               }
           },"exchange-topic", Arrays.asList("user.*"));
       }
   }
   ```
   
   

