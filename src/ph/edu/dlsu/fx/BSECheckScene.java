package ph.edu.dlsu.fx;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import ph.edu.dlsu.fx.ui.CustomMenuItem;
import ph.edu.dlsu.fx.ui.MenuHBox;
import ph.edu.dlsu.fx.utils.Utils;


/**
 * Created by cobalt on 4/8/16.
 */
public class BSECheckScene extends BaseOpenniScene {

    final static int CV_CAP_OPENNI_DEPTH_MAP = 0; // Depth values in mm (CV_16UC1)
    final static int CV_CAP_OPENNI_POINT_CLOUD_MAP = 1; // XYZ in meters (CV_32FC3)
    final static int CV_CAP_OPENNI_DISPARITY_MAP = 2; // Disparity in pixels (CV_8UC1)
    final static int CV_CAP_OPENNI_DISPARITY_MAP_32F = 3; // Disparity in pixels (CV_32FC1)
    final static int CV_CAP_OPENNI_VALID_DEPTH_MASK = 4; // CV_8UC1
    final static int CV_CAP_OPENNI_BGR_IMAGE = 5;
    final static int CV_CAP_OPENNI_GRAY_IMAGE = 6;
    private int OUTPUT_MODE = CV_CAP_OPENNI_BGR_IMAGE; // default mode
    private double scaleFactor = 0.05;
    private Mat mIntermediate;


    @Override
    public Parent createContent() {

        System.out.println(Core.getBuildInformation());

        // Initialize intermediate matrix
        mIntermediate = new Mat();

        // Create Main Menu pane
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        // Initialize background image and load to Imageview
        ImageView imgBackground = Utils.loadImage2View("res/drawable/lightpink.jpg", displayWidth, displayHeight);
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

        // Add menu w/ title in the Pane
        rootNode.getChildren().add(menuBox);

        return rootNode;
    }

    @Override
    public void createHMenu() {
        final CustomMenuItem homeMenu = new CustomMenuItem("HOME", menuWidth, menuHeight);
        final CustomMenuItem rgbMenu = new CustomMenuItem("RGB", menuWidth, menuHeight);
        final CustomMenuItem grayMenu = new CustomMenuItem("GRAY", menuWidth, menuHeight);
        final CustomMenuItem depthMenu = new CustomMenuItem("DEPTH", menuWidth, menuHeight);
        final CustomMenuItem disparityMenu = new CustomMenuItem("DISPARITY", menuWidth, menuHeight);
        final CustomMenuItem exit = new CustomMenuItem("EXIT", menuWidth, menuHeight);

        // handle menu events
        homeMenu.setOnMouseClicked(e -> {
            stopCamera();
            App.onHome();
        });

        rgbMenu.setOnMouseClicked(e -> {
            OUTPUT_MODE = CV_CAP_OPENNI_BGR_IMAGE;
        });

        grayMenu.setOnMouseClicked(e -> {
            OUTPUT_MODE = CV_CAP_OPENNI_GRAY_IMAGE;
        });

        depthMenu.setOnMouseClicked(e ->
        {
            OUTPUT_MODE = CV_CAP_OPENNI_DEPTH_MAP;
        });


        disparityMenu.setOnMouseClicked(e ->
        {
            //OUTPUT_MODE = CV_CAP_OPENNI_DISPARITY_MAP;
            OUTPUT_MODE = CV_CAP_OPENNI_DISPARITY_MAP_32F;
        });

        exit.setOnMouseClicked(e -> {
            Boolean confirmQuit = App.onExit();
            if (confirmQuit) {
                stopCamera();
            }

        });

        menuBox = new MenuHBox(
                homeMenu,
                rgbMenu,
                grayMenu,
                depthMenu,
                disparityMenu,
                exit);

        menuBox.setTranslateX((displayWidth - 6 * menuWidth) / 2.0);
        menuBox.setTranslateY(0);

    }

    @Override
    public void onCaptureFrame(Mat frame) {

        capture.retrieve(frame, OUTPUT_MODE);

        if (OUTPUT_MODE == CV_CAP_OPENNI_DEPTH_MAP) {
            applyColor(frame);
        }

        if (OUTPUT_MODE == CV_CAP_OPENNI_DISPARITY_MAP_32F) {
            applyColor(frame);
        }
    }

    private void applyColor(Mat frame){
        Core.MinMaxLocResult minMax = Core.minMaxLoc(frame);
        double min = minMax.minVal;
        double max = minMax.maxVal;

        double scale = 255 / (max-min);

        frame.convertTo(mIntermediate, CvType.CV_8UC1, scale, -min*scale);
        Imgproc.applyColorMap(mIntermediate, frame, Imgproc.COLORMAP_JET);
    }

    @Override
    public void stopCamera() {
        if (mIntermediate != null) {
            mIntermediate.release();
        }
        super.stopCamera();
    }
}
