package com.cia.lojao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

	public static String timestampToDate(Date data) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data); 
	}
	
	public static String timestampToDate(Long data) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data); 
	}
	
}
