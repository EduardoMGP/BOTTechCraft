package br.com.techcraftbrasil.app.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {

    private AudioPlayer player;
    private ByteBuffer buffer;
    private MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer player){
        this.player = player;
        //Aloca 1204 bytes onde será armazenado faixas de audio
        buffer = ByteBuffer.allocate(1024);
        frame = new MutableAudioFrame();
        frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.player.provide(this.frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return (ByteBuffer) ((Buffer) this.buffer).flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
