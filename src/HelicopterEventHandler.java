import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class HelicopterEventHandler implements EventHandler<KeyEvent> {

	public Timeline upTransition;
	public Timeline downTransition;
	public static HelicopterEventHandler handler;

	public static HelicopterEventHandler getInstance() {
		if (null == handler) {
			handler = new HelicopterEventHandler();
		}
		return handler;
	}

	public static HelicopterEventHandler reset() {
		handler = new HelicopterEventHandler();
		return handler;
	}

	public void stop() {
		upTransition.stop();
		downTransition.stop();
	}
	public void pause() {
		upTransition.pause();
		downTransition.pause();
	}

	private HelicopterEventHandler() {
		final Helicopter helicopter = Helicopter.getInstance();
		upTransition = new Timeline();
		downTransition = new Timeline();
		upTransition.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				helicopter.setTranslateY(helicopter.getTranslateY()
						- JHelicopter.helicopterKeyHitTranslate);
			}
		};

		KeyValue keyValueY = new KeyValue(helicopter.rotateProperty(), 0);
		KeyFrame keyFrame = new KeyFrame(
				Duration.millis(JHelicopter.helicopterKeyHitSpeed), onFinished,
				keyValueY);
		upTransition.getKeyFrames().add(keyFrame);

		downTransition.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> onFinishedDown = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				helicopter.setTranslateY(helicopter.getTranslateY()
						+ JHelicopter.helicopterKeyHitTranslate);
			}
		};

		KeyValue keyValueYDown = new KeyValue(helicopter.rotateProperty(), 0);
		KeyFrame keyFrameDown = new KeyFrame(
				Duration.millis(JHelicopter.helicopterKeyHitSpeed),
				onFinishedDown, keyValueYDown);
		downTransition.getKeyFrames().add(keyFrameDown);

	}

	@Override
	public void handle(KeyEvent event) {
		final Helicopter helicopter = Helicopter.getInstance();
		if (helicopter.anim.getStatus() != Animation.Status.STOPPED) {
			if (event.getEventType() == KeyEvent.KEY_RELEASED) {
				downTransition.pause();
				upTransition.pause();
				helicopter.anim.playFromStart();
			} else if (event.getCode() == KeyCode.UP) {
				if (helicopter.anim.getStatus() != Animation.Status.PAUSED) {
					helicopter.anim.pause();
				}
				if (downTransition.getStatus() == Animation.Status.RUNNING) {
					downTransition.pause();
				}
				if (upTransition.getStatus() != Animation.Status.RUNNING) {
					upTransition.playFromStart();
				}
			} else if (event.getCode() == KeyCode.DOWN) {
				if (helicopter.anim.getStatus() != Animation.Status.PAUSED) {
					helicopter.anim.pause();
				}
				if (upTransition.getStatus() == Animation.Status.RUNNING) {
					upTransition.pause();
				}
				if (downTransition.getStatus() != Animation.Status.RUNNING) {
					downTransition.playFromStart();
				}
			}else if (event.getCode() == KeyCode.SPACE) {
				Hurdles.pauseAll();
			}
		}
	}
}
