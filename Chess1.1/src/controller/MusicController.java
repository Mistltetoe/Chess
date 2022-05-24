package controller;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicController {

    private AudioClip aau;
    private String url;
    private int N;


    public MusicController(String url){
        this.url=url;
        playMusic();
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public void playMusic(){
        try {
            URL cb;
            File f = new File(url);
            cb = f.toURL();
            aau = Applet.newAudioClip(cb);

        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
    }
    public void musicMain(int n) {
        switch (n) {
            case 1:
                aau.play();
                N = 1;
                break;
            case 2:
                aau.stop();
                N = 2;
                break;
            case 3:
                aau.loop();
                N = 3;
                break;
            default:
                break;
        }
    }
}
