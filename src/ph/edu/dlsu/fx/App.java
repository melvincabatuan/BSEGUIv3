package ph.edu.dlsu.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.opencv.core.Core;
import ph.edu.dlsu.fx.ui.CustomMenuItem;
import ph.edu.dlsu.fx.ui.MenuTitle;
import ph.edu.dlsu.fx.ui.MenuVBox;
import ph.edu.dlsu.fx.utils.*;

import java.nio.file.Paths;

public class App extends Application {

    private static final String introFilePath = "/home/cobalt/IdeaProjects/BSEGUIv3/res/video/FINAL Infovideo for BSE Campaign.mp4";
    private static final String demoFilePath = "/home/cobalt/IdeaProjects/BSEGUIv3/res/video/vid1left.mp4";
    private static final String tutorialFilePath = "/home/cobalt/IdeaProjects/BSEGUIv3/res/video/bse_dr_joson.mp4";
    private static final String WINDOW_TITLE = "BSE APPLICATION -- Alpha Version";
    public static final String MENU_TITLE    = "    BSE MENU";

    // Window size
    private static double displayWidth;
    private static double displayHeight;

    // App stage
    static Stage stage;

    // Main Menu
    MenuVBox menuBox;

    // Scene
    static Scene menuScene;


    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeScreenSize();
        menuScene = new Scene(createHomeContent());
        stage = primaryStage;
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(menuScene);
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.show();
    }

    private void initializeScreenSize() {
        displayWidth = ScreenSize.getDisplayWidth();
        displayHeight = ScreenSize.getDisplayHeight();
    }


    // Create content for the Main Menu scene
    private Parent createHomeContent() {

        // Create Main Menu pane
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        // Initialize background image and load to Imageview
        ImageView imgBackground = Utils.loadImage2View("res/drawable/lightpink.jpg", displayWidth, displayHeight);
        if (imgBackground != null) {
            rootNode.getChildren().add(imgBackground);
        }

        // Create Menu title and content
        MenuTitle title = new MenuTitle(MENU_TITLE);
        title.setTranslateX(20);
        title.setTranslateY(200);
        createVMenu();


        // Add menu w/ title in the Pane
        rootNode.getChildren().addAll(title, menuBox);
        return rootNode;
    }


    private void createVMenu() {

        final CustomMenuItem intro =    new CustomMenuItem("INTRO");
        final CustomMenuItem facts =    new CustomMenuItem("FACTS");
        final CustomMenuItem demo =    new CustomMenuItem("DEMO");
        final CustomMenuItem tutorial = new CustomMenuItem("TUTORIAL");
        final CustomMenuItem visual =   new CustomMenuItem("VISUAL");
        final CustomMenuItem help =     new CustomMenuItem("HELP");
        final CustomMenuItem exit =     new CustomMenuItem("EXIT");

        // handle menu events
        intro.setOnMouseClicked(e -> onIntro());
        facts.setOnMouseClicked(e -> onFacts());
        demo.setOnMouseClicked(e-> onDemo());
        tutorial.setOnMouseClicked(e -> onTutorial());
        visual.setOnMouseClicked(e -> onVisual());
        help.setOnMouseClicked(e -> onHelp());
        exit.setOnMouseClicked(e -> onExit());

        menuBox = new MenuVBox(
                intro,
                facts,
                demo,
                tutorial,
                visual,
                help,
                exit);

        menuBox.setTranslateX(50);
        menuBox.setTranslateY(300);
    }



    // HOME Menu
    public static void onHome() {
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(menuScene);
    }

    // INTRO Menu
    public static void onIntro() {
        try {
            String url = Paths.get(introFilePath).toUri().toURL().toString(); // better for cross-platform
            //String url = "file://" + introFilePath;
            VideoBox.show(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // FACTS Menu
    public static void onFacts() {
        ImageBox.show();
    }


    private void onDemo() {

        DemoScene startScene = new DemoScene();
        stage.setScene(new Scene(startScene.createContent(), displayWidth, displayHeight));

//        try {
//            String url = Paths.get(demoFilePath).toUri().toURL().toString();
//            VideoBox.show(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    // TUTORIAL Menu
    public static void onTutorial() {
        try {
            String url = Paths.get(tutorialFilePath).toUri().toURL().toString();
            VideoBox.show(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onVisual() {
        BSECheckScene bseScene = new BSECheckScene();
        stage.setScene(new Scene(bseScene.createContent(), displayWidth, displayHeight));
    }


    private void onTactile() {
        FactsScene factScene = new FactsScene();
        stage.setScene(new Scene(factScene.createContent(), displayWidth, displayHeight));
    }


    private void onHelp() {

    }


    // EXIT Menu
    public static boolean onExit() {
        boolean confirmQuit = ConfirmationBox.show(
                "Are you sure you want to quit?",
                "Yes", "No");
        if (confirmQuit) {
            // Perform cleanup tasks here
            Platform.exit();
        }
        return confirmQuit;
    }


    // Load OpenCV in main()
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // OpenCV
        launch(args);
    }
}