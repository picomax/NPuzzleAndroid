package kr.co.sunnyvale.npuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends Activity {
    int [] ar = {1,2,3,4,5,6,7,8,0};
    Button [][] puzButtons = new Button[3][3];
    Point curPoint = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnShuffle = (Button)findViewById(R.id.btnShuffle);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                if(v == btn1) { // v와 btn2이 같은 레퍼런스이냐?
                    tv.setText("Hi World");
                    System.out.println("click.. hi1");
                }else if(v == btn2){ // v와 btn2가 같은 레퍼런스이냐?
                    tv.setText("Hello World");
                    System.out.println("click.. hello1");
                }
                */

                if (v == btnShuffle) {
                    suffleNumbers();
                    return;
                }
            }
        };

        btnShuffle.setOnClickListener(listener);

        initButtons(true);
    }

    public void initButtons(boolean listen){
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Point p = (Point)v.getTag();
                if(curPoint.equals(p)) {
                    System.out.println("blank : " + p.toString());
                }else if((curPoint.x == p.x && Math.abs(curPoint.y - p.y) < 2) || (curPoint.y == p.y && Math.abs(curPoint.x - p.x) < 2)) {
                    Button touchedButton = (Button)v;
                    Button blankButton = (Button)findViewById( getResources().getIdentifier("btn" + curPoint.x + curPoint.y, "id", getPackageName()) );

                    if(blankButton != null){
                        blankButton.setText(touchedButton.getText());
                        touchedButton.setText("");
                        curPoint.set(p.x, p.y);
                    }

                    if(checkComplete() == true){
                        showCompleteAlert("Good Job.. Again?");
                        System.out.println("Complete!!!!!");
                    }

                    System.out.println("near : " + p.toString());
                }else{
                    System.out.println("far : " + p.toString());
                }
            }
        };

        int count = 0;
        for(int r=0; r<puzButtons.length; r++){
            for(int c=0; c<puzButtons[r].length; c++){
                // getResources().getIdentifier(패키지명:디렉토리/파일명, null, null);
                //String buttonName = getPackageName() + ":id/btn" + r + c;
                //int id = getResources().getIdentifier(buttonName, null, null);

                // 또는, getResources().getIdentifier(파일명, 디렉토리명, 패키지명);
                int id = getResources().getIdentifier("btn" + r + c, "id", getPackageName());

                puzButtons[r][c] = (Button)findViewById(id);
                puzButtons[r][c].setText(Integer.toString(ar[count]));
                if(listen == true) {
                    puzButtons[r][c].setOnClickListener(listener);
                }
                puzButtons[r][c].setTag(new Point(r,c));

                if(ar[count] == 0){
                    curPoint.set(r, c);
                    puzButtons[r][c].setText("");
                }

                count++;
            }
        }
    }

    public void showCompleteAlert(String message){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
        alert_confirm.setMessage(message).setCancelable(false).setPositiveButton("Again!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        suffleNumbers();
                    }
                }).setNegativeButton("Fuck you!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    public void suffleNumbers(){
        int count = 0;

        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }

        initButtons(false);
    }

    public boolean checkComplete(){
        int count = 1;
        for(int r=0; r<puzButtons.length; r++){
            for(int c=0; c<puzButtons[r].length; c++){
                String chk = count%10 == 0 ? "" : Integer.toString(count);
                Button btn = (Button)findViewById( getResources().getIdentifier("btn" + r + c, "id", getPackageName()) );
                if(btn == null){
                    return false;
                }

                if(chk.equals(btn.getText()) == false){
                    return false;
                }

                if(count >= ar.length - 1){
                    return true;
                }

                count++;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
