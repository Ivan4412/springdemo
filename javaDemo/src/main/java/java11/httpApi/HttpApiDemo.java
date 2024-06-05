package java11.httpApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * HttpApiDemo
 * 标准化和简化：
 * Java 11 中的 HTTP 客户端 API（位于 java.net.http 包）被正式标准化，为 Java 应用程序提供了现代、简洁且功能强大的 HTTP 客户端。
 * 该 API 取代了之前使用的 HttpURLConnection，提供了更直观、更易于使用的接口。
 * 主要类和接口：
 * HttpClient：用于发送 HTTP 请求和接收 HTTP 响应的客户端。
 * HttpRequest：表示 HTTP 请求，包含 URL、HTTP 方法（如 GET、POST）、请求头等信息。
 * HttpResponse：表示 HTTP 响应，包含响应状态码、响应头和响应体等信息。
 * 异步和同步支持：
 * HTTP 客户端 API 支持同步和异步请求。同步请求会阻塞调用线程，直到收到响应；而异步请求则允许调用线程在发送请求后继续执行其他任务，直到通过回调函数或 CompletableFuture 处理响应。
 * WebSocket 支持：
 * 除了 HTTP/1.1 和 HTTP/2 协议外，新的 HTTP 客户端 API 还支持 WebSocket，使得 Java 应用程序能够更方便地与 WebSocket 服务器进行通信。
 * 性能优化：
 * 新的 HTTP 客户端 API 在性能方面进行了优化，提供了与 Apache HttpClient 库、Netty 和 Jetty 等流行 HTTP 客户端库相当的性能。
 * @author yjs
 * @date 2024/6/5
 */
public class HttpApiDemo {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.baidu.com/"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.statusCode()); // 输出响应状态码
        System.out.println(response.body()); // 输出响应体
    }
}
