package imageControl;


import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class getImageFile {
    BufferedImage image;
    public File profile(String username) {
        new imageControlCfg();

        Path simple = Paths.get("simple.png");
        return new File(simple.toAbsolutePath().getParent()+
                imageControlCfg.profilePath+ username+ ".png");
    }
    public File twitte(String username) {
        new imageControlCfg();
        Path simple = Paths.get("simple.png");
        return new File(simple.toAbsolutePath().getParent()+
                imageControlCfg.twittePath+ username+ ".png");
    }
    public File message(String username) {
        new imageControlCfg();
        Path simple = Paths.get("simple.png");
        return new File(simple.toAbsolutePath().getParent()+
                imageControlCfg.messagePath+
                username+ ".png");
    }
}
