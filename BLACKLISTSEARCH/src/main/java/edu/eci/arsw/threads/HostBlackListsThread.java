package edu.eci.arsw.threads;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.LinkedList;

public class HostBlackListsThread extends Thread {

    private int firstServer;
    private int latestServer;
    private int occurrencesCount;
    private int checkedListsCount;
    private int blackListAlarmCount;
    private String ipaddress;
    private LinkedList<Integer> blackListOcurrences = new LinkedList<>();
    HostBlacklistsDataSourceFacade skds;


    public HostBlackListsThread(int firstServer, int latestServer, String ipaddress, int blackListAlarmCount, int occurrencesCount, int checkedListsCount, HostBlacklistsDataSourceFacade skds){
        this.firstServer=firstServer;
        this.latestServer=latestServer;
        this.ipaddress=ipaddress;
        this.blackListAlarmCount=blackListAlarmCount;
        this.occurrencesCount=occurrencesCount;
        this.checkedListsCount=checkedListsCount;
        this.skds=skds;
    }

    public void run(){
        for(int i=this.firstServer; i<=this.latestServer && occurrencesCount< blackListAlarmCount; i++){
            if(skds.isInBlackListServer(i,this.ipaddress)){
                occurrencesCount++;
                blackListOcurrences.add(i);
            }
        }
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }

    public int getCheckedListsCount() {
        return checkedListsCount;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}
