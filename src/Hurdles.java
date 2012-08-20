import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.util.Duration;


class Hurdles extends Group {
		static Timeline anim = new Timeline();
		static Timeline wallAnim;
		static int range = JHelicopter.height - JHelicopter.wallBlockHeight - 100;

		public Hurdle[] getHurdle(List<Delta> layoutCordinate) {
			List<Hurdle> list = new ArrayList<Hurdle>();
			Hurdle[] temp = { new Hurdle() };
			for (Delta delta : layoutCordinate) {
				list.add(new Hurdle(delta.x, delta.y));
			}
			return list.toArray(temp);
		}

		public Node[] initStage(int width) {
			// generte start cordinate
			Random random = new Random(System.currentTimeMillis());
			List<Delta> layoutCordinate = new ArrayList<Delta>();
			layoutCordinate.add(new Delta(500, random.nextInt(range) + 50));
			layoutCordinate.add(new Delta(900, random.nextInt(range) + 50));
			return getHurdle(layoutCordinate);
		}

		public Hurdles(final int width) {
			// this.content = content;
			this.getChildren().addAll(initStage(width));
			wallAnim = getInitWall(this);
			anim.setCycleCount(Timeline.INDEFINITE);
			EventHandler<ActionEvent> onFinished = new OnFinished(this);
			KeyValue keyValueX = new KeyValue(this.rotateProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(JHelicopter.wallHurdelSpeed), onFinished,
					keyValueX);
			anim.getKeyFrames().add(keyFrame);
			
		}
		
		public Timeline getInitWall(final Group group) {

			int cutY = 0;
			int bottomY = 0;
			int length = JHelicopter.width / JHelicopter.hurdleWidth;
			int initialPos = JHelicopter.width % JHelicopter.hurdleWidth;
			List<Wall> list = new ArrayList<Wall>();
			Wall top = null, bottom = null;
			for (int i = 0; i < length; i++) {
				cutY = JHelicopter.RANDOM.nextInt(JHelicopter.wallBlockHeight);
				bottomY = JHelicopter.wallBlockHeight - cutY;
				top = new Wall(initialPos + i * JHelicopter.hurdleWidth, 0, cutY);
				bottom = new Wall(initialPos + i * JHelicopter.hurdleWidth, JHelicopter.height
						- bottomY, bottomY);
				list.add(top);
				list.add(bottom);
			}
			group.getChildren().addAll(list);
			Timeline anim = new Timeline();
			
			anim.setCycleCount(Timeline.INDEFINITE);
			EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					group.getChildren().addAll(Wall.getWall());
				}
			};
			int duration = (JHelicopter.hurdleWidth / JHelicopter.wallHurdelTranslateX) * JHelicopter.wallHurdelSpeed;
			
			KeyValue keyValueX = new KeyValue(this.rotateProperty(), 0);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), onFinished,
					keyValueX);
			anim.getKeyFrames().add(keyFrame);
			
			
			return anim;
		}
		
		public static void start(){
			JHelicopter.startTime = System.currentTimeMillis();
			wallAnim.play();
			anim.play();
			Helicopter.getInstance().start();
			JHelicopter.loopAudio.setCycleCount(Timeline.INDEFINITE);
			JHelicopter.loopAudio.setVolume(1.0f);
			if(JHelicopter.playAudio){
			JHelicopter.loopAudio.play();
			}
		}
		
		
		public static void stopAll(){
			if(JHelicopter.playAudio){
			JHelicopter.loopAudio.stop();
			JHelicopter.crashAudio.play();
			}
			JHelicopter.endTime =  System.currentTimeMillis();
			if(wallAnim.getStatus() != Animation.Status.STOPPED)
			wallAnim.stop();
			if(anim.getStatus() != Animation.Status.STOPPED)
			anim.stop();
			if(Helicopter.getInstance().anim.getStatus() != Animation.Status.STOPPED)
			Helicopter.getInstance().anim.stop();
			HelicopterEventHandler.getInstance().stop();
			JHelicopter.finalModal();
		}
		public static void pauseAll(){
			JHelicopter.loopAudio.stop();
			if(wallAnim.getStatus() == Animation.Status.RUNNING)
			wallAnim.pause();
			if(anim.getStatus() == Animation.Status.RUNNING)
			anim.pause();
			if(Helicopter.getInstance().anim.getStatus() == Animation.Status.RUNNING)
			Helicopter.getInstance().anim.pause();
			HelicopterEventHandler.getInstance().pause();
		}
		public static void resumeAll(){
			if(JHelicopter.playAudio){
			JHelicopter.loopAudio.play();
			}
			if(wallAnim.getStatus() == Animation.Status.PAUSED)
			wallAnim.play();
			if(anim.getStatus() == Animation.Status.PAUSED)
			anim.play();
			if(Helicopter.getInstance().anim.getStatus() == Animation.Status.PAUSED)
			Helicopter.getInstance().anim.play();
		}
		public static void stopAudio(){
			JHelicopter.loopAudio.stop();
		}
	}
