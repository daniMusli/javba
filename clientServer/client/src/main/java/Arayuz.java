import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Arayuz extends JFrame {
	private Socket socket;

	private DataInputStream dis;
	private DataOutputStream dos;

	private String tc;

	private String serverdanGelenMesaj;
	private String msg;
	// ***************************************************
	private JTextField tfKullanici = new JTextField();
	private JPasswordField pfSifre = new JPasswordField();
	private JButton btnGiris = new JButton("Giriş");
	private JButton btnHesapAc = new JButton("Hesap Aç");
	/**
	 * anapanel tüm içerik buraya atılıyor
	 */
	private JPanel panel = new JPanel();

	public Arayuz() {
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(3, 2));

		bilesenleriOlustur();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 150);
		centerFrame();
		setContentPane(panel);
		setVisible(true);
		serveraBaglan();
		serveraMesajYolla("" + data.clientId++);
	}

	/**
	 * bileşenler burada set ediliyor
	 */
	public void bilesenleriOlustur() {
		tfKullanici.setBorder(new TitledBorder(new EtchedBorder(), "TC Kimlik Numarası"));
		pfSifre.setBorder(new TitledBorder(new EtchedBorder(), "Şifre"));

		btnHesapAc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hesapOlusturEkrani();
			}
		});

		btnGiris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String girisYapMesaji = "girisYap" + tfKullanici.getText() + "|" + new String(pfSifre.getPassword());
				serveraMesajYolla(girisYapMesaji);
			}
		});

		panel.add(tfKullanici);
		panel.add(pfSifre);
		panel.add(btnGiris);
		panel.add(btnHesapAc);
	}

	/**
	 * pencreyi ekranın tam ortasına getirir
	 */
	public void centerFrame() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		setLocation(screenWidth / 4, screenHeight / 4);

		setTitle("Giriş");
	}

	/**
	 * hesap oluşturma ekranını açar
	 */
	public void hesapOlusturEkrani() {
		final JDialog dialog = new JDialog(this, "Hesap Oluştur", true);

		final JTextField tfTc = new JTextField();
		final JTextField tfAdSoyad = new JTextField();
		final JPasswordField pfSifre = new JPasswordField();

		tfTc.setBorder(new TitledBorder(new EtchedBorder(), "TC Kimlik Numarası"));
		tfAdSoyad.setBorder(new TitledBorder(new EtchedBorder(), "İsim Soyisim"));
		pfSifre.setBorder(new TitledBorder(new EtchedBorder(), "Şifre"));

		final JComboBox<String> cbHesapTuru = new JComboBox<String>();
		cbHesapTuru.setBorder(new TitledBorder(new EtchedBorder(), "Hesap Türü"));
		cbHesapTuru.addItem("Uzun Vadeli");
		cbHesapTuru.addItem("Kısa Vadeli");
		cbHesapTuru.addItem("Vadesiz");
		cbHesapTuru.addItem("Cari");

		JButton btnHesapOlustur = new JButton("Hesap Oluştur");
		JButton btnKapat = new JButton("Kapat");

		btnHesapOlustur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tc = tfTc.getText();
				String adSoyad = tfAdSoyad.getText();
				String sifre = new String(pfSifre.getPassword());

				String hesapAcMesaji = "hesapAc" + tc + "|" + adSoyad + "|" + sifre + "|"
						+ cbHesapTuru.getSelectedItem();
				serveraMesajYolla(hesapAcMesaji);
			}
		});

		btnKapat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1));

		panel.add(tfTc);
		panel.add(tfAdSoyad);
		panel.add(pfSifre);
		panel.add(cbHesapTuru);
		panel.add(btnHesapOlustur);
		panel.add(btnKapat);

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		dialog.setLocation(screenWidth / 4, screenHeight / 4);

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setContentPane(panel);
		dialog.setSize(300, 300);
		dialog.setVisible(true);
	}

	public void serveraBaglan() {
		try {
			socket = new Socket(data.ip, data.port);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			_receive();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * tc / şifre başarılı ise kullanıcı bilgileri ekrana gelir
	 */
	private void kullaniciPaneliniAc(int hesapNumarasi, String tc, String adSoyad, String hesapTuru, double para) {
		/**
		 * ekranı temizler
		 */
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		panel.revalidate();
		// ************************************
		panel.setLayout(new GridLayout(5, 1));

		Label lblHesapNumarasi = new Label("Hesap Numarası: " + hesapNumarasi);
		Label lblTc = new Label("Tc: " + tc);
		Label lblAdSoyad = new Label("Ad Soyad: " + adSoyad);
		Label lblHesapTuru = new Label("Hesap Türü: " + hesapTuru);
		Label lblPara = new Label("Bankadaki Para: " + para);

		/**
		 * para yatırma ve çekme
		 */
		JPanel pnlParaIslemleri = new JPanel();
		JTextField tfPara = new JTextField();
		tfPara.setPreferredSize(new Dimension(50, 30));
		JButton btnParaYatir = new JButton("Para Yatır");
		JButton btnParaCek = new JButton("Para Çek");
		pnlParaIslemleri.add(tfPara);
		pnlParaIslemleri.add(btnParaYatir);
		pnlParaIslemleri.add(btnParaCek);
		
		btnParaYatir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
//****************************************************************
		
		JButton btnHesabiKilitle = new JButton("Hesabı dondur");
		btnHesabiKilitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		panel.add(lblHesapNumarasi);
		panel.add(lblTc);
		panel.add(lblAdSoyad);
		panel.add(lblHesapTuru);
		panel.add(lblPara);
		panel.add(pnlParaIslemleri);
		panel.add(btnHesabiKilitle);
		this.pack();

	}

	/**
	 * serverdan veri gelirse burada işleniyor
	 */
	public void _receive() {
		Thread th = new Thread() {
			public void run() {
				while (true) {
					try {
						serverdanGelenMesaj = dis.readUTF();
						System.out.println("Serverdan Gelen mesaj:" + serverdanGelenMesaj);
						if (serverdanGelenMesaj.startsWith("Hesap Açma")) {
							JOptionPane.showMessageDialog(btnHesapAc, serverdanGelenMesaj);
						}
						if (serverdanGelenMesaj.startsWith("girisBasarili")) {
							String komut = serverdanGelenMesaj.replaceAll("girisBasarili", "");
							String[] parcalanmisKomut = komut.split("\\|");

							int hesapNumarasi = Integer.parseInt(parcalanmisKomut[0]);
							String tc = parcalanmisKomut[1];
							String adSoyad = parcalanmisKomut[2];
							String hesapTuru = parcalanmisKomut[3];
							int para = Integer.parseInt(parcalanmisKomut[4]);

							kullaniciPaneliniAc(hesapNumarasi, tc, adSoyad, hesapTuru, para);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};
		th.start();
	}

	/**
	 * servera mesaj yollar
	 */
	public void serveraMesajYolla(String mesaj) {
		try {
			msg = mesaj;
			System.out.println(tc + " : " + msg);
			dos.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
