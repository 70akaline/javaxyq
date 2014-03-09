package com.javaxyq.data;

import java.util.HashMap;

public class CharacterUtils {

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
	

	/**
	 * �ж��Ƿ�Ϊ�ý�ɫ����ѡ����
	 * @param character
	 * @param type
	 * @return
	 */
	public static boolean isFirstWeapon(String character, String type) {
		if(char_0001.equals(character)) {		
			return "��".equals(type);
		}else if(char_0002.equals(character)){
			return "��".equals(type);
		}else if(char_0003.equals(character)){
			return "��Ȧ".equals(type);
		}else if(char_0004.equals(character)){
			return "��".equals(type);
		}else if(char_0005.equals(character)){
			return "��".equals(type);
		}else if(char_0006.equals(character)){
			return "��".equals(type);
		}else if(char_0007.equals(character)){
			return "��".equals(type);
		}else if(char_0008.equals(character)){
			return "ħ��".equals(type);
		}else if(char_0009.equals(character)){
			return "��".equals(type);
		}else if(char_0010.equals(character)){
			return "ǹì".equals(type);
		}else if(char_0011.equals(character)){
			return "��Ȧ".equals(type);
		}else if(char_0012.equals(character)){
			return "ħ��".equals(type);			
		}
		return false;
	}


	/**
	 * �ж��Ƿ�Ϊ��ͨ״̬��վ�������ߣ�
	 * @param state
	 * @return
	 */
	public static boolean isNormalState(String state) {
		return "stand".equals(state)||"walk".equals(state);
	}

}
