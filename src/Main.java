import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        LinkedList<Integer> buffer = new LinkedList<>();
        int bufferSize = 5;
        Thread producer = new Thread(new Producer(buffer, bufferSize));
        Thread consumer = new Thread(new Consumer(buffer, bufferSize));
        producer.start();
        consumer.start();
    }

    public static class Producer implements Runnable {

        private final LinkedList<Integer> buffer;
        private final int bufferSize;

        public Producer(LinkedList<Integer> buffer, int bufferSize) {
            this.buffer = buffer;
            this.bufferSize = bufferSize;
        }

        public void run() {
            for (int i = 1; i <= 10; i++) {
                synchronized (buffer) {
                    while (buffer.size() == bufferSize) {
                        try {
                            System.out.println("Buffer is full. Producer is waiting...");
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Producer: produce " + i);
                    buffer.add(i);
                    buffer.notify();
                }
                try {
                    Thread.sleep((long) (Math.random() * 50));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer implements Runnable {

        private final LinkedList<Integer> buffer;
        private final int bufferSize;

        public Consumer(LinkedList<Integer> buffer, int bufferSize) {
            this.buffer = buffer;
            this.bufferSize = bufferSize;
        }

        public void run() {
            while (true) {
                synchronized (buffer) {
                    while (buffer.isEmpty()) {
                        try {
                            System.out.println("Buffer is empty. Consumer is waiting...");
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int consumedValue = buffer.remove();
                    System.out.println("Consumer: consume " + consumedValue);
                    buffer.notify();
                }
                try {
                    Thread.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
