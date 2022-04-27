/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2022/4/2
 * @description :
 * 现象：如果while里面放try/catch，会导致中断失效
 * 原因：如果当前线程由于wait(),join(),sleep()等方法的调用而被阻塞，它的interrupt状态会被清除并且会接收一个InterruptedException。
 * <p>
 * 最佳实践1：catch了InterruptedExcetion之后的优先选择：在方法签名中抛出异常 那么在run()就会强制try/catch
 * <p>
 * 最佳实践2：在catch子语句中调用Thread.currentThread().interrupt()来恢复设置中断状态，以便于在后续的执行中，依然能够检查到刚才发生了中断回到刚才RightWayStopThreadInProd补上中断，让它跳出
 */
public class WrongWayToInterrupt extends Thread {

    public static void main(String[] args) {
        WrongWayToInterrupt wrongWayToStop = new WrongWayToInterrupt();

        System.out.println("线程启动");
        wrongWayToStop.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("中断线程");
        wrongWayToStop.interrupt();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("线程结束");
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            for (int i = 0; i < 10; i++) {
                System.out.println("线程在运行中" + i);//输出当前循环执行了多少次
                //错误方式：
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

         /*
                //最佳实践1：catch了InterruptedExcetion之后的优先选择：在方法签名中抛出异常 那么在run()就会强制try/catch
                try {
                    throwInMethod();
                } catch (InterruptedException e) {
                    //run方法不能抛出异常，只能catch。
                    //发出中断信号，给run方法获知，以便正常退出
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }



               // 最佳实践2：在catch子语句中调用Thread.currentThread().interrupt()来恢复设置中断状态，以便于在后续的执行中
                reInterrupt();
            */
            }
        } System.out.println("线程正确结束，执行收尾逻辑，如清理工作，关闭流或连接等");
    }

    private void throwInMethod() throws InterruptedException {
        //不捕获异常，上抛到Thread的run方法中去
        Thread.sleep(2000);
    }

    private void reInterrupt() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

