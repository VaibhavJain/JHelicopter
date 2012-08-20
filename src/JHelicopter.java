import java.util.Random; 

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.sun.javafx.scene.paint.ImagePattern;

/**
 * @author Anubhav Jain
 */

public class JHelicopter extends Application {

	public static int width = 900;
	public static int hurdleWidth = 50;
	public static int height = 500;
	public static final Random RANDOM = new Random(System.currentTimeMillis());
	public static int helicopterDownSpeed = 50;
	public static int helicopterKeyHitSpeed = 100;
	public static int helicopterKeyHitTranslate = 8;
	public static int wallBlockHeight = 100;
	public static int initHelecopterX = 100;
	public static int initHelecopterY = 200;
	public static int helicopterWidth = 75;
	public static int helicopterHeigth = 40;
	public static int wallHurdelTranslateX = 5;
	public static int wallHurdelSpeed = 30;
	public static long startTime;
	public static long endTime;
	
	public static Stage stage;
	
	public final static AudioClip loopAudio =
            new AudioClip(JHelicopter.class.getResource("audio/loop.wav").toString());
	public final static AudioClip crashAudio =
            new AudioClip(JHelicopter.class.getResource("audio/crash.wav").toString());
	
	
	final static Paint fillColor = new ImagePattern(new Image(Wall.class.getResource("pattern.jpg").toExternalForm())
	,0,0,50,10,false);
	
	public static void main(String[] args) {
		Application.launch(JHelicopter.class, args);
	}

	@Override
	public void start(final Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle("JHelicopter");
		primaryStage.setResizable(false);
		final Group root = new Group();
		Scene scene = new Scene(root, width, height, Color.BLACK);

		Helicopter helicopter = Helicopter.getInstance();
		
		root.getChildren().add(helicopter);
		final Hurdles hurdles = new Hurdles(width);
		root.getChildren().addAll(hurdles);
		
		HelicopterEventHandler handler = HelicopterEventHandler.getInstance();

		scene.setOnKeyPressed(handler);
		scene.setOnKeyReleased(handler);

		//scene.setCamera(new ParallelCamera());
		primaryStage.setScene(scene);

		primaryStage.show();
		
		startModal();
	}
	
	public static void startModal(){
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(
				HBoxBuilder
						.create()
						.styleClass("modal-dialog")
						.children(
								ImageViewBuilder
										.create()
										.fitHeight(85)
										.fitHeight(85)
										.cursor(Cursor.DISAPPEAR)
										.preserveRatio(true)
										.styleClass("modal-image")
										.image(new Image(
												JHelicopter.class.getResourceAsStream(
														"monu.jpg"))).build(),
								LabelBuilder
										.create()
										.text("Welcome to JavaFX Helicopter. This is a learning \n project developed by Anubhav Jain")
										.build(),
								ButtonBuilder
										.create()
										.text("Start")
										.defaultButton(true)
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent actionEvent) {
														// take action and close
														// getLudoInst();
														Hurdles.start();
														// the dialog.
														stage
																.getScene()
																.getRoot()
																.setEffect(null);
														dialog.close();
													}
												}).build(),
								ButtonBuilder
										.create()
										.text("Exit")
										.cancelButton(true)
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent actionEvent) {

														// close the dialog.
														stage
																.getScene()
																.getRoot()
																.setEffect(null);
														dialog.close();
														stage.close();
													}
												}).build()).build(),
				Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());

		// allow the dialog to be dragged around.
		final Node rootDialog = dialog.getScene().getRoot();
		final Delta dragDelta = new Delta(0, 0);
		rootDialog.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = (new Double(dialog.getX()
						- mouseEvent.getScreenX())).intValue();
				dragDelta.y = (new Double(dialog.getY()
						- mouseEvent.getScreenY())).intValue();
			}
		});
		rootDialog.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
				dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
			}
		});

		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
		
	}
	
	public static void finalModal(){
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		long time = JHelicopter.endTime - JHelicopter.startTime;
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(
				HBoxBuilder
						.create()
						.styleClass("modal-dialog")
						.children(
								LabelBuilder
										.create()
										.text("You have scored " + time + " runs")
										.build(),
								ButtonBuilder
										.create()
										.text("Replay")
										.defaultButton(true)
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent actionEvent) {
														// take action and close
														// getLudoInst();
														reset();
														// the dialog.
														stage
																.getScene()
																.getRoot()
																.setEffect(null);
														dialog.close();
													}
												}).build(),
								ButtonBuilder
										.create()
										.text("Exit")
										.cancelButton(true)
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent actionEvent) {

														// close the dialog.
														stage
																.getScene()
																.getRoot()
																.setEffect(null);
														dialog.close();
														stage.close();
													}
												}).build()).build(),
				Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());
		
		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
	}

	public static void pauseModal(){
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(
				HBoxBuilder
						.create()
						.styleClass("modal-dialog")
						.children(
								LabelBuilder
										.create()
										.text("Hit enter to resume ")
										.build(),
								ButtonBuilder
										.create()
										.text("Resume")
										.defaultButton(true)
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent actionEvent) {
														// take action and close
														// getLudoInst();
														Hurdles.resumeAll();
														// the dialog.
														stage
																.getScene()
																.getRoot()
																.setEffect(null);
														dialog.close();
													}
												}).build()
								).build(),
				Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());
		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
	}

	
	public static void reset(){
		final Group root = new Group();
		Scene scene = stage.getScene();
		Helicopter helicopter = Helicopter.reset();
		
		root.getChildren().add(helicopter);
		
		final Hurdles hurdles = new Hurdles(width);
		root.getChildren().addAll(hurdles);
		
		HelicopterEventHandler handler = HelicopterEventHandler.reset();
		
		//stage.show();
		scene.setOnKeyPressed(handler);
		scene.setOnKeyReleased(handler);
		
		scene.setRoot(root);
		//startModal();
		Hurdles.start();
	}
	
}
