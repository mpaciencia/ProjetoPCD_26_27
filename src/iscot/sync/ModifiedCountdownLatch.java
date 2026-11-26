package iscot.sync;

public class ModifiedCountdownLatch {
    private int count;

    public ModifiedCountdownLatch(int count) {
        if(count < 0) {
            throw new IllegalArgumentException("O contador não pode ser negativo");
        }

        this.count = count;
    }

    public synchronized void countdown() {
        if (count > 0) {
            count--;

            if (count == 0) {
                notifyAll();
            }
        }
    }

    public synchronized void await(int waitPeriod) throws InterruptedException {
        if (waitPeriod < 0) {
            throw new IllegalArgumentException("O tempo de espera não pode ser negativo");
        }

        long startTime = System.nanoTime();
        long timeoutNanos = waitPeriod * 1_000_000L;

        while (count > 0) {
            long elapsedTime = System.nanoTime() - startTime;
            long remainingNanos = timeoutNanos - elapsedTime;

            if (remainingNanos <= 0) {
                return;
            }

            long remainingMillis = remainingNanos / 1_000_000L;
            int remainingNanosParte = (int) (remainingNanos % 1_000_000L);

            wait(remainingMillis, remainingNanosParte);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ModifiedCountdownLatch latch = new ModifiedCountdownLatch(2);

        Thread waitingThread = new Thread(() -> {
            long startTime = System.currentTimeMillis();

            try {
                System.out.println("A thread vai esperar");
                latch.await(3000);

                long elapedMillis = System.currentTimeMillis() - startTime;
                System.out.println("A espera terminou após " + elapedMillis + "ms");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("A thread de espera foi interrompida");
            }
        });

        waitingThread.start();

        Thread.sleep(500);
        System.out.println("Primeiro countdown");
        latch.countdown();

        Thread.sleep(500);
        System.out.println("Segundo countdown");
        latch.countdown();

        waitingThread.join();
    }
}
