package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    private JButton action;
    private int countDeadSnakes;
    private int worstSnake;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        app=this;
        countDeadSnakes = 0;
        worstSnake = -1;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        frame.add(board,BorderLayout.CENTER);
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        action = new JButton("Start");
        actionsBPabel.add(action);
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        actions();
    }

    private void actions(){
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (action.getText().equals(("Start"))){
                    for (int i = 0; i != MAX_THREADS; i++) {
                        thread[i].start();
                    }
                    action.setText("Pause");
                }
                else if(action.getText().equals("Pause")){
                    for (Snake s: snakes){
                        s.pause();
                    }
                    action.setText("Resume");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    pauseReport();
                }
                else if(action.getText().equals("Resume")){
                    for (Snake s: snakes){
                        s.resume();
                    }
                    action.setText("Pause");
                }
            }
        });
    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        for (int i = 0; i != MAX_THREADS; i++) {
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
        }
        frame.setVisible(true);

        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
    }

    public synchronized void addDeadSnake(Snake s){
        countDeadSnakes++;
        if (worstSnake < 0){
            for (int i=0; i<snakes.length; i++){
                if (snakes[i].equals(s)){
                    worstSnake = i; break;
                }
            }
        }
        if(countDeadSnakes == MAX_THREADS){
            System.out.println("Thread (snake) status:");
            for (int i = 0; i != MAX_THREADS; i++) {
                System.out.println("["+i+"] :"+thread[i].getState());
            }
        }
    }

    public static SnakeApp getApp() {
        return app;
    }

    private void pauseReport(){
        System.out.println("===========================================");
        System.out.println("           INFORME DE PAUSA");
        System.out.println("===========================================");
        System.out.println(" -La serpiente más larga es la número: "+ findLongestSnake());
        if (worstSnake < 0){
            System.out.println(" -Aún no ha muerto ninguna serpiente");
        }else{
            System.out.println(" -La peor serpiente es la número: "+ worstSnake);
        }
        System.out.println("===========================================");


    }

    private int findLongestSnake(){
        int max = 0;
        for (int i=1; i<snakes.length; i++){
            if(snakes[i].getBody().size() > snakes[max].getBody().size()){
                max = i;
            }
        }
        return max;
    }
}
