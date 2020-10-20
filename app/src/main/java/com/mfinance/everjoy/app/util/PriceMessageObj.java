package com.mfinance.everjoy.app.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;

public class PriceMessageObj extends MessageObj
{
	public int iDP = 0;
	private int iFieldDelimitor = 17;
	private byte byFieldDelimitor = (byte)iFieldDelimitor;
	private byte byEmptyDepthDelimitor = (byte)-128;
	private byte byBodyFieldDelimitor = (byte)19;
	private static HashMap<String, Integer> hmDecimalPlaceMap = new HashMap<String, Integer>();

	private int iDepthLevel = 1;
        
	public PriceMessageObj()
	{
		m_serviceType = 0;
        m_functionType = 0;
	}
	
	public PriceMessageObj(int iServiceType, int iFunctionType)
	{
		m_serviceType = iServiceType;
		m_functionType = iFunctionType;
	}
	
	public static void addDecimalPlace(String key, int val)
	{
		hmDecimalPlaceMap.put(key, val);	
	}
	
	public static void cleanUp()
	{
		hmDecimalPlaceMap.clear();	
	}
	
	public boolean parse(byte[] bytes)
	{
		try
		{
			String sMkt = null;
			byte[] tmpByte;
			int i = 0, i2 = 0;
			while(bytes[i] != iFieldDelimitor) {i++;}
			tmpByte = new byte[i];
			System.arraycopy(bytes, 0, tmpByte, 0, i);
			sMkt = new String(tmpByte);
			m_body.put("mkt1", sMkt);
			m_body.put("noitem", "1");
			if (!hmDecimalPlaceMap.containsKey(sMkt))
			{
				return false;
			}
			int iDP = hmDecimalPlaceMap.get(sMkt);
			i++;	// iFieldDelimitor
			i2 = i;
			while(bytes[i] != iFieldDelimitor) {i++;}
			if (i > i2)
			{
				tmpByte = new byte[i-i2];
				System.arraycopy(bytes, i2, tmpByte, 0, i-i2);
				m_body.put("tag1", new String(tmpByte));
			}
			i++;	// iFieldDelimitor
			if (m_functionType == 1 || m_functionType == 4){
				tmpByte = new byte[4];
				System.arraycopy(bytes, i, tmpByte, 0, 4);
				m_body.put("bid1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
				i+=4;
				System.arraycopy(bytes, i, tmpByte, 0, 4);
				m_body.put("ask1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
				i+=4;
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("hi1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("low1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("hiask1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("lowask1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
			}
			else
			{
				int tmpInt;
				BigDecimal LotDP = new BigDecimal(10.0).pow(CompanySettings.NUM_OF_LOT_DP);
				BigDecimal tmpBD;
				tmpByte = new byte[1];
				System.arraycopy(bytes, i, tmpByte, 0, 1);
				iDepthLevel = (int)tmpByte[0];
				i++;
				if (iDepthLevel < 1 || iDepthLevel > CompanySettings.MARKET_DEPTH_LEVEL) {
					return false;
				}
				else
					m_body.put("depth", String.valueOf(CompanySettings.DEPTH_PRICE));
				for (int j=1;j<=iDepthLevel;j++)
				{
					if (bytes[i] == byEmptyDepthDelimitor)
					{
						i++;
					}
					else
					{
						tmpByte = new byte[4];
						System.arraycopy(bytes, i, tmpByte, 0, 4);
						m_body.put("bid"+j, decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
						i+=4;
						tmpByte = new byte[3];
						System.arraycopy(bytes, i, tmpByte, 0, 3);
						tmpInt = (tmpByte[2] & 0xFF) | ((tmpByte[1]) << 8) | ((tmpByte[0] & 0x0F) << 16);
						tmpBD = new BigDecimal(tmpInt).divide(LotDP, CompanySettings.NUM_OF_LOT_DP, BigDecimal.ROUND_HALF_UP);
						m_body.put("bidlot"+j, tmpBD.toString());
						i+=3;
					}
					if (bytes[i] == byEmptyDepthDelimitor)
					{
						i++;
					}
					else
					{
						tmpByte = new byte[4];
						System.arraycopy(bytes, i, tmpByte, 0, 4);
						m_body.put("ask"+j, decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
						i+=4;
						tmpByte = new byte[3];
						System.arraycopy(bytes, i, tmpByte, 0, 3);
						tmpInt = (tmpByte[2] & 0xFF) | ((tmpByte[1]) << 8) | ((tmpByte[0] & 0x0F) << 16);
						tmpBD = new BigDecimal(tmpInt).divide(LotDP, CompanySettings.NUM_OF_LOT_DP, BigDecimal.ROUND_HALF_UP);
						m_body.put("asklot"+j, tmpBD.toString());
						i+=3;
					}
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("hi1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("low1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("hiask1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
				if (bytes.length > i)
				{
					System.arraycopy(bytes, i, tmpByte, 0, 4);
					m_body.put("lowask1", decodePrice(ByteBuffer.wrap(tmpByte).getInt(), iDP));
					i+=4;
				}
			}
			return true;
		}
		catch(Exception e)
		{
			Log.i("PriceMessageObject", e.toString());
			return false;
		}
	}

	public boolean parse(ByteBuffer buffer) {
		byte[] temp = new byte[buffer.remaining()];
		buffer.get(temp);
		return this.parse(temp);
	}
	
	private String decodePrice(int price, int iDP)
	{
		BigDecimal bdDP = new BigDecimal(10.0).pow(iDP);
		BigDecimal bdTmp = new BigDecimal(price).divide(bdDP, iDP, BigDecimal.ROUND_HALF_UP);
		return bdTmp.toString();
	}
	
	private byte[] intTo2ByteArray(int value)
	{
		return new byte[] {
				(byte)(value >>> 8),
				(byte)value
				};
	}
}
