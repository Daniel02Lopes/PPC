package library;

import KnapsackGA.BootstrapMessage;
import KnapsackGA.Individual;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Actor extends Thread {
    private static final int N_GENERATIONS = 2;
    private static final int POP_SIZE = 100000;
    private static final double PROB_MUTATION = 0.5;
    private static final int TOURNAMENT_SIZE = 3;
    private Random r = new Random();
    private Individual[] population = new Individual[POP_SIZE];

    public Address getSupervisor() {
        return supervisor;
    }

    private Address supervisor;

    public Actor() {
        this(null);
    }

    public Actor(Address supervisor) {
        this.supervisor = supervisor;
        this.start();
    }

    ConcurrentLinkedQueue<Message> mailbox = new ConcurrentLinkedQueue<>();

    public Address getAddress() {
        return (Message m) -> {
            mailbox.add(m);
        };
    }

    public void run() {
        while (true) {
            Message m = mailbox.poll();
            if (m == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if (m instanceof ExceptionalMessage m2 ) {
                if (handleOrDie(m2.getException())) {
                    this.getAddress().sendMessage(new SystemKillMessage());
                }
            } else {
                try {
                    handleMessage(m);
                } catch (Exception e) {
                    if (handleOrDie(e)) {
                        this.getAddress().sendMessage(new SystemKillMessage());
                    }
                }
                if (m instanceof SystemKillMessage)  {
                    return;
                }
            }
        }
    }

    private boolean handleOrDie(Exception e) {
        boolean r = handleException(e);
        if (!r) {
            if (supervisor != null) {
                supervisor.sendMessage(new ExceptionalMessage(e));
            }
            return true;
        } else {
            return false;
        }
    }

    protected abstract void handleMessage(Message m);

    protected boolean handleException(Exception e) {
        return false;
    }

    public Random getR() {
        return r;
    }
    public Individual[] getPopulation() {
        return population;
    }

    public static int getnGenerations() {
        return N_GENERATIONS;
    }

    public static int getPopSize() {
        return POP_SIZE;
    }

    public static double getProbMutation() {
        return PROB_MUTATION;
    }

    public static int getTournamentSize() {
        return TOURNAMENT_SIZE;
    }
}
