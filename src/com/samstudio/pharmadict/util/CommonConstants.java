package com.samstudio.pharmadict.util;

public class CommonConstants {
	public static final String URL = "http://192.168.1.4/";
	public static final String WEB_SERVICE_URL = URL+"pharmadict/";
	public static final String WEB_SERVICE_URL_SEARCH = URL+"pharmadict/obat.php/key/obat/";
	public static final String WEB_SERVICE_URL_SEARCH_PENGOBATAN = URL+"pharmadict/obat.php/key/indikasi/";
	public static final String WEB_SERVICE_URL_IMAGE = URL+"pharmadict/pic/";
	public static final String WEB_SERVICE_URL_GET_SINGLE = URL+"pharmadict/obat.php/";
	public static final String WEB_SERVICE_URL_POST_FORM = URL+"pharmadict/login.php";
	public static final String WEB_SERVICE_URL_POST_UPDATE_FORM = URL+"pharmadict/input.php";
	public static final String WEB_SERVICE_URL_DELETE_OBAT = URL+"pharmadict/delete.php?id=";
	
	public static final String TAG_SUCCESS = "success";
	public static final String TAG_OBAT = "obat";
	public static final String TAG_OBATID = "obat_id";
	public static final String TAG_OBATNAMA = "obat_nama";
	public static final String TAG_OBATDESKRIPSI = "obat_deskripsi";
	public static final String TAG_OBATINDIKASI = "obat_indikasi";
	public static final String TAG_OBATHARGA = "obat_harga";
	public static final String TAG_OBATJENIS = "obat_jenis";
	public static final String TAG_OBATPIC = "obat_pic";
	public static final String TAG_OBATEFEKSAMPING = "obat_efeksamping";
	public static final String TAG_OBATDOSIS = "obat_dosis";
	public static final String TAG_OBATPERHATIAN = "obat_perhatian";
	public static final String TAG_OBATISI = "obat_isi";
	public static final String TAG_OBATKODE = "obat_kode";
	
	public static final String PLEASE_INPUT_KEYWORD = "Masukkan kata kunci!";
	public static final String LOADING_CONTENT = "Memuat konten...";
	public static final String INPUT_USENAME_AND_PASSWORD = "Masukkan username dan password";
	public static final String PLEASE_WAIT = "Silahkan tunggu...";
	public static final String INVALID_INPUT = "Username dan password tidak cocok!";
	public static final String KEYWORD_NOT_FOUND = "Kata kunci tidak ditemukan!";
	public static final String UPDATING_FAILED = "Tidak berhasil diperbarui!";
	
	public static final int CONNECTION_TIMEOUT_IN_SEC = 15;
	public static final String CONNECTION_TIMED_OUT = "Connection Time Out";
	
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String KEYWORD = "keyword";
	public static final String GET = "GET";
	
	public static final String INDICATION_SEARCH = "Pencarian Indikasi";
	public static final String OBAT_SEARCH = "Pencarian Obat";
	
	public static final String INPUT_SUCCEED = "Input Berhasil";
	public static final String UPDATE_SUCCEED = "Update Berhasil";
	public static final String DELETE_SUCCEED = "Delete Berhasil";
	public static final String DELETE_FAILED = "Delete Gagal";
	
	public static final String UPDATE = "Update";
	public static final String DELETE = "Delete";
}
