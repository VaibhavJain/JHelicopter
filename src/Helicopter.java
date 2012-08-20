import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.util.Duration;

public class Helicopter extends ImageView {

	public Timeline anim;
	private static Helicopter helicopter;

	public static Helicopter getInstance() {
		if (null == helicopter) {
			helicopter = new Helicopter();
		}
		return helicopter;
	}

	public static Helicopter reset() {
		helicopter = new Helicopter();
		return helicopter;
	}

	private Helicopter() {
		super();
		setImage(new Image(Helicopter.class.getResource("helicopter.png")
				.toExternalForm()));
		setFitHeight(JHelicopter.helicopterHeigth);
		setFitWidth(JHelicopter.helicopterWidth);
		setCache(true);
		setLayoutX(JHelicopter.initHelecopterX);
		setLayoutY(JHelicopter.initHelecopterY);
		anim = new Timeline();
		anim.setCycleCount(Timeline.INDEFINITE);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				setTranslateY(getTranslateY() + 5);
			}
		};

		KeyValue keyValueX = new KeyValue(Helicopter.this.rotateProperty(), 0);
		KeyFrame keyFrame = new KeyFrame(new Duration(
				JHelicopter.helicopterDownSpeed), onFinished, keyValueX); // ,
																			// keyValueY);
		anim.getKeyFrames().add(keyFrame);
	}

	public Circle getSmoke() {
		
		Circle smoke = CircleBuilder.create()
				.centerX(JHelicopter.initHelecopterX - 5)
				.centerY(helicopter.getLayoutY() + helicopter.getTranslateY())
				.radius(JHelicopter.wallHurdelTranslateX).fill(Color.DARKGRAY)
				.build();

		return smoke;
	}

	public void start() {
		anim.play();
	}

}
