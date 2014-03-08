package com.javaxyq.data;

import java.util.HashMap;

public class CharacterConstants {

	private static HashMap<String, String> charNames;
	
	public static String char_0001 = "0001";
	public static String char_0002 = "0002";
	public static String char_0003 = "0003";
	public static String char_0004 = "0004";
	public static String char_0005 = "0005";
	public static String char_0006 = "0006";
	public static String char_0007 = "0007";
	public static String char_0008 = "0008";
	public static String char_0009 = "0009";
	public static String char_0010 = "0010";
	public static String char_0011 = "0011";
	public static String char_0012 = "0012";

	public static String getCharacterName(String character) {
		if(charNames == null) {
			charNames= new HashMap<String, String>();
			charNames.put("0001", "��ң��");
			charNames.put("0002", "������");
			charNames.put("0003", "����Ů");
			charNames.put("0004", "ӢŮ��");
			charNames.put("0005", "��ħ��");
			charNames.put("0006", "��ͷ��");
			charNames.put("0007", "������");
			charNames.put("0008", "�Ǿ���");
			charNames.put("0009", "�����");
			charNames.put("0010", "��̫��");
			charNames.put("0011", "���켧");
			charNames.put("0012", "���ʶ�");
		}
		return charNames.get(character);
	}

}
