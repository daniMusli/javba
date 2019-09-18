
public class Musteri {
	private int hesapNumarasi;
	private String tc;
	private String adSoyad;
	private String parola;
	private String hesapTuru;
	private long para;

	public Musteri(int hesapNumarasi, String tc, String adSoyad, String parola, String hesapTuru, long para) {
		this.hesapNumarasi = hesapNumarasi;
		this.tc = tc;
		this.adSoyad = adSoyad;
		this.parola = parola;
		this.hesapTuru = hesapTuru;
		this.para = para;
	}

	public String getTc() {
		return tc;
	}

	public void setTc(String tc) {
		this.tc = tc;
	}

	public String getAdSoyad() {
		return adSoyad;
	}

	public void setAdSoyad(String adSoyad) {
		this.adSoyad = adSoyad;
	}

	public String getParola() {
		return parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}

	public String getHesapTuru() {
		return hesapTuru;
	}

	public void setHesapTuru(String hesapTuru) {
		this.hesapTuru = hesapTuru;
	}

	public long getPara() {
		return para;
	}

	public void setPara(long para) {
		this.para = para;
	}

	public int getHesapNumarasi() {
		return hesapNumarasi;
	}

	public void setHesapNumarasi(int hesapNumarasi) {
		this.hesapNumarasi = hesapNumarasi;
	}

	@Override
	public String toString() {
		return "Musteri [tc=" + tc + ", adSoyad=" + adSoyad + ", parola=" + parola + ", hesapTuru=" + hesapTuru
				+ ", para=" + para + "]";
	}

}
