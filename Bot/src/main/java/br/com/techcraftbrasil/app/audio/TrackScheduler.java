package br.com.techcraftbrasil.app.audio;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    private AudioPlayer player;
    private BlockingQueue<AudioTrack> queue; //queue
    public TrackScheduler(AudioPlayer player){
        this.player = player;
        queue = new LinkedBlockingDeque<>();
        System.out.println((char)((byte) 3));
    }

    /*

     */
    public void queue(AudioTrack track){
        //Só cai dentro do if se não tiver nenhuma musica tocando no momento
        //Retorna falso se nenhuma musica estiver tocando
        if(!this.player.startTrack(track, true)){
            this.queue.offer(track);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, com.sedmelluq.discord.lavaplayer.track.AudioTrack track, AudioTrackEndReason endReason) {
       //Verifica se o motivo da música parar e se pode começar a próxima
        if(endReason.mayStartNext){
           nextTrack();
       }
    }

    /*
        Inicia uma proxíma musica no gerenciador de audio (AudioPlayer) interropendo a anterior
     */
    private void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
    }
}
