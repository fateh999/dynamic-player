package updatePlayer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventListener;

public class updateUI extends Thread{

private JSlider positionSlider;
public EmbeddedMediaPlayer emp;
public MediaListPlayer mlp;
long cTime,tTime;
JLabel ct,td;
JFrame f;


 public static double factor;


 
	public updateUI(JSlider positionSlider,EmbeddedMediaPlayer emp,JLabel ct,JLabel td,MediaListPlayer mlp,JFrame f) {
		// TODO Auto-generated method stub
      this.positionSlider = positionSlider;
      this.emp = emp;
      this.ct = ct;
      this.td = td;
      this.mlp = mlp;
      this.f = f;
	}
	

	public void run() {

		while(true)
		{	
			if(emp.isPlayable())
			{
			    factor = emp.getLength()/1000;
				positionSlider.setValue( (int) (emp.getTime() /factor));
				
				mlp.addMediaListPlayerEventListener(new MediaListPlayerEventListener() {

						@Override
						public void mediaDurationChanged(MediaListPlayer arg0, long arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mediaFreed(MediaListPlayer arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mediaMetaChanged(MediaListPlayer arg0, int arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mediaParsedChanged(MediaListPlayer arg0, int arg1) {
							// TODO Auto-generated method stub
							//f.setTitle("Dynamic Player"+"   |   "+arg0.getMediaList().items().get(arg1).name());
						}

						@Override
						public void mediaStateChanged(MediaListPlayer arg0, int arg1) {
							// TODO Auto-generated method stub
						}

						@Override
						public void mediaSubItemAdded(MediaListPlayer arg0, libvlc_media_t arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void nextItem(MediaListPlayer arg0, libvlc_media_t arg1, String arg2) {
							// TODO Auto-generated method stub
							File v = new File(arg2);
							try {
								f.setTitle("Dynamic Player"+"   |   "+URLDecoder.decode(v.getName(), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void played(MediaListPlayer arg0) {
							// TODO Auto-generated method stub
						}

						@Override
						public void stopped(MediaListPlayer arg0) {
							// TODO Auto-generated method stub
							
						}	
					});
			}
			else
			{
				positionSlider.setValue(positionSlider.getValue());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 cTime = emp.getTime();
			 String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(cTime), TimeUnit.MILLISECONDS.toMinutes(cTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(cTime)), TimeUnit.MILLISECONDS.toSeconds(cTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(cTime)));
		        ct.setText(s);

             tTime = emp.getLength();
             String t = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(tTime), TimeUnit.MILLISECONDS.toMinutes(tTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tTime)), TimeUnit.MILLISECONDS.toSeconds(tTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tTime)));
             td.setText(t);
             
             if(!emp.isPlayable())
             {
            	 emp.stop();
            	 positionSlider.setValue(0);
            	 break;
             }
		}
	}


}

