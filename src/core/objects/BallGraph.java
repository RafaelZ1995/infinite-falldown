package core.objects;

import static core.handlers.Cons.BALL_DIAM;
import static core.handlers.Cons.VIR_WIDTH;

/**
 * This class stores the x positions of the predetermined locations the ball will from and to.
 * Created by Rafael on 1/3/2017.
 */
public class BallGraph {

    private float[] graphX;
    private int currentStop;
    private int previousStop;

    public BallGraph(int numStops) {
        graphX = new float[numStops + 2];
        if (numStops <= 0){
            // correct end points
            graphX[0] = graphX[0] + BALL_DIAM;
            graphX[graphX.length - 1] = VIR_WIDTH - BALL_DIAM;
            currentStop = 0;
            return;
        }

        numStops += 2; // end points

        // set stops (predetermined positions)
        for (int i = 0; i < graphX.length; i++){
            float currX = i * VIR_WIDTH / (numStops - 1);
            graphX[i] = currX;
        }

        // correct end points
        graphX[0] = graphX[0] + BALL_DIAM;
        graphX[graphX.length - 1] = graphX[graphX.length - 1] - BALL_DIAM / 2;

        // initialize to a stop in the middle
        System.out.println(graphX.length);
        currentStop = 0;
        previousStop = 0;
    }

    public float getxDestination() {
        return graphX[currentStop];
    }

    public int getCurrentStop() {
        return currentStop;
    }

    public int getPreviousStop() {
        return previousStop;
    }

    public int getSize(){
        return graphX.length - 1;
    }

    public void nextLeftStop(){
        previousStop = currentStop;
        currentStop--;
        if (currentStop < 0) {
            currentStop = 0;
        }

    }

    public void nextRightStop(){
        previousStop = currentStop;
        currentStop++;
        if (currentStop >= graphX.length) {
            currentStop = graphX.length - 1;
        }
    }

    public boolean isNextStopToTheLeft(){
        if (previousStop < currentStop)
            return true;
        else
            return false;
    }

}