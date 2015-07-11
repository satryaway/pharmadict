package com.samstudio.pharmadict.helpers;

import com.samstudio.pharmadict.R;

public class Seeder {
	public static String setJenisObat(int jenis){
		String jenisObat;
		switch(jenis){
			case 1 : jenisObat = "Tablet"; break;
			case 2 : jenisObat = "Kapsul"; break;
			case 3 : jenisObat = "Sirup"; break;
			case 4 : jenisObat = "Puyer"; break;
			case 5 : jenisObat = "Cream"; break;
			case 6 : jenisObat = "Salep"; break;
			case 7 : jenisObat = "Minyak"; break;
			default : jenisObat = ""; break;
		}
		
		return jenisObat;
	}
	
	public static int kodeObat(int kode){
		int kodeObat;
		switch(kode){
		case 1 : kodeObat = R.drawable.circle_green; break;
		case 2 : kodeObat = R.drawable.circle_blue; break;
		default : kodeObat = R.drawable.circle_red; break;
		}
		return kodeObat;
	}
}
