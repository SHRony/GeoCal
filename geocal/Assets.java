package geocal;

import java.io.InputStream;
import javafx.scene.image.Image;

final class Assets {
    private Assets() {
    }

    static Image image(String path) {
        String name = path.startsWith("img/") ? path.substring(4) : path;
        InputStream stream = GeoCal.class.getResourceAsStream("/img/" + name);
        if (stream != null) {
            return new Image(stream);
        }
        return new Image(path);
    }

    static String stylesheet(String path) {
        String name = path.startsWith("img/") ? path.substring(4) : path;
        var url = GeoCal.class.getResource("/img/" + name);
        if (url != null) {
            return url.toExternalForm();
        }
        return path;
    }
}
