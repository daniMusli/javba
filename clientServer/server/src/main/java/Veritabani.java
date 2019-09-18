import com.healthmarketscience.jackcess.Cursor;
import com.healthmarketscience.jackcess.CursorBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

public class Veritabani {

	private Table kullanıciTablosu() {
		Table tablo = null;
		try {
			Database database = DatabaseBuilder
					.open(new File(getClass().getClassLoader().getResource("veritabani.mdb").getFile()));
			tablo = database.getTable("KULLANICI");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tablo;
	}

	/**
	 * veritabanına kullanıcıyı ekler başarılı olursa sonuc 1 değilse 0 döner
	 */
	public int kullaniciEkle(String tc, String adSoyad, String parola, String hesapTuru) {
		Table tablo = kullanıciTablosu();

		try {
			tablo.addRow(1, adSoyad, parola, hesapTuru, 0, false, tc);
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void printTable() {
		for (Row row : kullanıciTablosu()) {
			System.out.println("Veri: " + row);
		}
	}

	/**
	 * veri tabanındaki müşterileri listeler
	 * 
	 * @return
	 */
	public List<Musteri> getMusteriListesi() {
		List<Musteri> musteriler = new ArrayList<Musteri>();
		Musteri musteri;
		for (Row row : kullanıciTablosu()) {
			try {
				musteri = new Musteri(row.getInt("ID"), row.getString("TC"), row.getString("AD_SOYAD"),
						row.getString("PAROLA"), row.getString("HESAP_TURU"),
						row.getString("PARA_MIKTARI") == null ? 0 : Integer.parseInt(row.getString("PARA_MIKTARI")));
				if (musteri.getTc() != null) {
					musteriler.add(musteri);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return musteriler;
	}

	/**
	 * para yatırma veya çekme işlemleri bu fonksiyon tarafından yapılır
	 */
	public int paraYatirParaCek(int hesapNumarasi, int miktar) {
		Cursor cursor;
		try {
			cursor = CursorBuilder.createCursor(kullanıciTablosu());
			int testNum = 1;
			for (Row row : cursor.newIterable().addMatchPattern("ID", hesapNumarasi)) {
				row.put("PARA_MIKTARI", miktar);
				kullanıciTablosu().updateRow(row);
			}
			return 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
