package ph.edu.dlsu.fx;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import ph.edu.dlsu.fx.ui.CustomMenuItem;
import ph.edu.dlsu.fx.ui.MenuHBox;
import ph.edu.dlsu.fx.utils.CustomFileChooser;
import ph.edu.dlsu.fx.utils.Utils;
import ph.edu.dlsu.fx.vision.ObjectDetector;


/**
 * Created by cobalt on 3/11/16.
 */
public class DemoScene extends BaseVideoScene {

    private final String defaultVideoPath = "res/video/GameofThronesTheme.mp4";
    private final String defaultCascadePath= "res/haarcascades/haarcascade_frontalface_alt.xml";
    private final String demoVideoPath1   = "res/video/bse_dr_joson.mp4";
    private final String demoVideoPath2   = "res/video/vid1left.mp4";
    private final String demoVideoPath3   = "res/video/BSECAMPAIGN.mp4";

    // Modes
    private static final int DEMO1  = 1;
    private static final int DEMO2  = 2;
    private static final int DEMO3  = 3;
    private static final int CUSTOM = 4;
    private int demoMode;

    // cascade classifier
    private ObjectDetector objectDetector = new ObjectDetector();

    // input
    private Mat mGray;

    @Override
    public boolean initializeCapture() {
        capture = new VideoCapture(0);
        return capture.isOpened();
    }

    @Override
    public Parent createContent() {

        // Create App Menu pane
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        // Initialize gray image
        mGray = new Mat((int)frameHeight, (int)frameWidth, CvType.CV_8UC1);

        // Initialize background image and load to Imageview
        ImageView imgBackground = Utils.loadImage2View("res/drawable/skyrim.jpg", displayWidth, displayHeight);
        if (imgBackground != null) {
            rootNode.getChildren().add(imgBackground);
        }

        currentFrame = Utils.loadImage2View("res/drawable/video.jpg", frameWidth, frameHeight);
        currentFrame.setTranslateX((displayWidth - frameWidth) / 2.0);
        currentFrame.setTranslateY(0);
        rootNode.getChildren().add(currentFrame);
        startCamera();

        // Create Menu title and content
        createHMenu();

        //
        initializeObjectDetector();

        // Add menu w/ title in the Pane
        rootNode.getChildren().add(menuBox);

        return rootNode;
    }

    private void initializeObjectDetector(){
        objectDetector.setCascadePath(defaultCascadePath);
    }


    @Override
    public void createHMenu() {
        final CustomMenuItem home   = new CustomMenuItem("HOME", menuWidth, menuHeight);
        final CustomMenuItem demo1  = new CustomMenuItem("DEMO1", menuWidth, menuHeight);
        final CustomMenuItem demo2  = new CustomMenuItem("DEMO2", menuWidth, menuHeight);
        final CustomMenuItem demo3  = new CustomMenuItem("DEMO3", menuWidth, menuHeight);
        final CustomMenuItem custom = new CustomMenuItem("CUSTOM", menuWidth, menuHeight);
        final CustomMenuItem exit   = new CustomMenuItem("EXIT", menuWidth, menuHeight);

        // handle menu events
        home.setOnMouseClicked(e -> {
            stopCamera();
            App.onHome();
        });

        demo1.setOnMouseClicked(e ->{
            demoMode = DEMO1;
            capture = new VideoCapture(demoVideoPath1);
        });

        demo2.setOnMouseClicked(e ->{
            demoMode = DEMO2;
            capture = new VideoCapture(demoVideoPath2);
        });

        demo3.setOnMouseClicked(e ->{
            demoMode = DEMO3;
            capture = new VideoCapture(demoVideoPath3);
        });

        custom.setOnMouseClicked(e ->{
            demoMode = CUSTOM;
            CustomFileChooser.chooseVideoAndXml();
            capture = new VideoCapture(CustomFileChooser.getVideoPath());
            objectDetector.setCascadePath(CustomFileChooser.getXmlPath());

        });


        exit.setOnMouseClicked(e -> {
            Boolean confirmQuit = App.onExit();
            if (confirmQuit) {
                stopCamera();
            }
        });

        menuBox = new MenuHBox(
                home,
                demo1,
                demo2,
                demo3,
                custom,
                exit);

        menuBox.setTranslateX((displayWidth - 6 * menuWidth) / 2.0);
        menuBox.setTranslateY(0);
    }

    @Override
    public void onCameraFrame(Mat frame) {
        Imgproc.cvtColor(frame, mGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(mGray, mGray);
        objectDetector.detectAndDisplay(frame);
    }


    @Override
    public void stopCamera(){
        if (mGray != null) {
            mGray.release();
        }
        super.stopCamera();
    }
}
