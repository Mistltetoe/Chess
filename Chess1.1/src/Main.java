import view.LoginFrame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        LoginFrame startFrame = new LoginFrame();
        startFrame.setVisible(true);

//        try {
//            Clip bgm = AudioSystem.getClip();
//            InputStream is = Main.class.getClassLoader().getResourceAsStream("music/bgm.wav");
//            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
//            bgm.open(ais);
//            bgm.loop(Clip.LOOP_CONTINUOUSLY);
//        }
//        catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
