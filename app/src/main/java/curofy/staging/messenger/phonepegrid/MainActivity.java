package curofy.staging.messenger.phonepegrid;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Integer level, m, n, noOfRemovedTiles, score = 0;
    ArrayList<TileObject> tileObjectArrayList;

    LinearLayout mainLL, cardsLL;
    TextView timer;
    TileObject selectedTile1, selectedTile2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLL = (LinearLayout)findViewById(R.id.mainLL);
        cardsLL = (LinearLayout)findViewById(R.id.cardsLL);
        timer = (TextView) findViewById(R.id.timer);

        //TODO MOVE COMPLETE CODE TO FRAGMENT AND CALL REPLACE FRAGMENT EVERY TIME LEVEL IS CHANGED
        if(savedInstanceState != null && !savedInstanceState.isEmpty()) {
            //TODO load saved state
        } else
            getLevelAndSetValues();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void getLevelAndSetValues() {
        level = Sessions.getLevel(this);
        tileObjectArrayList = new ArrayList<>();
        noOfRemovedTiles = 0;

        //TODO for next level
        cardsLL.removeAllViews();
        selectedTile1 = null;
        selectedTile2 = null;
        //get m*n value according to level

        m = 3;
        n = 2;

        //getRequiredTiles
        int totalItems = (m*n)/2;

        //getThese many items from DB ... ie. TileObject
        getItemsList(totalItems);
    }

    private void getItemsList(Integer noOfItems) {

        //get from DB And randomize
        for(int i = 0; i < noOfItems; i++) {
            tileObjectArrayList.add(new TileObject(TileObject.CLOSE, i));
            tileObjectArrayList.add(new TileObject(TileObject.CLOSE, i));
        }

        Collections.shuffle(tileObjectArrayList);

        setArrayListToTiles(2*noOfItems);
    }

    private void setArrayListToTiles(int noOfItems){

        //for now added views in a list only
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mainLL.removeAllViews();
        for(int i = 0; i < noOfItems; i++) {
            final View tile = inflater.inflate(R.layout.image_tile, null);
            tile.setId(i);
            TextView t = (TextView)tile.findViewById(R.id.tile1TV);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 20, 20, 20);
            tile.setLayoutParams(layoutParams);
            t.setText(String.format("%d", tileObjectArrayList.get(i).getValue()));
            t.setVisibility(View.GONE);
            tile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedTile1 != null && selectedTile2 != null) {
                        compareTiles();
                    } else if(selectedTile1 != null && selectedTile2 == null) {
                        selectedTile2 = tileObjectArrayList.get(tile.getId());
                        TextView t = (TextView)tile.findViewById(R.id.tile1TV);
                        t.setVisibility(View.VISIBLE);
                        t.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                compareTiles();
                            }
                        }, 1000);

                    } else {
                        selectedTile1 = tileObjectArrayList.get(tile.getId());
                        TextView t = (TextView)tile.findViewById(R.id.tile1TV);
                        t.setVisibility(View.VISIBLE);
                    }

                }
            });
            tile.setVisibility(View.VISIBLE);
            this.cardsLL.addView(tile);


            //TODO start timer
        }
        startTimer();

    }

    private void compareTiles() {
        if(selectedTile1.getValue().equals(selectedTile2.getValue())){
            removeTiles();
            noOfRemovedTiles += 2;
            if(noOfRemovedTiles == m*n){
                Sessions.setLevel(this, level+1);
                getLevelAndSetValues();
            }
            selectedTile1 = null;
            selectedTile2 = null;
            return;
        } else {
            resetTiles();
        }
    }

    private void removeTiles(){
        cardsLL.removeView(findViewById(tileObjectArrayList.indexOf(selectedTile1)));
        cardsLL.removeView(findViewById(tileObjectArrayList.indexOf(selectedTile2)));
    }

    private void resetTiles(){
        int id1 = tileObjectArrayList.indexOf(selectedTile1);
        int id2 = tileObjectArrayList.indexOf(selectedTile2);

        View tile = findViewById(id1);
        TextView t = (TextView)tile.findViewById(R.id.tile1TV);
        t.setVisibility(View.GONE);

        View tile2 = findViewById(id2);
        TextView t2 = (TextView)tile2.findViewById(R.id.tile1TV);
        t2.setVisibility(View.GONE);

        selectedTile1 = null;
        selectedTile2 = null;
    }

    private void startTimer() {
        int millisInFuture = 100000 - (5000*level);
        new CountDownTimer(millisInFuture, 1000){
            public void onTick(long millisUntilFinished){
                //TODO change progress bar
                timer.setText(millisUntilFinished+"");
            }
            public  void onFinish(){
                //TODO show time up view
                //cardsLL.removeAllViews();
            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putDouble("time", liter);
        //outState.putDouble("level_completed", true or false);
        Toast.makeText(this, "Activity state saved", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
