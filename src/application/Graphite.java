

package application;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import updatePlayer.updateUI;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;


public class Graphite{
	
	public static MediaPlayerFactory mpf;
	public static EmbeddedMediaPlayer emp;
	public static MediaListPlayer mlp;
	static boolean isPlaying = true;
	static boolean toggle = true;
	static boolean repeatT = true;
	public static JSlider positionSlider;
	public static JLabel ct,td;
	public static JButton playb;
	public static JFrame f;
	public static File[] files;
	public static int i,j=0;
	public static Canvas c;
	public static JList<String> playlistView;
	public static DefaultListModel<String> listModel;
	public static MediaList medialist;
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void play() {

		f = new JFrame();
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(Graphite.class.getResource("/application/Icons/App Icon/DPlayer.png")));
		f.setTitle("Dynamic Player");
		f.setLocation(20,70);
		f.setSize(1066,600);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();
		p.setBorder(null);
		f.getContentPane().add(p);
		p.setLayout(new BorderLayout());

		c = new Canvas();
		p.add(c);
		c.setBackground(Color.black);
		
		JPanel playlistPanel = new JPanel();
		playlistPanel.setBackground(new Color(35, 39, 45));
		p.add(playlistPanel, BorderLayout.WEST);
		playlistPanel.setLayout(new BorderLayout());
		playlistPanel.setVisible(false);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(24, 24, 24));
		p.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		javax.swing.JButton playb = new javax.swing.JButton("");
		panel.add(playb);
		playb.setOpaque(false);
		playb.setContentAreaFilled(false);
		playb.setBorderPainted(false);
		playb.setToolTipText("Play/Pause");
		playb.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/play.png")));

		JButton stop = new JButton("");
		stop.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/stop.png")));
		stop.setOpaque(false);
		stop.setContentAreaFilled(false);
		stop.setBorderPainted(false);
		stop.setToolTipText("Stop");
		panel.add(stop);
		
		JButton open = new JButton("");
		panel.add(open);
		open.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/open.png")));
		open.setOpaque(false);
		open.setContentAreaFilled(false);
		open.setToolTipText("Open Media File");
		open.setBorderPainted(false);
		
		JButton audioToggle = new JButton("");
		audioToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = emp.getAudioDescriptions().get(2).id();
				emp.setAudioTrack((emp.getAudioTrack()==1 && emp.getAudioTrackCount()==3) ? i : 1);
			}
		});
		
		listModel = new DefaultListModel<>();
		playlistView = new JList<>(listModel);
		playlistView.setForeground(new Color(181, 210, 252));
		playlistView.setBackground(new Color(35, 39, 45));
		JScrollPane scrollPane = new JScrollPane(playlistView);
		scrollPane.setBorder(null);
		playlistView.setBorder(null);
		playlistView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playlistPanel.add(scrollPane,BorderLayout.CENTER);
		
		JPanel toolbar = new JPanel();
		playlistPanel.add(toolbar, BorderLayout.NORTH);
		toolbar.setBackground(new Color(24,24,24));
		toolbar.setBorder(null);
		
		JButton Prev = new JButton("");
		Prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mlp.isPlaying()){
					mlp.playPrevious();
				}
			}
		});
		Prev.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/prev.png")));
		Prev.setToolTipText("Prev Media");
		Prev.setOpaque(false);
		Prev.setFocusable(false);
		Prev.setContentAreaFilled(false);
		Prev.setBorderPainted(false);
		toolbar.add(Prev);
		
		JButton repeat = new JButton("");
		repeat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(repeatT == true){
					repeat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/repeat-once.png")));
					mlp.setMode(MediaListPlayerMode.REPEAT);
					repeatT = false;
					repeat.setToolTipText("Repeat All");
				}
				else{
					repeat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/repeat.png")));
					mlp.setMode(MediaListPlayerMode.LOOP);
					repeatT = true;
					repeat.setToolTipText("Repeat Single");
				}	
			}
		});
		repeat.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/repeat.png")));
		repeat.setToolTipText("Repeat Single");
		repeat.setOpaque(false);
		repeat.setFocusable(false);
		repeat.setContentAreaFilled(false);
		repeat.setBorderPainted(false);
		toolbar.add(repeat);
		
		JButton next = new JButton("");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mlp.isPlaying())
				{
					mlp.playNext();
				}
			}
		});
		next.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/next.png")));
		next.setToolTipText("Next Media");
		next.setOpaque(false);
		next.setFocusable(false);
		next.setContentAreaFilled(false);
		next.setBorderPainted(false);
		toolbar.add(next);
		
		JButton add = new JButton("");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Add Files to Playlist");
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Media Files", new String[] {"3g2","3gp","3gp2","3gpp","amv","asf","avi","bik","bin","divx","drc","dv","evo","f4v","flv",  "gvi","gxf","iso","m1v","m2v","m2t",  "m2ts",  "m4v",  "mkv",  "mov",  "mp2",  "mp2v",  "mp4",  "mp4v",  "mpe",  "mpeg",  "mpeg1",  "mpeg2",  "mpeg4",  "mpg",  "mpv2",  "mts",  "mtv",  "mxf",  "mxg",  "nsv",  "nuv",  "ogg",  "ogm",  "ogv",  "ogx",  "ps",  "rec",  "rm",  "rmvb",  "rpl",  "thp",  "tod",  "ts",  "tts",  "txd",  "vob",  "vro",  "webm",  "wm",  "wmv",  "wtv",  "xesc",  "3ga",  "669",  "a52",  "aac",  "ac3",  "adt",  "adts",  "aif",  "aifc",  "aiff",  "amb",  "amr",  "aob",  "ape",  "au",  "awb",  "caf",  "dts",  "flac",  "it",  "kar",  "m4a",  "m4b",  "m4p",  "m5p",  "mid",  "mka",  "mlp",  "mod",  "mpa",  "mp1",  "mp2",  "mp3",  "mpc",  "mpga",  "mus",  "oga",  "ogg",  "oma",  "opus",  "qcp",  "ra",  "rmi",  "s3m",  "sid",  "spx",  "tak",  "thd",  "tta",  "voc",  "vqf",  "w64","wav","wma","wv","xa","xm"});
				fileChooser.setFileFilter(filter);
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setMultiSelectionEnabled(true);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					playlistPanel.setVisible(false);
					playlistPanel.setVisible(true);
					files = fileChooser.getSelectedFiles();
					for(i=0;i<files.length;i++){
						mlp.getMediaList().insertMedia(mlp.getMediaList().size(),files[i].getAbsolutePath());
						}
						listModel.clear();
						for(i=0;i<mlp.getMediaList().size();i++){
							listModel.addElement(mlp.getMediaList().items().get(i).name());
						}
					}
			}
		});
		
		add.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/add.png")));
		add.setToolTipText("Add Files to Playlist");
		add.setOpaque(false);
		add.setFocusable(false);
		add.setContentAreaFilled(false);
		add.setBorderPainted(false);
		toolbar.add(add);
		
		
		JButton playlist = new JButton("");
		playlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(mlp.getMediaList().items().isEmpty()==false){
				if(toggle){
					playlistPanel.setVisible(true);
					toggle = false;
					if(mlp.getMediaList().items().isEmpty()==false){
						listModel.clear();
					for(i=0;i<mlp.getMediaList().size();i++){
						listModel.addElement(mlp.getMediaList().items().get(i).name());
						}	
					}
				}
				else{
					playlistPanel.setVisible(false);
					toggle = true;
					if(mlp.getMediaList().items().isEmpty()==false){
						listModel.clear();
					for(i=0;i<mlp.getMediaList().size();i++){
						listModel.addElement(mlp.getMediaList().items().get(i).name());
						}
					}
				}
			}
			}
		});
		
		
		playlistView.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mlp.playItem(playlistView.getSelectedIndex());
                    if(emp.isPlaying()==false){
                    	playb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/pause.png")));
                    }
                }
            }
        });
		
		playlist.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/playlist.png")));
		panel.add(playlist);
		playlist.setOpaque(false);
		playlist.setContentAreaFilled(false);
		playlist.setToolTipText("View Playlist");
		playlist.setBorderPainted(false);

		JButton fullscreenb = new JButton("");
		panel.add(fullscreenb);
		fullscreenb.setOpaque(false);
		fullscreenb.setContentAreaFilled(false);
		fullscreenb.setBorderPainted(false);
		fullscreenb.setToolTipText("Toggle Fullscreen");
		fullscreenb.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/fullscreenplayer.png")));
		
		positionSlider = new JSlider();
		positionSlider.setBorder(null);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		positionSlider.setForeground(Color.CYAN);
		positionSlider.setBackground(new Color(24, 24, 24));
		positionSlider.setPreferredSize(new Dimension(500, 40));
		panel.add(positionSlider);

		ct = new JLabel("00:00:00");
		ct.setForeground(new Color(189, 195, 199));
		panel.add(ct);

		JLabel label_2 = new JLabel("/");
		label_2.setForeground(new Color(189, 195, 199));
		label_2.setFocusable(false);
		panel.add(label_2);

		td = new JLabel("00:00:00");
		td.setForeground(new Color(189, 195, 199));
		td.setFocusable(false);
		panel.add(td);

		audioToggle.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/audioToggle.png")));
		audioToggle.setOpaque(false);
		audioToggle.setContentAreaFilled(false);
		audioToggle.setBorderPainted(false);
		audioToggle.setToolTipText("Toggle Audio Tracks");
		panel.add(audioToggle);
		
		JButton subtitles = new JButton("");
		subtitles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select Subtitle");
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Subtitles File", new String[] {"cdg","idx","srt","sub","utf","ass","ssa","aqt","jss","psb","rt","smi","txt","smil","stl","usf","dks","pjs","mpl2","mks","vtt"});
				fileChooser.setFileFilter(filter);
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File fileName = fileChooser.getSelectedFile();
					emp.setSubTitleFile(fileName.getAbsolutePath().toString());
			}
		}
		});
		subtitles.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/subtitles.png")));
		subtitles.setOpaque(false);
		subtitles.setContentAreaFilled(false);
		subtitles.setBorderPainted(false);
		subtitles.setToolTipText("Select Subtitle");
		panel.add(subtitles);
		
		
		JButton snapshot = new JButton("");
		snapshot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BufferedImage img = emp.getSnapshot();
				String path = System.getProperty("user.home")+"\\Pictures\\";
				String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
				File imgSnap = new File(path+fileName+".png");
				System.out.println(imgSnap.getAbsolutePath());
				try {
					ImageIO.write(img, "png", imgSnap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		snapshot.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/snapshot.png")));
		snapshot.setToolTipText("Take Snapshot");
		snapshot.setOpaque(false);
		snapshot.setFocusable(false);
		snapshot.setContentAreaFilled(false);
		snapshot.setBorderPainted(false);
		panel.add(snapshot);
		
		
		JButton theme = new JButton("");
		theme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String[] theme = { "Graphite", "Colorful", "Metal"};
				String selectedTheme = (String) JOptionPane.showInputDialog(f,"Application will Shut Down!!! Theme will be applied on next restart. Select Theme.","Themes",JOptionPane.QUESTION_MESSAGE,null,theme,theme[0]);
				if(selectedTheme!=null){
						try {
							themeChanger(selectedTheme);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
			}
		});
		theme.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/theme.png")));
		theme.setOpaque(false);
		theme.setContentAreaFilled(false);
		theme.setBorderPainted(false);
		theme.setToolTipText("Change Theme");
		panel.add(theme);
		
		JLabel label = new JLabel("");
		label.setForeground(Color.WHITE);
		panel.add(label);

		JLabel Volume = new JLabel("");
		Volume.setIcon(new ImageIcon(Graphite.class.getResource("/application/Icons/Graphite/mvol.png")));
		Volume.setForeground(Color.WHITE);
		Volume.setToolTipText("Control Volume");
		panel.add(Volume);

		JSlider volumeSlider = new JSlider();
		volumeSlider.setBorder(null);
		volumeSlider.setBackground(new Color(24, 24, 24));
		volumeSlider.setForeground(Color.CYAN);
		volumeSlider.setPreferredSize(new Dimension(100, 40));
		panel.add(volumeSlider);

		playb.setFocusable(false);
		fullscreenb.setFocusable(false);
		open.setFocusable(false);
		positionSlider.setFocusable(false);
		volumeSlider.setFocusable(false);
		ct.setFocusable(false);
		stop.setFocusable(false);
		playlist.setFocusable(false);
		audioToggle.setFocusable(false);
		subtitles.setFocusable(false);
		theme.setFocusable(false);
		
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			 playb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/play.png")));
			 positionSlider.setValue(0);
			 f.setTitle("Dynamic Player");
             mlp.stop();
             if(playlistPanel.isVisible()==true){
             playlistPanel.repaint();
             playlistPanel.setVisible(false);
             toggle = true;
             }
             medialist.clear();
             mlp.getMediaList().clear(); 
             listModel.clear();
             playlistView.removeAll();
			}
		});
		
		
		positionSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JSlider slider = (JSlider) e.getSource();
				slider.setValue(e.getX()*2);
				slider.repaint();
				emp.setTime((long) (slider.getValue()* updateUI.factor));	
			}
		});


		fullscreenb.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				emp.toggleFullScreen();
				try {
		            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		        } catch (ClassNotFoundException | InstantiationException
		                | IllegalAccessException | UnsupportedLookAndFeelException exx) {
		            exx.printStackTrace();
		        }
		        SwingUtilities.updateComponentTreeUI(f);

				if(emp.isFullScreen()== true)
				{
					panel.setVisible(false);
					playlistPanel.setVisible(false);
				try {
		            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		           } catch (ClassNotFoundException | InstantiationException
		                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
		            ex.printStackTrace();
		        }
		        SwingUtilities.updateComponentTreeUI(f);

			}
		}});

		
		Volume.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isPlaying) {
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));
					isPlaying = false;
					emp.mute();
				} else {
					emp.setVolume(volumeSlider.getValue()*2);
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));
					if(volumeSlider.getValue()>10 && volumeSlider.getValue()<30)
						Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/lvol.png")));
					else if(volumeSlider.getValue()>30 && volumeSlider.getValue()<60)
						Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mvol.png")));
					else if(volumeSlider.getValue()<=100 && volumeSlider.getValue()>=60)
						Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/hvol.png")));
					isPlaying = true;
					emp.mute();
				}
			}
		});


		volumeSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JSlider source = (JSlider)e.getSource();
				source.setValue(e.getX());
				emp.setVolume(source.getValue()*2);
				if(volumeSlider.getValue()<=1)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));
				else if(volumeSlider.getValue()<30)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/lvol.png")));
				else if(volumeSlider.getValue()>30 && volumeSlider.getValue()<60)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mvol.png")));
				else if(volumeSlider.getValue()<=100 && volumeSlider.getValue()>=60)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/hvol.png")));
				else if(volumeSlider.getValue()<3)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));

			}
		});
		
		
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				emp.setVolume(source.getValue()*2);
				if(volumeSlider.getValue()<=1)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));
				else if(volumeSlider.getValue()<30)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/lvol.png")));
				else if(volumeSlider.getValue()>30 && volumeSlider.getValue()<60)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mvol.png")));
				else if(volumeSlider.getValue()<=100 && volumeSlider.getValue()>=60)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/hvol.png")));
				else if(volumeSlider.getValue()<3)
					Volume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/mute.png")));
			}
		});


		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				if(panel.isVisible()== false )
					{
					panel.setVisible(true);

					try {
			            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
			        } catch (ClassNotFoundException | InstantiationException
			                | IllegalAccessException | UnsupportedLookAndFeelException exx) {
			            exx.printStackTrace();
			        }
			        SwingUtilities.updateComponentTreeUI(f);

					}
				else if(panel.isVisible()== true && emp.isFullScreen()== true)
				{
					panel.setVisible(false);
					try {
			            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			           } catch (ClassNotFoundException | InstantiationException
			                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			            ex.printStackTrace();
			        }
			        SwingUtilities.updateComponentTreeUI(f);
				}
			}
		});


		open.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Media Files", new String[] {"3g2","3gp","3gp2","3gpp","amv","asf","avi","bik","bin","divx","drc","dv","evo","f4v","flv",  "gvi","gxf","iso","m1v","m2v","m2t",  "m2ts",  "m4v",  "mkv",  "mov",  "mp2",  "mp2v",  "mp4",  "mp4v",  "mpe",  "mpeg",  "mpeg1",  "mpeg2",  "mpeg4",  "mpg",  "mpv2",  "mts",  "mtv",  "mxf",  "mxg",  "nsv",  "nuv",  "ogg",  "ogm",  "ogv",  "ogx",  "ps",  "rec",  "rm",  "rmvb",  "rpl",  "thp",  "tod",  "ts",  "tts",  "txd",  "vob",  "vro",  "webm",  "wm",  "wmv",  "wtv",  "xesc",  "3ga",  "669",  "a52",  "aac",  "ac3",  "adt",  "adts",  "aif",  "aifc",  "aiff",  "amb",  "amr",  "aob",  "ape",  "au",  "awb",  "caf",  "dts",  "flac",  "it",  "kar",  "m4a",  "m4b",  "m4p",  "m5p",  "mid",  "mka",  "mlp",  "mod",  "mpa",  "mp1",  "mp2",  "mp3",  "mpc",  "mpga",  "mus",  "oga",  "ogg",  "oma",  "opus",  "qcp",  "ra",  "rmi",  "s3m",  "sid",  "spx",  "tak",  "thd",  "tta",  "voc",  "vqf",  "w64","wav","wma","wv","xa","xm"});
				fileChooser.setFileFilter(filter);
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setMultiSelectionEnabled(true);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					files = fileChooser.getSelectedFiles();
					medialist = mpf.newMediaList();
						for(i=0;i<files.length;i++){
						if(mlp.isPlaying()){
							medialist.clear();
							mlp.stop();
						}
						medialist.addMedia(files[i].getAbsolutePath());
						}
					mlp.setMediaList(medialist);
					mlp.setMode(MediaListPlayerMode.DEFAULT);
					mlp.play();
					new updateUI(positionSlider,emp,ct,td,mlp,f).start();
					try {
						f.setTitle("Dynamic Player"+"   |   "+URLDecoder.decode(files[0].getName(), "UTF-8"));
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					playb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/pause.png")));
					listModel.clear();
					for(i=0;i<mlp.getMediaList().size();i++){
						listModel.addElement(mlp.getMediaList().items().get(i).name());
					}
				}
			}
		});


		playb.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed(java.awt.event.ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (isPlaying) {
					playb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/play.png")));
					isPlaying = false;
					emp.pause();
				} else {
					playb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/application/Icons/Graphite/pause.png")));
					isPlaying = true;
					emp.pause();
				}
			}
		});

		f.setVisible(true);
		f.pack();
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),"lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		mpf = new MediaPlayerFactory();
		emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(f));
	    emp.setVideoSurface(mpf.newVideoSurface(c));
		mlp = mpf.newMediaListPlayer();
		mlp.setMediaPlayer(emp);
		emp.setEnableMouseInputHandling(false);
		emp.setEnableKeyInputHandling(false);
	}


	public static void themeChanger(String st) throws IOException{
		//String filePath = new File("").getAbsolutePath();
		String path = System.getProperty("user.home")+"\\Documents\\";
		//BufferedWriter writer = new BufferedWriter( new FileWriter(filePath + "/src/application/theme.txt"));
		BufferedWriter writer = new BufferedWriter( new FileWriter(path + "theme.txt"));
	    writer.write(st);
	    writer.close();
	    System.exit(0);
	}
		

	public static void main(String[] args) {

	           try {
				UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	            play();
	}


}

