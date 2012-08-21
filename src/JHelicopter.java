import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
	public static boolean playAudio = true;

	public static Stage stage;

	public final static AudioClip loopAudio = new AudioClip(JHelicopter.class
			.getResource("audio/loop.wav").toString());
	public final static AudioClip crashAudio = new AudioClip(JHelicopter.class
			.getResource("audio/crash.wav").toString());

	final static Paint fillColor = new ImagePattern(new Image(Wall.class
			.getResource("pattern.jpg").toExternalForm()), 0, 0, 50, 10, false);

	public static void main(String[] args) {
		Application.launch(JHelicopter.class, args);
	}

	@Override
	public void start(final Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle("JHelicopter");
		primaryStage.setResizable(false);
		Group root = new Group();
		Scene scene = new Scene(root, width, height, Color.BLACK);

		Helicopter helicopter = Helicopter.getInstance();

		root.getChildren().add(helicopter);
		Hurdles hurdles = new Hurdles(width);
		root.getChildren().addAll(hurdles);

		HelicopterEventHandler handler = HelicopterEventHandler.getInstance();

		scene.setOnKeyPressed(handler);
		scene.setOnKeyReleased(handler);

		// scene.setCamera(new ParallelCamera());
		primaryStage.setScene(scene);

		primaryStage.show();

		startModal();
	}

	public static void startModal() {
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
												JHelicopter.class
														.getResourceAsStream("monu.jpg")))
										.build(),
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
														stage.getScene()
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
														stage.getScene()
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

	public static void finalModal() {
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		long time = JHelicopter.endTime - JHelicopter.startTime;

		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(HBoxBuilder
				.create()
				.styleClass("modal-dialog")
				.children(
						LabelBuilder.create()
								.text("You have scored " + time + " runs")
								.build(),
						ButtonBuilder.create().text("Replay")
								.defaultButton(true)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent actionEvent) {
										// take action and close
										// getLudoInst();
										reset();
										// the dialog.
										stage.getScene().getRoot()
												.setEffect(null);
										dialog.close();
									}
								}).build(),
						ButtonBuilder.create().text("Exit").cancelButton(true)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent actionEvent) {

										// close the dialog.
										stage.getScene().getRoot()
												.setEffect(null);
										dialog.close();
										stage.close();
									}
								}).build()).build(), Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());

		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
	}

	public static void pauseModal() {
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);

		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(HBoxBuilder
				.create()
				.styleClass("modal-dialog")
				.children(
						LabelBuilder.create().text("Hit enter to resume ")
								.build(),
						ButtonBuilder.create().text("Resume")
								.defaultButton(true)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent actionEvent) {
										// take action and close
										// getLudoInst();
										Hurdles.resumeAll();
										// the dialog.
										stage.getScene().getRoot()
												.setEffect(null);
										dialog.close();
									}
								}).build()).build(), Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());
		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
	}

	@SuppressWarnings("unchecked")
	public static void setting() {
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);

		final CheckBox cb = CheckBoxBuilder.create().text("Sound")
				.selected(playAudio).build();
		cb.selectedProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue,
					Object newValue) {
				// TODO Auto-generated method stub
				if (!cb.selectedProperty().getValue()) {
					playAudio = false;
					loopAudio.stop();
				}
			}
		});

		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setScene(new Scene(HBoxBuilder
				.create()
				.styleClass("modal-dialog")
				.children(
						LabelBuilder.create().text("Select Setting").build(),
						cb,
						ButtonBuilder.create().text("Done").defaultButton(true)
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent actionEvent) {
										// take action and close
										// getLudoInst();
										Hurdles.resumeAll();
										// the dialog.
										stage.getScene().getRoot()
												.setEffect(null);
										dialog.close();
									}
								}).build()).build(), Color.TRANSPARENT));
		dialog.getScene()
				.getStylesheets()
				.add(JHelicopter.class.getResource("modal-dialog.css")
						.toExternalForm());
		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();
	}

	public static void reset() {
		final Group root = new Group();
		Scene scene = stage.getScene();
		Helicopter helicopter = Helicopter.reset();

		root.getChildren().add(helicopter);

		Hurdles hurdles = new Hurdles(width);
		root.getChildren().addAll(hurdles);

		HelicopterEventHandler handler = HelicopterEventHandler.reset();

		// stage.show();
		scene.setOnKeyPressed(handler);
		scene.setOnKeyReleased(handler);

		scene.setRoot(root);
		// startModal();
		Hurdles.start();
	}

	public static void waitAcreen() {
		final Stage dialog = new Stage(StageStyle.TRANSPARENT);
		final Integer count = 0;
		final StringProperty string = new SimpleStringProperty();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		Group root = new Group();
		final Text text = TextBuilder.create().font(new Font(20)).build();
		text.textProperty().bind(string);

		Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
		dialog.setScene(scene);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent paramT) {
				// TODO Auto-generated method stub
				if(count < 3){
				ScaleTransition st = ScaleTransitionBuilder.create().node(text)
						.byX(5.0f).byY(5.0f).cycleCount(0)
						.duration(Duration.millis(50)).build();
				st.play();
				count = count + 1;
				}else{
					
				}
			}
		};

		Timeline anim = new Timeline();
		KeyValue keyValueX = new KeyValue(new SimpleIntegerProperty(0), 0);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(50), onFinished,
				keyValueX);
		anim.getKeyFrames().add(keyFrame);

		stage.getScene().getRoot().setEffect(new BoxBlur());
		dialog.show();

	}
}
