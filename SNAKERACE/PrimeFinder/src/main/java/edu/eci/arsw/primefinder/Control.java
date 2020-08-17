/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import javax.swing.Timer;
import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {

    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];

    private boolean paused = false;
    Timer timer;

    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }

    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(PrimeFinderThread t: pft){
            t.start();
        }
        while (isRunning()){
            try {
                sleep(TMILISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(PrimeFinderThread t: pft){
                t.pause();
            }
            try {
                sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextReport();
        }
        System.out.println("==================================================");
        System.out.println("=              NO HAY MÁS REPORTES               =");
        System.out.println("= El programa ha finalizado                      =");
        System.out.println("==================================================");
    }

    private void nextReport(){
        System.out.println("==================================================");
        System.out.println("=              FIN DEL REPORTE                   =");
        System.out.println("= Presione Enter para ver el siguiente           =");
        System.out.println("==================================================");
        Scanner s = new Scanner(System.in);
        String key = s.nextLine();
        while (!key.isEmpty()){
            System.out.println("==================================================");
            System.out.println("=             ENTRADA NO RECONOCIDA              =");
            System.out.println("= Presione Enter para ver el siguiente reporte   =");
            System.out.println("==================================================");
            s = new Scanner(System.in);
            key = s.nextLine();
        }
        for(PrimeFinderThread t: pft) {
            t.resumee();
        }
    }

    private boolean isRunning(){
        for (PrimeFinderThread t: pft){
            if (t.isAlive()){
                return true;
            }
        }
        return false;
    }

}
